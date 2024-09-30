package pieces;

import main.Board;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Piece {

    //each piece needs columns,rows, x and y position
    public int col, row;
    public int xPos, yPos;

    //piece team is on
    public boolean isWhite;
    public String name;
    //public int value;

    public boolean isFirstMove = true;
    public boolean isBeingDragged = false;

    //sheet is the pieces.png image
    BufferedImage sheet;
    {
        try {
            sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("res/pieces.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected int sheetScale = sheet.getWidth()/6;

    //taking section of sheet and assigning
    Image sprite;

    Board board;
    public Piece(Board board) {
        this.board = board;

    }


    public boolean isEnemy(Piece otherPiece) {
        return this.isWhite != otherPiece.isWhite;
    }

    public boolean isValidMovement(int col, int row) {
        return true;
    }

    public boolean moveCollidesWithPiece(int col, int row) {
        return false;
    }



    public void paint(Graphics2D g2d, int xPos, int yPos) {
        g2d.drawImage(sprite, xPos, yPos, null);
    }

}
