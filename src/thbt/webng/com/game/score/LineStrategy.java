package thbt.webng.com.game.score;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;

public class LineStrategy extends ScoreStrategy{

	public LineStrategy(Square[][] squareArray) {
		super(squareArray);
	}

	@Override
	public List<Square> getCompleteArea(Position pos) {
		List<Square> listCompleteSquare = new ArrayList<Square>();
		Color color = squareArray[pos.x][pos.y].getBall().getColor();

		int row = squareArray.length;
		int col = squareArray[0].length;
		Square square;
		int i, j;

		var listTempSquare = new ArrayList<Square>();
		j = 1;
		while (pos.y + j < col && (square = squareArray[pos.x][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			j++;
		}
		j = -1;
		while (pos.y + j >= 0 && (square = squareArray[pos.x][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			j--;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		listTempSquare = new ArrayList<Square>();
		i = 1;
		while (pos.x + i < col && (square = squareArray[pos.x + i][pos.y]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i++;
		}
		i = -1;
		while (pos.x + i >= 0 && (square = squareArray[pos.x + i][pos.y]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i--;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		listTempSquare = new ArrayList<Square>();
		i = 1;
		j = 1;
		while (pos.x + i < col && pos.y + j < row
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i++;
			j++;
		}
		i = -1;
		j = -1;
		while (pos.x + i >= 0 && pos.y + j >= 0
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i--;
			j--;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		listTempSquare = new ArrayList<Square>();
		i = 1;
		j = -1;
		while (pos.x + i < col && pos.y + j >= 0
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i++;
			j--;
		}
		i = -1;
		j = 1;
		while (pos.x + i >= 0 && pos.y + j < row
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i--;
			j++;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		if (listCompleteSquare.size() > 0) {
			listCompleteSquare.add(squareArray[pos.x][pos.y]);
		}

		return listCompleteSquare;
	}
}
