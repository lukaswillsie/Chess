package com.lukaswillsie.chess.engine;

import com.lukaswillsie.chess.Board;
import com.lukaswillsie.chess.Colour;

import java.util.concurrent.Callable;

public interface EvaluationFunction {
    float score(Board board, Colour turnColour);
}
