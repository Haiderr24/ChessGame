package main;

import pieces.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Input extends MouseAdapter {

    Board board;
    private boolean isWhiteTurn = true;

    public Input(Board board) {
        this.board = board;
    }

    public void switchTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int col = getAdjustedCol(e.getX());
        int row = getAdjustedRow(e.getY());

        Piece pieceXY = board.getPiece(col, row);
        if (pieceXY != null && pieceXY.isWhite == isWhiteTurn) {
            board.selectedPiece = pieceXY;
            pieceXY.isBeingDragged = true; // Start dragging
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.selectedPiece != null) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            int tileSize = board.tileSize;

            int adjustedX, adjustedY;

            if (board.isBoardFlipped) {
                // Flip the mouse coordinates by subtracting from board dimensions
                // Then adjust to keep the piece under the mouse cursor
                adjustedX = board.getWidth() - mouseX - tileSize / 2;
                adjustedY = board.getHeight() - mouseY - tileSize / 2;
            } else {
                // Keep the piece under the mouse cursor for an unflipped board
                adjustedX = mouseX - tileSize / 2;
                adjustedY = mouseY - tileSize / 2;
            }

            // Set the piece's position
            board.selectedPiece.xPos = adjustedX;
            board.selectedPiece.yPos = adjustedY;

            // Repaint the board to reflect the new position
            board.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int col = getAdjustedCol(e.getX());
        int row = getAdjustedRow(e.getY());

        if (board.selectedPiece != null) {
            // Stop dragging the piece
            board.selectedPiece.isBeingDragged = false;

            Move move = new Move(board, board.selectedPiece, col, row);

            if (board.isValidMove(move)) {
                board.makeMove(move);
            } else {
                // If the move isn't valid, reset the piece's position
                board.selectedPiece.xPos = board.selectedPiece.col * board.tileSize;
                board.selectedPiece.yPos = board.selectedPiece.row * board.tileSize;
            }

            // Clear the selected piece
            board.selectedPiece = null;
            // Refresh the board
            board.repaint();


        }
    }

    private int getAdjustedCol(int x) {
        return board.isBoardFlipped ? board.cols - 1 - (x / board.tileSize) : x / board.tileSize;
    }

    private int getAdjustedRow(int y) {
        return board.isBoardFlipped ? board.rows - 1 - (y / board.tileSize) : y / board.tileSize;
    }


}
