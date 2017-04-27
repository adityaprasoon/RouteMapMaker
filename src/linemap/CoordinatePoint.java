package linemap;

public class CoordinatePoint {
	private double x;
	private double y;

	public CoordinatePoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public long getXWholeNumberPart() {
		return (long) x;
	}

	public long getYWholeNumberPart() {
		return (long) y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!CoordinatePoint.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		final CoordinatePoint other = (CoordinatePoint) obj;
		if (this.x == other.getX() && this.y == other.getY()) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Point: " + "X:" + x + " Y:" + y;
	}
}
