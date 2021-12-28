package com.lukaswillsie.chess.engine;


import com.lukaswillsie.chess.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * TODO: Use a Builder pattern to construct an engine with modularity
 * TODO: Zero-sum? - could compute eval always from one player's point of view and take the negative
 * Feature Ideas:
 *      Bishop pair
 *      Enemy squares controlled
 *
 */
public class Engine {
    public static EngineOutput minimax(
            Colour turnColour,
            Board board,
            int depth,
            int maxDepth
    ) {
        /*
         * 1. If max depth reached, evaluation function
         * 2. If game is over, return positive/negative infinity. Draw = 0?
         * 3. Otherwise, get all moves and copy board, recurse
         */
        if(board.isCheckmate(Colour.WHITE)) {
            return new EngineOutput(
                    null,
                    null,
                    Evaluation.winEvaluation(Colour.BLACK)
            );
        }
        else if(board.isCheckmate(Colour.BLACK)) {
            return new EngineOutput(
                    null,
                    null,
                    Evaluation.winEvaluation(Colour.WHITE)
            );
        }
        else if(board.isStalemate()) {
            return new EngineOutput(null, null, Evaluation.standardEvaluation(0f));
        }
        else if(depth == maxDepth) {
            return new EngineOutput(null, null, evaluate(board));
        }
        // TODO: If alpha-beta, consider sorting pieces first for better pruning
        List<Pair[]> moves = getTurnPlayerMoves(board, turnColour);

        boolean is_max = turnColour == Colour.WHITE;
        Pair[] bestMove = new Pair[]{null, null};
        Evaluation best = is_max ? Evaluation.winEvaluation(Colour.BLACK) : Evaluation.winEvaluation(Colour.WHITE);
        for(Pair[] move : moves) {
            Board copy = board.copy();
            copy.move(move[0], move[1]);

            EngineOutput output = minimax(turnColour.flip(), copy, depth + 1, maxDepth);
            Evaluation evaluation = output.getEvaluation();

            if (is_max ? evaluation.compareTo(best) >= 0 : evaluation.compareTo(best) <= 0) {
                bestMove = move;
                best = evaluation;
            }
        }

        return new EngineOutput(
                bestMove[0],
                bestMove[1],
                // Need to wrap <best>'s score in a new Evaluation object rather than just returning it
                // directly. For example, if <best> represents a checkmate for white, it will have
                // isWhiteWin=true. We don't want to return that since the current board is NOT a
                // checkmate. Not doing this causes weirdness where the engine can't tell the difference
                // between a move that immediately gives checkmate and a move that still allows
                // checkmate to be forced in the future, causing it to pick one arbitrarily. This causes
                // it to pass up on mate in 1, for example, in favour of mate in 2, which we don't want.
                Evaluation.standardEvaluation(best.getScore())
        );
    }

    public static EngineOutput alphaBeta(
            Colour turnColour,
            Board board,
            int depth,
            int maxDepth,
            Evaluation alpha,
            Evaluation beta
    ) {
        /*
         * 1. If max depth reached, evaluation function
         * 2. If game is over, return positive/negative infinity. Draw = 0?
         * 3. Otherwise, get all moves and copy board, recurse
         */
        if(board.isCheckmate(Colour.WHITE)) {
            return new EngineOutput(
                    null,
                    null,
                    Evaluation.winEvaluation(Colour.BLACK)
            );
        }
        else if(board.isCheckmate(Colour.BLACK)) {
            return new EngineOutput(
                    null,
                    null,
                    Evaluation.winEvaluation(Colour.WHITE)
            );
        }
        else if(board.isStalemate()) {
            return new EngineOutput(null, null, Evaluation.standardEvaluation(0f));
        }
        else if(depth == maxDepth) {
            return new EngineOutput(null, null, evaluate(board));
        }
        // TODO: If alpha-beta, consider sorting pieces first for better pruning
        List<Pair[]> moves = getTurnPlayerMoves(board, turnColour);

        boolean is_max = turnColour == Colour.WHITE;
        Pair[] bestMove = new Pair[]{null, null};
        Evaluation best = is_max ? Evaluation.winEvaluation(Colour.BLACK) : Evaluation.winEvaluation(Colour.WHITE);
        for(Pair[] move : moves) {
            Board copy = board.copy();
            copy.move(move[0], move[1]);

            EngineOutput output = alphaBeta(turnColour.flip(), copy, depth + 1, maxDepth, alpha, beta);
            Evaluation evaluation = output.getEvaluation();

            if (is_max && evaluation.compareTo(best) >= 0) {
                bestMove = move;
                best = evaluation;
                if (evaluation.compareTo(beta) > 0) {
                    break;
                }
                if(evaluation.compareTo(alpha) > 0) {
                    alpha = evaluation;
                }
            }
            else if (!is_max && evaluation.compareTo(best) <= 0) {
                bestMove = move;
                best = evaluation;
                if (evaluation.compareTo(alpha) < 0) {
                    break;
                }
                if(evaluation.compareTo(beta) < 0) {
                    beta = evaluation;
                }
            }
        }

//        System.out.println("=======================================");
//        System.out.println("White Checkmate: " + wCheckmate);
//        System.out.println("Black Checkmate: " + bCheckmate);
//        System.out.println("Stalemate: " + stalemate);
//        System.out.println("Get Player Moves: " + getPlayerMoves);
//        System.out.println("Copying: " + copying);
//        System.out.println("=======================================");

        return new EngineOutput(
                bestMove[0],
                bestMove[1],
                // Need to wrap <best>'s score in a new Evaluation object rather than just returning it
                // directly. For example, if <best> represents a checkmate for white, it will have
                // isWhiteWin=true. We don't want to return that since the current board is NOT a
                // checkmate. Not doing this causes weirdness where the engine can't tell the difference
                // between a move that immediately gives checkmate and a move that still allows
                // checkmate to be forced in the future, causing it to pick one arbitrarily. This causes
                // it to pass up on mate in 1, for example, in favour of mate in 2, which we don't want.
                Evaluation.standardEvaluation(best.getScore())
        );
    }

    private static Evaluation evaluate(Board board) {
        int whiteMaterial = 0, blackMaterial = 0;
        for(Piece[] row : board) {
            for(Piece piece : row) {
                if(piece != null) {
                    if (piece.getColour() == Colour.WHITE) {
                        whiteMaterial += getMaterial(piece);
                    } else {
                        blackMaterial += getMaterial(piece);
                    }
                }
            }
        }
        return Evaluation.standardEvaluation(((float) whiteMaterial) / blackMaterial);
    }

    private static List<Pair[]> getTurnPlayerMoves(Board board, Colour turnColour) {
        List<Pair[]> moves = new ArrayList<>();
        for(Piece[] row : board) {
            for(Piece piece : row) {
                if(piece == null || piece.getColour() != turnColour) {
                    continue;
                }

                Pair square = piece.getSquare();
                for(Pair dest : piece.getMoves()) {
                    moves.add(new Pair[]{square, dest});
                }
            }
        }
        return moves;
    }

    private static int getMaterial(Piece piece) {
        if(piece instanceof Pawn || piece instanceof King) {
            return 1;
        }
        else if (piece instanceof Bishop || piece instanceof Knight) {
            return 3;
        }
        else if (piece instanceof Rook) {
            return 5;
        }
        else if (piece instanceof Queen) {
            return 9;
        }
        return 0;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Board testBoard = new Board();
        testBoard.initialize(new Scanner(new File("com/lukaswillsie/chess/new_board.txt")));
        Evaluation alpha = Evaluation.winEvaluation(Colour.BLACK);
        Evaluation beta = Evaluation.winEvaluation(Colour.WHITE);
        alphaBeta(Colour.WHITE, testBoard, 0, 2, alpha, beta);
        timeTest(3);
    }

    private static void timeTest(int maxDepth) throws FileNotFoundException {
        Board testBoard = new Board();
        testBoard.initialize(new Scanner(new File("com/lukaswillsie/chess/new_board.txt")));
        long[] baseTimes = new long[5];
        long[] alphaBetaTimes = new long[5];
        long start, end;
        Evaluation alpha = Evaluation.winEvaluation(Colour.BLACK);
        Evaluation beta = Evaluation.winEvaluation(Colour.WHITE);
        for(int i = 0; i < maxDepth; i++) {
            start = System.currentTimeMillis();
            minimax(Colour.WHITE, testBoard, 0, i+1);
            end = System.currentTimeMillis();
            baseTimes[i] = end - start;

            start = System.currentTimeMillis();
            alphaBeta(Colour.WHITE, testBoard, 0, i+1, alpha, beta);
            end = System.currentTimeMillis();
            alphaBetaTimes[i] = end - start;

            System.out.println("====" + (i + 1) + "====");
            System.out.println("\tMinimax: " + baseTimes[i]);
            System.out.println("\tAlphaBeta: " + alphaBetaTimes[i]);
        }
    }
}


//    public static EngineOutput alphaBeta(
//            Colour turnColour,
//            Board board,
//            int depth,
//            int maxDepth,
//            Evaluation alpha,
//            Evaluation beta
//    ) {
//        /*
//         * 1. If max depth reached, evaluation function
//         * 2. If game is over, return positive/negative infinity. Draw = 0?
//         * 3. Otherwise, get all moves and copy board, recurse
//         */
//        long start, end;
//        long wCheckmate, bCheckmate, stalemate, getPlayerMoves, copying = 0;
//
//        start = System.nanoTime();
//        boolean whiteCheckmate = board.isCheckmate(Colour.WHITE);
//        end = System.nanoTime();
//        wCheckmate = end - start;
//
//        start = System.nanoTime();
//        boolean blackCheckmate = board.isCheckmate(Colour.BLACK);
//        end = System.nanoTime();
//        bCheckmate = end - start;
//
//        start = System.nanoTime();
//        boolean isStalemate = board.isStalemate();
//        end = System.nanoTime();
//        stalemate = end - start;
//
//        if(whiteCheckmate) {
//            return new EngineOutput(
//                    null,
//                    null,
//                    Evaluation.winEvaluation(Colour.BLACK)
//            );
//        }
//        else if(blackCheckmate) {
//            return new EngineOutput(
//                    null,
//                    null,
//                    Evaluation.winEvaluation(Colour.WHITE)
//            );
//        }
//        else if(board.isStalemate()) {
//            return new EngineOutput(null, null, Evaluation.standardEvaluation(0f));
//        }
//        else if(depth == maxDepth) {
//            return new EngineOutput(null, null, evaluate(board));
//        }
//        // TODO: If alpha-beta, consider sorting pieces first for better pruning
//        start = System.nanoTime();
//        List<Pair[]> moves = getTurnPlayerMoves(board, turnColour);
//        end = System.nanoTime();
//        getPlayerMoves = end - start;
//
//        boolean is_max = turnColour == Colour.WHITE;
//        Pair[] bestMove = new Pair[]{null, null};
//        Evaluation best = is_max ? Evaluation.winEvaluation(Colour.BLACK) : Evaluation.winEvaluation(Colour.WHITE);
//        for(Pair[] move : moves) {
//            start = System.nanoTime();
//            Board copy = board.copy();
//            copy.move(move[0], move[1]);
//            end = System.nanoTime();
//            copying = end - start;
//
//            EngineOutput output = alphaBeta(turnColour.flip(), copy, depth + 1, maxDepth, alpha, beta);
//            Evaluation evaluation = output.getEvaluation();
//
//            if (is_max && evaluation.compareTo(best) >= 0) {
//                bestMove = move;
//                best = evaluation;
//                if (evaluation.compareTo(beta) > 0) {
//                    break;
//                }
//                if(evaluation.compareTo(alpha) > 0) {
//                    alpha = evaluation;
//                }
//            }
//            else if (!is_max && evaluation.compareTo(best) <= 0) {
//                bestMove = move;
//                best = evaluation;
//                if (evaluation.compareTo(alpha) < 0) {
//                    break;
//                }
//                if(evaluation.compareTo(beta) < 0) {
//                    beta = evaluation;
//                }
//            }
//        }
//
////        System.out.println("=======================================");
////        System.out.println("White Checkmate: " + wCheckmate);
////        System.out.println("Black Checkmate: " + bCheckmate);
////        System.out.println("Stalemate: " + stalemate);
////        System.out.println("Get Player Moves: " + getPlayerMoves);
////        System.out.println("Copying: " + copying);
////        System.out.println("=======================================");
//
//        return new EngineOutput(
//                bestMove[0],
//                bestMove[1],
//                // Need to wrap <best>'s score in a new Evaluation object rather than just returning it
//                // directly. For example, if <best> represents a checkmate for white, it will have
//                // isWhiteWin=true. We don't want to return that since the current board is NOT a
//                // checkmate. Not doing this causes weirdness where the engine can't tell the difference
//                // between a move that immediately gives checkmate and a move that still allows
//                // checkmate to be forced in the future, causing it to pick one arbitrarily. This causes
//                // it to pass up on mate in 1, for example, in favour of mate in 2, which we don't want.
//                Evaluation.standardEvaluation(best.getScore())
//        );
//    }
