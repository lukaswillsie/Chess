package Chess.com.lukaswillsie.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a King on a chessboard.
 * 
 * @author Lukas Willsie
 */
public class King extends Piece {
	// The character representation of this class.
	public static final char charRep = 'k';

	/**
	 * Create a knew King object, of the given colour, located at the given position
	 * on the given board.
	 * 
	 * King must actually be added to the given board, through the Board.addPiece() method,
	 * before any computation takes place.
	 * 
	 * @param row - the row the new King is on
	 * @param column - the column the new King is on
	 * @param colour - the colour of the King
	 * @param board - the board that this King has been placed on
	 */
	public King(int row, int column, Colour colour, Board board) {
		super(row, column, colour, board);
	}

	/**
	 * Compute where this piece can move to on the board, accounting for
	 * the King not being able to put himself into check
	 * 
	 * @return A List of pairs (row,column), where each pair represents
	 * a square that this King can move to, according to its rules of movement
	 */
	@Override
	public List<Pair> getMoves() {
		List<Pair> moves = new ArrayList<Pair>();
		Colour enemy_colour = (colour == Colour.WHITE) ? Colour.BLACK : Colour.WHITE;
		
		// We pick the King up from the board, temporarily, to get a complete
		// picture of where enemy pieces can move. Without this step,
		// the King may misinterpret certain squares as legal.
		// For example, suppose the King has an enemy rook on its left.
		// Then the square to the right of the King is not technically
		// a square that the rook can move to at the moment, but the King still
		// can't move there because if he did he would be in check. We need
		// to remove the King from the board to catch such subtleties. 
		board.pickUp(this);
		
		// By definition, protected squares are the squares which a King is not allowed
		// to move to, so fetch all of them
		List<Pair> enemyProtectedSquares = board.getProtectedSquares(enemy_colour);
		
		// We can put the King back on the board now
		board.restore(this);
		
		int[] row_offsets = {-1,1};
		int[] column_offsets= {-1,0,1};
		
		// Check the rows in front of and behind the King
		for(int row_offset : row_offsets) {
			for(int column_offset : column_offsets) {
				// The King can move to a square if
				// 1) It is on the board
				// 2) It does not hold an ally
				// 3) It is not being protected by the enemy
				if(board.isMovable(this.row+row_offset, this.column+column_offset, this.colour)
				&& Collections.binarySearch(enemyProtectedSquares, new Pair(this.row+row_offset, this.column+column_offset)) < 0) {
					moves.add(new Pair(this.row + row_offset, this.column + column_offset));
				}
			}
		}
		
		
		// Check the square in the same row and to the right of the King
		if(board.isMovable(this.row, this.column+1, this.colour)
		&& Collections.binarySearch(enemyProtectedSquares, new Pair(this.row, this.column+1)) < 0) {
			moves.add(new Pair(this.row, this.column + 1));
		}
		
		// Check the square in the same row and to the left of the King
		if(board.isMovable(this.row, this.column-1, this.colour)
		&& Collections.binarySearch(enemyProtectedSquares, new Pair(this.row, this.column-1)) < 0) {
			moves.add(new Pair(this.row, this.column - 1));
		}
		
		// If the King can castle, the convention in chess apps is to include the square that the King
		// ends up on after the castle as a square the King can move to
		if(board.canKingsideCastle(colour)) {
			moves.add(new Pair(this.getRow(), this.getColumn()+2));
		}
		
		if(board.canQueensideCastle(colour)) {
			moves.add(new Pair(this.getRow(), this.getColumn() - 2));
		}
		
		return moves;
	}
	
	/**
	 * Compute all squares that this piece is PROTECTING. A protected
	 * square is a square that this piece is preventing the enemy king
	 * from moving to. In other words, it's a square that the enemy
	 * king can't move to, lest he put himself in check. <br>
	 *  <br>
	 * We note that there is a subtlety to this definition when talking
	 * about Kings. In particular, a King can be protecting a square without
	 * being able to move there. For example, suppose there's a white King,
	 * and there's a black Rook in the row in front of it, preventing it
	 * from moving forward. Suppose the black King is two squares in front of
	 * the white King: <br>
	 * <br>
	 * k - Black king <br>
	 * XXXr - Black rook <br>
	 * K - White king <br>
	 * <br>
	 * Then the White King CAN'T MOVE FORWARD without placing himself in check,
	 * and if there was a black piece directly in front of him he wouldn't be able
	 * to take it, because of the rook. Regardless, the black King can't move to the
	 * square in front of the white King either, because then he'd be under attack by
	 * the white King. <br>
	 *  <br>
	 * All this to say that a King's protected squares are all those squares around
	 * him which are either empty or occupied by an ally. <br>
	 * <br>
	 * @return A list of Pairs, where each pair represents a square protected by
	 * this piece
	 */
	@Override
	public List<Pair> getProtectedSquares() {
		List<Pair> protected_squares = new ArrayList<Pair>();
		
		int[] row_offsets = {-1,1};
		int[] column_offsets= {-1,0,1};
		
		// Check the rows in front of and behind the King
		for(int row_offset : row_offsets) {
			for(int column_offset : column_offsets) {
				// The King protects a square just like any other piece;
				// if it is occupied by an ally or is empty
				if((board.isPiece(this.row+row_offset,this.column+column_offset)
				&& board.getPiece(this.row+row_offset, this.column+column_offset).getColour() == this.colour)
				|| board.isEmpty(this.row+row_offset, this.column+column_offset)) {
					protected_squares.add(new Pair(this.row + row_offset, this.column + column_offset));
				}
			}
		}
		
		// Check the square in the same row and to the right of the King
		if((board.isPiece(this.row,this.column+1)
		 && board.getPiece(this.row, this.column+1).getColour() == this.colour)
		 || board.isEmpty(this.row, this.column+1)) {
			protected_squares.add(new Pair(this.row, this.column + 1));
		}
		
		// Check the square in the same row and to the left of the King
		if((board.isPiece(this.row,this.column-1)
		 && board.getPiece(this.row, this.column-1).getColour() == this.colour)
		 || board.isEmpty(this.row, this.column-1)) {
			protected_squares.add(new Pair(this.row, this.column - 1));
		}
		
		return protected_squares;
	}
	
	/**
	 * Create a String representation of this King.
	 * 
	 * Guaranteed to only be one character long
	 * 
	 * @return A String representation of this King
	 */
	public String toString() {
		return (colour == Colour.WHITE) ? "K" : "k";
	}

	/**
	 * Compute whether or not this piece is giving check to the enemy King. Note that this is
	 * a little subtle. The piece does not have to actually be able to capture the enemy King
	 * to be attacking it. As an example, a pinned piece that can't actually move at all can still
	 * be giving check. To demonstrate:
	 * 
	 * kXX
	 * XrX
	 * XXB
	 * XKX
	 * 
	 * Here, the black Rook is pinned by the white Bishop, and can't move at all. That is, its
	 * getMoves() would return an empty list. But white is still in check because the black
	 * Rook and white King are in the same column. 
	 * @return true if and only if this piece is giving check to the enemy king
	 */
	@Override
	public boolean isCheckingKing() {
		// It's impossible for a King to be checking the enemy King, since this would
		// require one of the King's to have moved themselves into check
		return false;
	}
}
