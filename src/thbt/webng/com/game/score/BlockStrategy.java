package thbt.webng.com.game.score;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;

public class BlockStrategy extends ScoreStrategy {

	private boolean[][] visitedArray;

	public BlockStrategy(Square[][] squareArray) {
		super(squareArray);
		visitedArray = new boolean[squareArray.length][squareArray[0].length];
	}

	@Override
	public List<Square> getCompleteArea(Position pos) {
		resetVisitedArray();

		List<Square> listCompleteSquare = getCompleteBlock(pos, squareArray[pos.x][pos.y].getBall().getColor());

		if (listCompleteSquare.size() >= 7) {
			return listCompleteSquare;
		}

		return new ArrayList<Square>();
	}

	private List<Square> getCompleteBlock(Position pos, Color color) {
		var row = squareArray.length;
		var col = squareArray[0].length;

		List<Square> listSquare = new ArrayList<Square>();

		if (!visitedArray[pos.x][pos.y]) {
			visitedArray[pos.x][pos.y] = true;

			Square square = squareArray[pos.x][pos.y];
			if (square.isEnableDestroy(color)) {
				listSquare.add(square);

				if (pos.y > 0) {
					listSquare.addAll(getCompleteBlock(new Position(pos.x, pos.y - 1), color));
				}

				if (pos.x > 0) {
					listSquare.addAll(getCompleteBlock(new Position(pos.x - 1, pos.y), color));
				}

				if (pos.y < row - 1) {
					listSquare.addAll(getCompleteBlock(new Position(pos.x, pos.y + 1), color));
				}

				if (pos.x < col - 1) {
					listSquare.addAll(getCompleteBlock(new Position(pos.x + 1, pos.y), color));
				}
			}
		}

		return listSquare;
	}

	private void resetVisitedArray() {
		for (int i = 0; i < visitedArray.length; i++) {
			for (int j = 0; j < visitedArray[0].length; j++) {
				visitedArray[i][j] = false;
			}
		}
	}

}
