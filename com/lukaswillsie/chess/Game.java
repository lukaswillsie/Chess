package com.lukaswillsie.chess;

import com.lukaswillsie.chess.logic.Board;
import com.lukaswillsie.chess.logic.Pair;
import com.lukaswillsie.chess.logic.Piece;

/**
 * Represents a game of chess at a moment in time, with an associated board, state,
 * and data.
 */
public class Game {
    public Board getBoard() {
        return board;
    }

    private GameState state;
    private Board board;

    public GameState getState() {
        return state;
    }

    private GameData data;

    public boolean isValidSquare(int row, int column) {
        return 0 <= row && row <= 7 && 0 <= column && column <= 7;
    }

    public boolean isValidSquare(Pair p) {
        return isValidSquare(p.first(), p.second());
    }

    public Piece getPiece(int row, int column) {
        return board.getPiece(row, column);
    }

    public Piece getPiece(Pair pair) {
        return board.getPiece(pair);
    }
}
