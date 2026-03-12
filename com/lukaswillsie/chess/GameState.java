package com.lukaswillsie.chess;

import com.lukaswillsie.chess.logic.Colour;
import com.lukaswillsie.chess.logic.Pair;
import com.lukaswillsie.chess.logic.Pawn;

/**
 * Represents the state of a game of chess. Stores things like whose turn it is,
 * whether each side is able to castle, whether there is an en passant square, etc.
 * These are values that cannot just be deduced from looking at the board, and so are
 * persisted here.
 */
public class GameState {
    // Keeps track of what colour's turn it is
    private Colour turn;

    // A reference to a pawn that needs to be promoted, if one exists. Is null otherwise.
    private Pawn needsToBePromoted;

    // Keeps track of whether or not there's a square that can be moved
    // to as part of an "en passant" move
    private Pair enPassant;

    // Booleans that partially track whether or not white/black has the ability to castle.
    // Specifically, <colour>CanQueensideCastle means that <colour>'s King hasn't
    // moved this game, and neither has <colour>'s Queenside Rook. Similarly for
    // <colour>CanKingsideCastle.
    // Note that there are other conditions that must be met by the board for
    // a given colour to actually be able to castle; the King can't be in check,
    // all of the squares that the King moves through must be empty, etc. These
    // booleans just store the prerequisites regarding whether the relevant pieces
    // have moved. The rest of the computation should be done elsewhere.
    private boolean whiteCanQueensideCastle;
    private boolean whiteCanKingsideCastle;
    private boolean blackCanQueensideCastle;
    private boolean blackCanKingsideCastle;

    public GameState(Colour turn, Pawn needsToBePromoted, Pair enPassant, boolean whiteCanQueensideCastle, boolean whiteCanKingsideCastle, boolean blackCanQueensideCastle, boolean blackCanKingsideCastle) {
        this.turn = turn;
        this.needsToBePromoted = needsToBePromoted;
        this.enPassant = enPassant;
        this.whiteCanQueensideCastle = whiteCanQueensideCastle;
        this.whiteCanKingsideCastle = whiteCanKingsideCastle;
        this.blackCanQueensideCastle = blackCanQueensideCastle;
        this.blackCanKingsideCastle = blackCanKingsideCastle;
    }
}
