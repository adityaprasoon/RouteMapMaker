package linemap;

public class CrossPoint {
	private double x;
	private double y;

	public CrossPoint(double x, double y) {
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

	public char getCrossingAxis() {
		if (x % 1 == 0 && y % 1 == 0) {
			return 'B'; // both
		} else if (x % 1 == 0) {
			return 'H'; // horizontal
		} else if (y % 1 == 0) {
			return 'V'; // vertical
		} else {
			return 'N';
		}
	}

	public long getWholeNumberPart() {
		if (x % 1 == 0) {
			return (long) x;
		} else if (y % 1 == 0) {
			return (long) y;
		} else {
			return -1;
		}
	}

	public double getDecimalNumberPart() {
		if (x % 1 != 0) {
			return x % 1;
		} else if (y % 1 != 0) {
			return y % 1;
		} else {
			return -1;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!CrossPoint.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		final CrossPoint other = (CrossPoint) obj;
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
