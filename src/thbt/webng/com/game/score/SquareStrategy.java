package thbt.webng.com.game.score;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;

public class SquareStrategy extends ScoreStrategy {

	public SquareStrategy(Square[][] squareArray) {
		super(squareArray);
	}

	@Override
	public List<Square> getCompleteArea(Position pos) {
		List<Square> listCompleteSquare = new ArrayList<Square>();

		int row = squareArray.length;
		int col = squareArray[0].length;
		Color color = squareArray[pos.x][pos.y].getBall().getColor();
		boolean b1 = false;
		boolean b2 = false;
		boolean b3 = false;

		if (pos.x > 0 && pos.y > 0) {
			if (squareArray[pos.x][pos.y - 1].isEnableDestroy(color)
					&& squareArray[pos.x - 1][pos.y - 1].isEnableDestroy(color)
					&& squareArray[pos.x - 1][pos.y].isEnableDestroy(color)

			) {
				listCompleteSquare.add(squareArray[pos.x][pos.y - 1]);
				listCompleteSquare.add(squareArray[pos.x - 1][pos.y - 1]);
				listCompleteSquare.add(squareArray[pos.x - 1][pos.y]);
				b1 = true;
			}
		}

		if (pos.x > 0 && pos.y < row - 1) {
			if (squareArray[pos.x - 1][pos.y].isEnableDestroy(color)
					&& squareArray[pos.x - 1][pos.y + 1].isEnableDestroy(color)
					&& squareArray[pos.x][pos.y + 1].isEnableDestroy(color)

			) {
				if (!b1) {
					listCompleteSquare.add(squareArray[pos.x - 1][pos.y]);
				}
				listCompleteSquare.add(squareArray[pos.x - 1][pos.y + 1]);
				listCompleteSquare.add(squareArray[pos.x][pos.y + 1]);
				b2 = true;
			}
		}

		if (pos.x < col - 1 && pos.y < row - 1) {
			if (squareArray[pos.x][pos.y + 1].isEnableDestroy(color)
					&& squareArray[pos.x + 1][pos.y + 1].isEnableDestroy(color)
					&& squareArray[pos.x + 1][pos.y].isEnableDestroy(color)

			) {
				if (!b2) {
					listCompleteSquare.add(squareArray[pos.x][pos.y + 1]);
				}
				listCompleteSquare.add(squareArray[pos.x + 1][pos.y + 1]);
				listCompleteSquare.add(squareArray[pos.x + 1][pos.y]);
				b3 = true;
			}
		}

		if (pos.x < col - 1 && pos.y > 0) {
			if (squareArray[pos.x + 1][pos.y].isEnableDestroy(color)
					&& squareArray[pos.x + 1][pos.y - 1].isEnableDestroy(color)
					&& squareArray[pos.x][pos.y - 1].isEnableDestroy(color)

			) {
				if (!b3) {
					listCompleteSquare.add(squareArray[pos.x + 1][pos.y]);
				}
				listCompleteSquare.add(squareArray[pos.x + 1][pos.y - 1]);

				if (!b1) {
					listCompleteSquare.add(squareArray[pos.x][pos.y - 1]);
				}
			}
		}

		if (listCompleteSquare.size() > 0) {
			listCompleteSquare.add(squareArray[pos.x][pos.y]);
		}

		return listCompleteSquare;
	}
}
