package edu.brandeis.cs12b.pa2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import edu.brandeis.cs12b.pa2.provided.GameOf15;

public class ComputerPlayer {

	public static void main(String[] args) {
		ComputerPlayer comp = new ComputerPlayer();
		System.out.println(comp.solve(new GameOf15()));
		
		// Solving using A*
		System.out.println("-----");
		GameOf15 game = new GameOf15();
		System.out.println("Initial board to be solved:");
		System.out.println(game);
		State solvedState = comp.actuallySolve(game);
		System.out.println("This board was solved in " + solvedState.moves + " moves:");
		System.out.println(solvedState.board);
		
	}

	
	/**
	 * Here, you should write a method that will print a sequence of moves to solve the GameOf15. Finally,
	 * return the solved GameOf15.
	 * 
	 * However, you don't have to solve the entire game, you merely have to get the blank space
	 *  into the right place.
	 * 
	 * In other words, if your board looks like this:
	 * 
	 * 1	13	6	2	
     * 5	8	10	4	
     * 9	7	3	11	
     * 14	15		12 
	 * 
	 * 
	 * then you need only make one move (right), in order to get this board:
	 * 
	 * 
	 * 1	13	6	2	
     * 5	8	10	4	
     * 9	7	3	11	
     * 14	15	12	
     * 
     *
     * Solving the entire board can be done for (a lot of) extra credit! TA Ryan personally
     * suggests you try and solve the entire board with some help from Google, as some of
     * the algorithms can be pretty complex. One option (not the easiest) 
     * is to use the A* search algorithm.
     * 
     * This might help you get started: http://stackoverflow.com/questions/94975/how-do-you-solve-the-15-puzzle-with-a-star-or-dijkstras-algorithm
	 * 
	 * You **must** not modify GameOf15.java for this part.
	 * 
	 * Print out each move on its own line using the same letters as before.
	 * u -- move up
	 * d -- move down
	 * l -- move left (that's an L, not an I)
	 * r -- move right
	 * 
	 * 
	 * 
	 * @return the solved game of 15 board
	 */
	public GameOf15 solve(GameOf15 gof) {
		int val = gof.getValue(3, 3);
		if (val == 0) {
			return gof;
		}
		while (val != 0) {
			
			gof.moveUp();
			System.out.println("u");
			
			gof.moveLeft();
			System.out.println("l");
				
			val = gof.getValue(3, 3);
		}
		return gof;
	}
	
	class State {
		GameOf15 board;
		int moves;
		GameOf15 previous;
		
		public State(GameOf15 b, int m, GameOf15 p) {
			this.board = b;
			this.moves = m;
			this.previous = p;
		}
	}
	
	public State actuallySolve(GameOf15 gof) {
		//Creating a PQ to store board states, reachable from already explored states
		PriorityQueue<State> p = new PriorityQueue<State>(100, new Comparator<State>() {
			// Comparator between a new state and an already stored state
			// This is the A* priority function: # of moves to get to a state + Hamming cost function (# of incorrectly positioned numbers)
			public int compare(State i, State j) {
				return Integer.compare(
					i.moves + i.board.wrongPositions(),
					j.moves + j.board.wrongPositions()
				);
			}
		});
			
		
		// Insert initial state into priority queue
		p.add(new State(gof, 0, null));
		
		// The queue is processed as reachable states are added and priority states are dequeued
		while (!p.isEmpty()) {
			State s = p.poll();
			
			if (s.board.hasWon()) {
				return s;
			}
			
			ArrayList<GameOf15> boards = s.board.reachableBoards();
			// Add all reachable states that are not redundant (differ from the already explored)
			for (GameOf15 board: boards) {
				if (s.previous == null || !board.equals(s.previous)) {
					p.add(new State(board, s.moves + 1, s.board));
				}
			}
		}
		
		// Inaccessible anyway because instructions say the puzzles are guaranteed to be solvable.
		return new State(gof, -1, null);
	}
}
