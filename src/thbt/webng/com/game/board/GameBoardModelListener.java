package thbt.webng.com.game.board;

import java.awt.*;

public interface GameBoardModelListener {

    void onModelChanged();

    int getScore();

    int getSpentTime();

    void setScore(int score);

    void setSpentTime(int timeInSeconds);

    void generateNextColors();

    void setNextColors(Color[] nextColors);

    Color[] getNextColors();
}
