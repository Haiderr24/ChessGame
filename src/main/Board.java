package main;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel {
    public int tileSize = 85;
    int cols = 8;
    int rows = 8;

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece; //will be the piece that is moving

    Input input = new Input(this);

    public CheckScanner checkScanner = new CheckScanner(this);

    public int enPassantTile = -1;
    boolean isBoardFlipped = false;

    public Board() {
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));

        input = new Input(this);  // Instantiate the input object first

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        addPieces();
    }

    public void toggleBoardFlip() {
        isBoardFlipped = !isBoardFlipped;
        repaint();
    }



    public Piece getPiece(int col, int row) {

        //if piece in the pieceList has the same col and row, return it
        for (Piece piece : pieceList) {
            if (piece.col == col && piece.row == row) {
                return piece;
            }
        }

        return null; //if nothing is found

    }

    public void makeMove(Move move) {

        if (move.piece.name.equals("Pawn")) {
            movePawn(move);
        } else if (move.piece.name.equals("King")) {
            moveKing(move);
        }
                move.piece.col = move.newCol;
                move.piece.row = move.newRow;
                move.piece.xPos = move.newCol * tileSize;
                move.piece.yPos = move.newRow * tileSize;

                move.piece.isFirstMove = false;

                capture(move.capture);
                input.switchTurn();

        if (checkScanner.isCheckmate(!move.piece.isWhite)) {
            // The opponent is in checkmate. End the game.
            System.out.println((move.piece.isWhite ? "Black" : "White") + " is in checkmate!");
        }

        isBoardFlipped = !isBoardFlipped;
        repaint();

    }

    private void moveKing(Move move) {

        if (Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rook ;
            if (move.piece.col < move.newCol) {
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            } else {
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * tileSize;
        }

    }

    private void movePawn(Move move) {

        //en Passant
        int colorIndex = move.piece.isWhite ? 1 : -1;

        if (getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }
        if (Math.abs(move.piece.row - move.newRow) == 2) {
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1;
        }

        //promotions
        colorIndex = move.piece.isWhite ? 0 : 7;
        if (move.newRow == colorIndex) {
            promotePawn(move);
        }

    }

    //makes pawn queen, need to add other options
    private void promotePawn(Move move) {
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }

    public void capture(Piece piece) {
        pieceList.remove(piece);
    }


    public boolean isValidMove(Move move) {

        if (move.newCol < 0 || move.newCol >= cols || move.newRow < 0 || move.newRow >= rows) {
            return false;
        }

        if (sameTeam(move.piece, move.capture)) {
            return false;
        }
        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }
        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }
        if (checkScanner.isKingChecked(move)) {
            return false;
        }

        return true;
    }

    public boolean sameTeam(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    public int getTileNum(int col, int row) {
        return row * rows + col;
    }

    Piece findKing(boolean isWhite) {
        for (Piece piece : pieceList) {
            if (isWhite == piece.isWhite && piece.name.equals("King")) {
                return piece;
            }
        }
        return null;
    }


    public void addPieces() {
        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new King(this, 4, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));

        pieceList.add(new Pawn(this, 0, 1, false));
        pieceList.add(new Pawn(this, 1, 1, false));
        pieceList.add(new Pawn(this, 2, 1, false));
        pieceList.add(new Pawn(this, 3, 1, false));
        pieceList.add(new Pawn(this, 4, 1, false));
        pieceList.add(new Pawn(this, 5, 1, false));
        pieceList.add(new Pawn(this, 6, 1, false));
        pieceList.add(new Pawn(this, 7, 1, false));



        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Queen(this, 3, 7, true));
        pieceList.add(new King(this, 4, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));

        pieceList.add(new Pawn(this, 0, 6, true));
        pieceList.add(new Pawn(this, 1, 6, true));
        pieceList.add(new Pawn(this, 2, 6, true));
        pieceList.add(new Pawn(this, 3, 6, true));
        pieceList.add(new Pawn(this, 4, 6, true));
        pieceList.add(new Pawn(this, 5, 6, true));
        pieceList.add(new Pawn(this, 6, 6, true));
        pieceList.add(new Pawn(this, 7, 6, true));
    }


    @Override
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Paint board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int drawRow = isBoardFlipped ? 7 - r : r;
                int drawCol = isBoardFlipped ? 7 - c : c;
                g2d.setColor((c + r) % 2 == 0 ? new Color(233, 237, 204) : new Color(95, 126, 70));
                g2d.fillRect(drawCol * tileSize, drawRow * tileSize, tileSize, tileSize);
            }
        }

        int circleRadius = tileSize / 8;
        if (selectedPiece != null) {
            for (int r = 0; r < cols; r++) {
                for (int c = 0; c < rows; c++) {
                    int adjustedC = isBoardFlipped ? 7 - c : c;
                    int adjustedR = isBoardFlipped ? 7 - r : r;
                    if (isValidMove(new Move(this, selectedPiece, adjustedC, adjustedR))) {
                        int x = c * tileSize + tileSize / 2 - circleRadius;
                        int y = r * tileSize + tileSize / 2 - circleRadius;
                        int diameter = 2 * circleRadius;

                        Piece pieceAtSquare = getPiece(adjustedC, adjustedR);

                        if (pieceAtSquare != null && pieceAtSquare.isEnemy(selectedPiece)) {
                            g2d.setColor(new Color(220, 220, 101, 140));
                            g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
                        } else {
                            g2d.setColor(new Color(169, 169, 169, 128)); // Gray with 50% opacity
                            g2d.fillOval(x, y, diameter, diameter);
                        }
                    }
                }
            }
        }


        for (Piece piece : pieceList) {
            if (piece.isBeingDragged) {
                int xRender, yRender;
                if (isBoardFlipped) {
                    // Adjust the coordinates for the flipped board
                    xRender = getWidth() - piece.xPos - tileSize;
                    yRender = getHeight() - piece.yPos - tileSize;
                } else {
                    // Keep the piece under the cursor for an un-flipped board
                    xRender = piece.xPos;
                    yRender = piece.yPos;
                }
                // Paint the piece at the adjusted cursor position
                piece.paint(g2d, xRender, yRender);
            } else {
                // Calculate the position based on whether the board is flipped
                int drawX = isBoardFlipped ? 7 - piece.col : piece.col;
                int drawY = isBoardFlipped ? 7 - piece.row : piece.row;
                // Paint the piece at its board position
                piece.paint(g2d, drawX * tileSize, drawY * tileSize);
            }
        }


    }

}