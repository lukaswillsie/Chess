package com.lukaswillsie.chess.engine;

import com.lukaswillsie.chess.logic.Board;
import com.lukaswillsie.chess.logic.Colour;

public interface EvaluationFunction {
    float score(Board board, Colour turnColour);
}
