package com.lukaswillsie.chess;

import com.lukaswillsie.chess.logic.Colour;
import com.lukaswillsie.chess.logic.InvalidPairException;
import com.lukaswillsie.chess.logic.Pair;
import com.lukaswillsie.chess.logic.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Is used to memoize information that has been computed regarding a game of chess,
 * to prevent copious re-computation of information when computing things like all
 * of the moves available to the turn player.
 */
public class GameData {
    // TODO: Immutable list?
    private final Game game;

    public PairContainer[][] getMoves() {
        return moves;
    }

    public PairContainer[][] getBlockingSquares() {
        return blockingSquares;
    }

    public PairContainer[][] getProtectedSquares() {
        return protectedSquares;
    }

    private final PairContainer[][] moves = new PairContainer[8][8];
    private final PairContainer[][] blockingSquares = new PairContainer[8][8];
    private final PairContainer[][] protectedSquares = new PairContainer[8][8];

    public GameData(Game game) {
        this.game = game;
    }

    public void computePlayerMoves(Colour player) {
        for(int x = 0; x < moves.length; x++) {
            for(int y = 0; y < moves[0].length; y++) {
                Piece piece = game.getPiece(x, y);
                if(piece == null || piece.getColour() != player) {
                    continue;
                }
                computeMoves(new Pair(x, y));
            }
        }
    }

    public List<Pair> getMoves(Pair target) throws InvalidPairException {
        if (!game.isValidSquare(target)) {
            throw new InvalidPairException(target);
        }
        if (moves[target.first][target.second] == null) {
            computeMoves(target);
        }
        return moves[target.first][target.second].getPairs();
    }

    private void computeMoves(Pair target) {
        Piece piece = game.getPiece(target);
        if (piece == null) {
            moves[target.first][target.second] = new PairContainer(new ArrayList<>());
        } else {
            List<Pair> pieceMoves = piece.getMoves();
            moves[target.first][target.second] = new PairContainer(pieceMoves);
        }
    }

    public List<Pair> getProtectedSquares(Pair target) throws InvalidPairException {
        if (!game.isValidSquare(target)) {
            throw new InvalidPairException(target);
        }
        if (protectedSquares[target.first][target.second] == null) {
            computeProtectedSquares(target);
        }
        return protectedSquares[target.first][target.second].getPairs();
    }

    private void computeProtectedSquares(Pair target) {
        Piece piece = game.getPiece(target);
        if (piece == null) {
            blockingSquares[target.first][target.second] = new PairContainer(new ArrayList<>());
        } else {
            List<Pair> pieceMoves = piece.getProtectedSquares();
            blockingSquares[target.first][target.second] = new PairContainer(pieceMoves);
        }
    }

    private static class PairContainer {
        public PairContainer(List<Pair> moves) {
            this.pairs = moves;
        }

        public List<Pair> getPairs() {
            return pairs;
        }

        List<Pair> pairs;
    }
}
