package game;

import java.util.ArrayList;
import java.util.List;

import utility.Pair;

public class Knight extends Piece {
	public Knight(int row, int column, Colour colour, Board board) {
		super(row, column, colour, board);
	}
	
	/**
	 * Compute where this piece can move to on the board
	 * 
	 * @return A List of pairs (row,column), where each pair represents
	 * a square that this knight can move to
	 */
	@Override
	public List<Pair> getMoves() {
		List<Pair> moves = new ArrayList<Pair>();
		
		// Use arrays to generalize the process of checking each square
		// the knight can move to
		int[] directions = {1,-1};
		int[] offsets = {2,-2};
		
		for(int offset : offsets) {
			for(int direction : directions) {
				if(board.isMovable(row+direction, column+offset, colour)) {
					moves.add(new Pair(row+direction, column+offset));
				}
				if(board.isMovable(row+offset, column+direction, colour)) {
					moves.add(new Pair(row+offset, column+direction));
				}
			}
		}
		
		return moves;
	}
	
	/**
	 * Create a String representation of this knight
	 * 
	 * @return A String representation of this knight
	 */
	public String toString() {
		return (colour == Colour.WHITE) ? "N" : "n";
	}
	
	/**
	 * Compute all squares that this piece is PROTECTING. A protected
	 * square is a square that is currently occupied by an allied piece,
	 * but which the enemy king can't move to without placing himself
	 * in check.
	 * 
	 * This might sound like a strange definition, but see the Javadoc
	 * for the King class's implementation of this method to understand
	 * why we phrase it this way.
	 * 
	 * This is useful for computing where a King cannot legally move
	 * without placing himself in check.
	 * 
	 * @return A list of Pairs, where each pair represents a square protected by
	 * this piece
	 */
	@Override
	public List<Pair> getProtectedSquares() {
		List<Pair> protectedSquares = new ArrayList<Pair>();
		
		// Use arrays to generalize the process of checking each square
		// the knight can move to
		int[] directions = {1,-1};
		int[] offsets = {2,-2};
		
		for(int offset : offsets) {
			for(int direction : directions) {
				// Only add a square to the list if it is occupied by an allied piece
				if(board.isPiece(row+direction, column+offset) &&
				   board.getPiece(row+direction, column+offset).getColour() == colour) {
					protectedSquares.add(new Pair(row+direction, column+offset));
				}
				if(board.isPiece(row+offset, column+direction) &&
				   board.getPiece(row+offset, column+direction).getColour() == colour) {
					protectedSquares.add(new Pair(row+offset, column+direction));
				}
			}
		}
		
		return protectedSquares;
	}
}
