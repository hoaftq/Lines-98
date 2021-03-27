package thbt.webng.com.game.option;

public enum NextBallDisplayType {
	ShowBoth("Show both"), ShowOnField("Show on field"), ShowOnTop("Show on top"), NotShow("Do not show");

	private String description;

	private NextBallDisplayType(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}
}
