package com.lukaswillsie.chess.engine;

import com.lukaswillsie.chess.logic.Pair;

public class EngineOutput {
    public Evaluation getEvaluation() {
        return evaluation;
    }

    public EngineOutput(Pair srcSquare, Pair destSquare, Evaluation evaluation) {
        this.srcSquare = srcSquare;
        this.destSquare = destSquare;
        this.evaluation = evaluation;
    }

    public Pair getSrcSquare() {
        return srcSquare;
    }

    public Pair getDestSquare() {
        return destSquare;
    }

    public void setSrcSquare(Pair srcSquare) {
        this.srcSquare = srcSquare;
    }

    public void setDestSquare(Pair destSquare) {
        this.destSquare = destSquare;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    private Pair srcSquare;
    private Pair destSquare;
    private Evaluation evaluation;
}
