package thbt.webng.com.game.board;

import java.awt.*;

public interface GameBoardViewListener {

    void drawGameInfo(Graphics g);


    void onPlayAt(int mouseX, int mouseY);
}
