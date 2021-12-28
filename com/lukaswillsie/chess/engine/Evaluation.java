package com.lukaswillsie.chess.engine;

import com.lukaswillsie.chess.Colour;

public class Evaluation implements Comparable<Evaluation> {
    private final float score;
    private final boolean isWhiteWin;
    private final boolean isBlackWin;

    public static Evaluation winEvaluation(Colour colour) {
        float score = colour == Colour.WHITE ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        return new Evaluation(
            score,
            colour == Colour.WHITE,
            colour == Colour.BLACK
        );
    }

    public static Evaluation standardEvaluation(float score) {
        return new Evaluation(score, false, false);
    }

    private Evaluation(float score, boolean isWhiteWin, boolean isBlackWin) {
        this.score = score;
        this.isWhiteWin = isWhiteWin;
        this.isBlackWin = isBlackWin;
    }

    public float getScore() {
        return score;
    }

    public boolean isWhiteWin() {
        return isWhiteWin;
    }

    @Override
    public int compareTo(Evaluation o) {
        if (isWhiteWin) {
            if (o.isWhiteWin) {
                return 0;
            }
            else {
                return 1;
            }
        }
        else if (isBlackWin) {
            if (o.isBlackWin) {
                return 0;
            }
            else {
                return -1;
            }
        }
        else {
            if (o.isWhiteWin) {
                return -1;
            }
            else if (o.isBlackWin) {
                return 1;
            }
            else {
                if (score == o.score) {
                    return 0;
                }
                return score < o.score ? -1 : 1;
            }
        }
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "score=" + score +
                ", isWhiteWin=" + isWhiteWin +
                ", isBlackWin=" + isBlackWin +
                '}';
    }
}
