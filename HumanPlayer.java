package edu.brandeis.cs12b.pa2;

import edu.brandeis.cs12b.pa2.provided.GameOf15;

import java.util.*;

public class HumanPlayer {

	public static void main(String[] args) {
		GameOf15 gof = new GameOf15();
		
		System.out.println(gof);
		HumanPlayer hp = new HumanPlayer();
		
		hp.play(gof);
	}
	
	/**
	 * Here, you must implement a command-line interface to the 15-puzzle game.
	 * 
	 * You should ask the user for moves and execute them on the provided board
	 * until the puzzle has been solved. The move commands are:
	 * 
	 * u -- move up
	 * d -- move down
	 * l -- move left (that's an L, not an I)
	 * r -- move right
	 * 
	 * @param args
	 */
	public void play(GameOf15 gof) {
		Scanner cs = new Scanner(System.in);
		String inp = "";
		while (!gof.hasWon()) {
			System.out.println("Where do you want to move? ");
			inp = cs.next();
			if (inp.equals("u")) {
				gof.moveUp();
				System.out.println(gof);
			} else if (inp.equals("d")) {
				gof.moveDown();
				System.out.println(gof);
			} else if (inp.equals("l")) {
				gof.moveLeft();
				System.out.println(gof);
			} else if (inp.equals("r")) {
				gof.moveRight();
				System.out.println(gof);
			} else {
				System.out.println("Enter correct input");
			}
		}
		System.out.println("You win!");
	}

}
