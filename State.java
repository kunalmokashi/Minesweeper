package minesweeper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Gives the state of the board at any point of time. It will contain the list
 * of explored cells, unexplored cells and list of the equations used to solve
 * the problem.
 * 
 * @author Kunal
 *
 */
public class State {

	private List<Cell> unexploredCells = new ArrayList<>();
	private List<Cell> exploredCells = new ArrayList<>();
	private Set<Cell> discoveredMines = new HashSet<>();
	private Set<Equations> listOfEquationsForEdge = new HashSet<>();
	private boolean gameOver;
	private boolean finised;
	private boolean autoExpand = false;

	public boolean getAutoExpand() {
		return this.autoExpand;
	}

	public void setAutoExpand(boolean b) {
		this.autoExpand = b;
	}

	public List<Cell> getUnexploredCells() {
		return unexploredCells;
	}

	public void setUnexploredCells(List<Cell> unexploredCells) {
		this.unexploredCells.addAll(unexploredCells);
	}

	public void decrementUnexploredCells(Cell cell) {
		this.unexploredCells.remove(cell);
	}

	public List<Cell> getExploredCells() {
		return exploredCells;
	}

	public void addToExploredCells(Cell cell) {
		this.exploredCells.add(cell);
	}

	public Set<Cell> getFlaggedMineCells() {
		return discoveredMines;
	}

	public void flagMineCells(Cell cell) {
		this.discoveredMines.add(cell);
	}

	public Set<Equations> getListOfEquationsForEdge() {
		return listOfEquationsForEdge;
	}

	public void setListOfEquationsForEdge(Equations equation) {
		this.listOfEquationsForEdge.add(equation);
	}

	public void emptyEquationList() {
		this.listOfEquationsForEdge.clear();
	}

	@Override
	public String toString() {
		return "State [exploredCells=" + exploredCells + ", discoveredMines=" + discoveredMines
				+ ", listOfEquationsForEdge=" + listOfEquationsForEdge + "]";
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public boolean isFinised() {
		return finised;
	}

	public void setFinised(boolean finised) {
		this.finised = finised;
	}

}
