package linemap;


public class GridPoint {
	private long x;
	private long y;

	public GridPoint(long x, long y) {
		super();
		this.x = x;
		this.y = y;
	}

	public long getX() {
		return x;
	}

	public void setX(long x) {
		this.x = x;
	}

	public long getY() {
		return y;
	}

	public void setY(long y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
	        return false;
	    }
	    if (!GridPoint.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    final GridPoint other = (GridPoint) obj;
	    if (this.x == other.getX() && this.y == other.getY()){
	    	return true;
	    }
	    return false;
	}
	@Override
	public String toString() {
		return "Point: " + "X:" + x + " Y:" + y;
	}
}
