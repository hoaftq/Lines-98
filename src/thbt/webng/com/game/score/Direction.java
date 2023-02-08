package thbt.webng.com.game.score;

class Direction {

	public static final Direction TO_RIGHT = new Direction(1, 0);
	public static final Direction TO_LEFT = TO_RIGHT.opposite();

	public static final Direction TO_BOTTOM = new Direction(0, 1);
	public static final Direction TO_TOP = TO_BOTTOM.opposite();

	public static final Direction TO_TOP_RIGHT = new Direction(1, -1);
	public static final Direction TO_BOTTOM_LEFT = TO_TOP_RIGHT.opposite();

	public static final Direction TO_BOTTOM_RIGHT = new Direction(1, 1);
	public static final Direction TO_TOP_LEFT = TO_BOTTOM_RIGHT.opposite();

	private int x;
	private int y;

	private Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Direction opposite() {
		return new Direction(-x, -y);
	}

}
