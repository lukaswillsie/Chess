package com.lukaswillsie.chess.logic;

public class InvalidPairException extends Exception {
    public InvalidPairException(Pair pair) {
        super("Pair " + pair.toString() + " does not lie in [0,7]x[0,7]");
    }
}
