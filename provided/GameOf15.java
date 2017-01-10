package edu.brandeis.cs12b.pa2.provided;

import java.util.ArrayList;
import java.util.Random;

public class GameOf15 {
	private int[][] gameBoard;
	
	public final static int NUM_ROWS = 4;
	public final static int NUM_COLS = 4;
	
	
	/**
	 * Construct a new game of 15 with the board scrambled
	 * The puzzle is gaurenteed to be solvable when generated in this way.
	 */
	public GameOf15() {
		gameBoard = new int[NUM_ROWS][NUM_COLS];
		initializeBoard();
		scramblePieces();

	}
	
	/**
	 * Create a new game of 15 using the provided board.
	 * 
	 * You will not need to use this constructor.
	 * 
	 * @param board the board to use
	 */
	public GameOf15(int[][] board) {
		gameBoard = board;
	}
	

	
	private void initializeBoard() {
		for (int row = 0; row < NUM_ROWS; row++) {
			for (int col = 0; col < NUM_COLS; col++) {
				gameBoard[row][col] = (row*4 + (col + 1)) % 16;
			}
		}
		
	}
	
	/**
	 * Check to see if the puzzle has been solved
	 * @return true is the puzzle is in a solved state, false otherwise
	 */
	public boolean hasWon() {
		for (int row = 0; row < NUM_ROWS; row++) {
			for (int col = 0; col < NUM_COLS; col++) {
				if (gameBoard[row][col] != (row*4 + (col + 1)) % 16) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void scramblePieces() {
		Random r = new Random();
		for (int i = 0; i < 50; i++) {
			switch (r.nextInt(4)) {
			case 0:
				moveUp();
				break;
			case 1:
				moveLeft();
				break;
			case 2:
				moveDown();
				break;
			case 3:
				moveRight();
				break;
			}
		}
	}
	
	/**
	 * Perform an upwards move if possible.
	 */
	public void moveUp() {
		// first, see if we can move up...
		Point blank = findBlank();
		if (blank.row == NUM_ROWS - 1)
			return; // can't move up, the blank is at the bottom!
		
		// we can move up.
		gameBoard[blank.row][blank.col] = gameBoard[blank.row + 1][blank.col];
		gameBoard[blank.row + 1][blank.col] = 0;
	}
	
	/**
	 * Move left, if possible.
	 */
	public void moveLeft() {
		// first, see if we can move left...
		Point blank = findBlank();
		if (blank.col == NUM_COLS - 1)
			return; // can't move left, the blank is all the way to the right!
		
		// we can move left.
		gameBoard[blank.row][blank.col] = gameBoard[blank.row][blank.col + 1];
		gameBoard[blank.row][blank.col + 1] = 0;
	}
	
	/**
	 * Move downwards, if possible.
	 */
	public void moveDown() {
		Point blank = findBlank();
		if (blank.row == 0)
			return; 
		
		gameBoard[blank.row][blank.col] = gameBoard[blank.row - 1][blank.col];
		gameBoard[blank.row - 1][blank.col] = 0;
	}
	
	/**
	 * Move to the right, if possible.
	 */
	public void moveRight() {
		Point blank = findBlank();
		if (blank.col == 0)
			return; 
		
		gameBoard[blank.row][blank.col] = gameBoard[blank.row][blank.col - 1];
		gameBoard[blank.row][blank.col - 1] = 0;
	}
	
	
	
	private Point findBlank() {
		for (int row = 0; row < NUM_ROWS; row++) {
			for (int col = 0; col < NUM_COLS; col++) {
				if (gameBoard[row][col] == 0)
					return new Point(row, col);
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the value at a particular row or column of the game board. A value of "0" represents
	 * the blank space.
	 * @param row the row index
	 * @param col the column index
	 * @return the value at the row,col position
	 */
	public int getValue(int row, int col) {
		return gameBoard[row][col];
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int[] row : gameBoard) {
			for (int v : row) {
				sb.append((v == 0 ? " " : v));
				sb.append("\t");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	
	// ----
	// Added for the purpose of solving with A*
	
	// Deep copy for GameOf15 objects
	public GameOf15 clone() {
		int[][] clonedBoard = new int[NUM_ROWS][NUM_COLS];
		
		for (int row = 0; row < NUM_ROWS; row++) {
			for (int col = 0; col < NUM_COLS; col++) {
				clonedBoard[row][col] = gameBoard[row][col];
			}
		}
		
		return new GameOf15(clonedBoard);
	}
	
	@Override
	public boolean equals(Object gof) {
		return this.toString().equals(gof.toString());
	}
	
	// All board states that can be reached from this board
	public ArrayList<GameOf15> reachableBoards() {
		ArrayList<GameOf15> boards = new ArrayList<GameOf15>();
		
		GameOf15 lBoard = this.clone();
		lBoard.moveLeft();
		if (!lBoard.equals(this)) {
			boards.add(lBoard);
		}

		GameOf15 rBoard = this.clone();
		rBoard.moveRight();
		if (!rBoard.equals(this)) {
			boards.add(rBoard);
		}

		GameOf15 uBoard = this.clone();
		uBoard.moveUp();
		if (!uBoard.equals(this)) {
			boards.add(uBoard);
		}

		GameOf15 dBoard = this.clone();
		dBoard.moveDown();
		if (!dBoard.equals(this)) {
			boards.add(dBoard);
		}
		
		return boards;
	}
	
	// Check how many numbers are in incorrect positions
	// This is used as a heuristic for A*
	public int wrongPositions() {
		int count = 0;
		for (int row = 0; row < NUM_ROWS; row++) {
			for (int col = 0; col < NUM_COLS; col++) {
				if (gameBoard[row][col] != NUM_COLS * row + col + 1) {	
					count++;
				}
			}
		}
		
		return count;
	}
}
