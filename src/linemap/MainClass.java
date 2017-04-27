package linemap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.google.gson.Gson;

public class MainClass {

	public static void main(String[] args) {

		// List<CoordinatePoint> shape = readCoordinates("C:\\Users\\Aditya\\Desktop\\shapeCoordinates.txt");
		//
		// List<CoordinatePoint> scaledShapeCoordinates = new ArrayList<>();
		// for (int i = 0; i < shape.size(); i++) {
		// scaledShapeCoordinates.add(scaleCoorinatePointToGridScale(shape.get(i)));
		// }
		//
		// writeCsvFileForCoordinate(scaledShapeCoordinates, "C:\\Users\\Aditya\\Desktop\\scaledShapeCoordinates.csv",
		// "X,Y", "\n", ",");
		// writeCsvFileForCrossPoint(getCrossPointsForShapePoints(scaledShapeCoordinates),
		// "C:\\Users\\Aditya\\Desktop\\crossPoints.csv", "X,Y", "\n", ",");

		System.out.println(getGridPointsBetweeenCrossingPoints(new CrossPoint(21.315, 16),new CrossPoint(21.4441, 13)));

	}

	private static List<CrossPoint> getCrossPointsForShapePoints(List<CoordinatePoint> scaledShapePoints) {
		List<CrossPoint> returnList = new ArrayList<>();
		CrossPoint crossPointA;
		CrossPoint crossPointB;
		for (int i = 1; i < scaledShapePoints.size(); i++) {
			CoordinatePoint pointUp;
			CoordinatePoint pointDown;
			boolean reversed = false;

			if (scaledShapePoints.get(i).getX() > scaledShapePoints.get(i - 1).getX()) { // ith
																							// one
																							// is
																							// above
				pointUp = scaledShapePoints.get(i);
				pointDown = scaledShapePoints.get(i - 1);
			} else {
				pointDown = scaledShapePoints.get(i);
				pointUp = scaledShapePoints.get(i - 1);
				reversed = true;
			}
			List<CrossPoint> tempList = new ArrayList<>();
			if (pointUp.getXWholeNumberPart() != pointDown.getXWholeNumberPart()
					&& pointUp.getYWholeNumberPart() != pointDown.getYWholeNumberPart()) {
				// both vertical and horizontal lines are crossed
				crossPointA = new CrossPoint(pointUp.getXWholeNumberPart(), pointUp.getY());
				crossPointB = new CrossPoint(pointDown.getX(),
						pointDown.getYWholeNumberPart() + (pointUp.getY() > pointDown.getY() ? 1 : 0));
				tempList.add(crossPointA);
				tempList.add(crossPointB);
			} else if (pointUp.getXWholeNumberPart() != pointDown.getXWholeNumberPart()) {
				// Only horizontal Line is crossed
				if (pointUp.getXWholeNumberPart() - pointDown.getXWholeNumberPart() == 1) {
					// only one horizontal line is crossed
					crossPointA = new CrossPoint(pointUp.getXWholeNumberPart(), pointUp.getY());
					tempList.add(crossPointA);
				} else {
					// multiple horizontal lines are crossed
					crossPointA = new CrossPoint(pointUp.getXWholeNumberPart(), pointUp.getY());
					crossPointB = new CrossPoint(pointDown.getXWholeNumberPart() + 1, pointDown.getY());
					tempList.add(crossPointA);
					tempList.add(crossPointB);
				}
			} else if (pointUp.getYWholeNumberPart() != pointDown.getYWholeNumberPart()) { // Only Vertical Line is
																							// crossed
				if (pointUp.getYWholeNumberPart() - pointDown.getYWholeNumberPart() == 1) {
					// only one vertical line is crossed
					crossPointA = new CrossPoint(pointUp.getX(),
							pointUp.getYWholeNumberPart() + (pointDown.getY() > pointUp.getY() ? 1 : 0));
					tempList.add(crossPointA);
				} else {
					// multiple vertical lines are crossed
					crossPointA = new CrossPoint(pointUp.getX(),
							pointUp.getYWholeNumberPart() + (pointDown.getY() > pointUp.getY() ? 1 : 0));
					crossPointB = new CrossPoint(pointDown.getX(),
							pointDown.getYWholeNumberPart() + (pointDown.getY() > pointUp.getY() ? 0 : 1));
					tempList.add(crossPointA);
					tempList.add(crossPointB);
				}
			}
			// if (reversed) {
			// Collections.reverse(tempList);
			// }
			returnList.addAll(tempList);
		}
		return returnList;
	}

	private static List<GridPoint> calculatePathPoints(GridPoint pointA, GridPoint pointB) {
		boolean pointsReversed = false;
		GridPoint abovePoint;
		GridPoint belowPoint;
		if (pointA.getX() > pointB.getX()) { // pointA is above pointB
			abovePoint = pointA;
			belowPoint = pointB;
		} else {
			abovePoint = pointB;
			belowPoint = pointA;
			pointsReversed = true;
		}

		List<GridPoint> path = new ArrayList<>();
		GridPoint tempPoint = null; // new Point to be added to the path
		while (!abovePoint.equals(belowPoint)) {
			if (tempPoint != null) {
				abovePoint = tempPoint;
			}
			if (abovePoint.equals(belowPoint))
				break;

			// if pointA and pointB are in same line
			if (abovePoint.getX() == belowPoint.getX() || abovePoint.getY() == belowPoint.getY()) {
				// make straight move
				if (abovePoint.getX() == belowPoint.getX()) {// move along x
																// axis
					if (belowPoint.getY() > abovePoint.getY()) { // move in
																	// right
																	// direction
						tempPoint = new GridPoint(abovePoint.getX(), abovePoint.getY() + 1);
						path.add(tempPoint);
						continue;
					} else { // move in left direction
						tempPoint = new GridPoint(abovePoint.getX(), abovePoint.getY() - 1);
						path.add(tempPoint);
						continue;
					}
				} else { // move along y axis
					tempPoint = new GridPoint(abovePoint.getX() - 1, abovePoint.getY());
					path.add(tempPoint);
					continue;
				}
			} else { // make diagonal move
				if (belowPoint.getY() > abovePoint.getY()) { // move right
					tempPoint = new GridPoint(abovePoint.getX() - 1, abovePoint.getY() + 1);
					path.add(tempPoint);
					continue;
				} else { // move left
					tempPoint = new GridPoint(abovePoint.getX() - 1, abovePoint.getY() - 1);
					path.add(tempPoint);
					continue;
				}
			}
		}
		if (pointsReversed) {
			path.remove(path.size() - 1);
			Collections.reverse(path);
			path.add(pointB);
		}
		return path;
	}

	private static GridPoint mapCoordinateToGrid(CoordinatePoint coordinate) {
		int gridX = (int) Math.round(((((coordinate.getX() - 22.000000) * 100000) - 43519) / 22244) * 100);
		int gridY = (int) Math.round(((((coordinate.getY() - 88.000000) * 100000) - 24170) / 29373) * 100);
		return new GridPoint(gridX, gridY);
	}

	private static CoordinatePoint scaleCoorinatePointToGridScale(CoordinatePoint coordinatePoint) {
		double scaledX = (double) ((((coordinatePoint.getX() - 22.000000) * 100000) - 43519) / 22244) * 100;
		double scaledY = (double) ((((coordinatePoint.getY() - 88.000000) * 100000) - 24170) / 29373) * 100;
		return new CoordinatePoint(scaledX, scaledY);
	}

	private static List<CoordinatePoint> readCoordinates(String fileName) {
		final String FILENAME = fileName;
		List<CoordinatePoint> returnList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				String[] xy = sCurrentLine.split("\t");
				CoordinatePoint coordinatePoint = new CoordinatePoint(Double.valueOf(xy[0]), Double.valueOf(xy[1]));
				returnList.add(coordinatePoint);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnList;
	}

	public static void writeCsvFileForGrid(List<GridPoint> list, String fileName, String FILE_HEADER,
			String NEW_LINE_SEPARATOR, String COMMA_DELIMITER) {

		FileWriter fileWriter = null;

		try {

			fileWriter = new FileWriter(fileName);

			// Write the CSV file header

			fileWriter.append(FILE_HEADER.toString());

			// Add a new line separator after the header

			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new student object list to the CSV file

			for (GridPoint item : list) {

				fileWriter.append(String.valueOf(item.getX()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(item.getY()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
			System.out.println("CSV file was created successfully !!!");
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();

			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
	}

	public static void writeCsvFileForCoordinate(List<CoordinatePoint> list, String fileName, String FILE_HEADER,
			String NEW_LINE_SEPARATOR, String COMMA_DELIMITER) {

		FileWriter fileWriter = null;

		try {

			fileWriter = new FileWriter(fileName);

			// Write the CSV file header

			fileWriter.append(FILE_HEADER.toString());

			// Add a new line separator after the header

			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new student object list to the CSV file

			for (CoordinatePoint item : list) {

				fileWriter.append(String.valueOf(item.getX()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(item.getY()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
			System.out.println("CSV file was created successfully !!!");
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();

			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
	}

	public static void writeCsvFileForCrossPoint(List<CrossPoint> list, String fileName, String FILE_HEADER,
			String NEW_LINE_SEPARATOR, String COMMA_DELIMITER) {

		FileWriter fileWriter = null;

		try {

			fileWriter = new FileWriter(fileName);

			// Write the CSV file header

			fileWriter.append(FILE_HEADER.toString());

			// Add a new line separator after the header

			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new student object list to the CSV file

			for (CrossPoint item : list) {

				fileWriter.append(String.valueOf(item.getX()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(item.getY()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
			System.out.println("CSV file was created successfully !!!");
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();

			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
	}

	public static void writeJSONFile(List<GridPoint> list, String fileName) {

		FileWriter fileWriter = null;

		try {

			fileWriter = new FileWriter(fileName);
			fileWriter.append(new Gson().toJson(list));

			System.out.println("JSON file was created successfully !!!");
		} catch (Exception e) {
			System.out.println("Error in JSONFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();

			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
	}

	public static List<GridPoint> getGridPointsBetweeenCrossingPoints(CrossPoint crossPointA, CrossPoint crossPointB) {
		List<GridPoint> returnList = new ArrayList<>();

		if (crossPointA.getCrossingAxis() != crossPointB.getCrossingAxis()) { // points are on both V and H grid Lines
			GridPoint newGridPoint;
			if (crossPointA.getCrossingAxis() == 'V') { // crossPointA's whole number coordinate is X
				newGridPoint = new GridPoint(crossPointB.getWholeNumberPart(), crossPointA.getWholeNumberPart());
			} else { // crossPointB's whole number coordinate is X
				newGridPoint = new GridPoint(crossPointA.getWholeNumberPart(), crossPointB.getWholeNumberPart());
			}
			returnList.add(newGridPoint);
		} else { // points are on vertically opposite sides
			GridPoint pointA;
			GridPoint pointB;
			if (crossPointA.getCrossingAxis() == 'H') { // points are up - down
				if ((1 - crossPointA.getDecimalNumberPart()) < (1 - crossPointB.getDecimalNumberPart())) {
					pointA = new GridPoint((long) crossPointA.getX(), Math.round(crossPointA.getY()));
					pointB = new GridPoint((long) crossPointB.getX(), pointA.getY());
				} else {
					pointB = new GridPoint((long) crossPointB.getX(), Math.round(crossPointB.getY()));
					pointA = new GridPoint((long) crossPointA.getX(), pointB.getY());
				}

			} else { // points are left - right

				if (crossPointA.getDecimalNumberPart() +crossPointB.getDecimalNumberPart() > 1) {
					pointA = new GridPoint((long)Math.ceil(crossPointA.getX()), (long) crossPointA.getY());
					pointB = new GridPoint((long)Math.ceil(crossPointB.getX()), (long) crossPointB.getY());
				} else {
					pointA = new GridPoint((long)Math.floor(crossPointA.getX()), (long) crossPointA.getY());
					pointB = new GridPoint((long)Math.floor(crossPointB.getX()), (long) crossPointB.getY());
				}
			}
			returnList.add(pointA);
			returnList.add(pointB);
		}
		return returnList;
	}

}
