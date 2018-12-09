package minesweeper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Main class - This is the solver class that runs and tries to clear the board
 * by solving the set of equations given by the state of the board.
 * 
 * @author Kunal
 *
 */
public class GamePlayer {

	static Map<Integer, Integer> knowledgeBase = new HashMap<>();

	public static void main(String args[]) {
		Board board = BoardBuilder.build();
		int length = board.getLength();
		int width = board.getWidth();
		List<Cell> unexploredCells = new ArrayList<>();
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < width; y++) {
				Cell cur = board.getCell(x, y);
				unexploredCells.add(cur);
				System.out.print("     " + cur);
			}
			System.out.println();
			System.out.println();
		}
		// Choose a first point to start the game.
		State initialState = new State();
		initialState.setUnexploredCells(unexploredCells);
		board.setState(initialState);
		Cell start = board.getCell(0, 0);

		DisplayMinesweeper display = new DisplayMinesweeper(board.getState(), board.getLength(), board.getWidth());
		display.setVisible(true);
		try {
			TimeUnit.MILLISECONDS.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		startGame(board, start, display);
	}

	private static void startGame(Board board, Cell start, DisplayMinesweeper display) {
		if (start.getIsMine()) {
			System.out.println("Game over.");
			System.exit(1);
		} else {
			start.setIsVisited();
			board.incrementTotalExplored();
			board.getState().addToExploredCells(start);
			board.getState().decrementUnexploredCells(start);
			List<Cell> neighbors = BoardBuilder.getNeighbors(start.getX(), start.getY(), board);
			List<Cell> nonEmptyCellList = new ArrayList<>();
			if (start.getNumber() == 0) {
				// explore all neighbors.
				System.out.println("start explored 0");
				exploreAllNeighbors(board, neighbors, start);
				makeMove(board, neighbors, nonEmptyCellList, display);
			} else {
				System.out.println("start explored -" + start.getNumber());
				nonEmptyCellList.add(start);
				Cell next = board.getCell(0, board.getWidth() - 1);
				makeMove(board, Collections.singletonList(next), nonEmptyCellList, display);
			}
		}
	}

	private static void makeMove(Board board, List<Cell> neighbors, List<Cell> nonEmptyCellList,
			DisplayMinesweeper display) {
		List<Cell> nextCells = new ArrayList<>(neighbors);
		while (!board.getState().isGameOver() && !board.getState().isFinised()) {
			display.update(board);
			List<Cell> newPass = exploreGame1(board, nextCells, nonEmptyCellList, display,
					!board.getState().getAutoExpand());
			if (board.getState().isGameOver() || board.getState().isFinised()) {
				break;
			}
			nextCells = newPass;
		}
		display.update(board);
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (board.getState().isGameOver()) {
			System.out.println("Make move - Game over.");
			System.out.println("Number of explored cells - " + board.getTotalExplored());
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			display.loss();
			System.exit(1);
		} else if (board.getState().isFinised()) {
			System.out.println("Win.");
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			display.win();
			System.exit(0);
		}
	}

	/*
	 * Creates a startup equation.
	 */
	private static void createEquation(Board board, Cell current, List<Cell> newNeighbors) {
		Equations equation1 = new Equations();
		equation1.setParentCell(current);
		int value = current.getNumber();
		for (Cell c : newNeighbors) {
			if (board.getState().getFlaggedMineCells().contains(c)) {
				value = value - 1;
			} else {
				equation1.addCellId(c.getId());
			}
		}
		equation1.setParentCellValue(value);
		boolean allowAddition = true;
		for (Equations eq : board.getState().getListOfEquationsForEdge()) {
			if (eq.getCellIds().size() == equation1.getCellIds().size()
					&& eq.getCellIds().containsAll(equation1.getCellIds())
					&& eq.getParentCellValue() == equation1.getParentCellValue()) {
				allowAddition = false;
				break;
			}
		}
		if (allowAddition) {
			board.getState().setListOfEquationsForEdge(equation1);
		} else {
			System.out.println("Equation already exists");
		}
	}

	private static List<Cell> exploreGame1(Board board, List<Cell> cellsToBeExplored, List<Cell> nonEmptyCellList,
			DisplayMinesweeper display, boolean click) {
		Set<Cell> newThisRound = new HashSet<>();
		for (Cell cellToBeExplored : cellsToBeExplored) {
			updateEquationsForEdge(board.getState(), cellToBeExplored);
			if (!cellToBeExplored.getIsVisited()) {
				cellToBeExplored.setIsVisited();
				board.incrementTotalExplored();
				board.getState().addToExploredCells(cellToBeExplored);
				board.getState().decrementUnexploredCells(cellToBeExplored);
			}
			if (cellToBeExplored.getIsMine()) {
				// failed
				board.getState().setGameOver(true);
				System.out.println("game over - " + cellToBeExplored);
				System.out.println("discoverd mines - " + board.getState().getFlaggedMineCells());
			} else if ((board.getTotalExplored() == board.getSize() - board.getMineNum())) {
				// success
				System.out.println("win - " + cellToBeExplored);
				board.getState().setFinised(true);
			}
			if (board.getState().isGameOver() || board.getState().isFinised()) {
				// we are done, returning an empty list and setting the gameOver or done
				// variables will let the caller know what to do next.
				return Collections.emptyList();
			}
			if (click) {
				display.updateCell(cellToBeExplored);
				try {
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
				click = false;
			}
			if (cellToBeExplored.getNumber() == 0) {
				List<Cell> newNeighbors = BoardBuilder.getNeighbors(cellToBeExplored.getX(), cellToBeExplored.getY(),
						board);
				if (!newNeighbors.isEmpty()) {
					exploreAllNeighbors(board, newNeighbors, cellToBeExplored);
					newThisRound.addAll(newNeighbors);
				} else {
					System.out
							.println("All neighbors of cell id " + cellToBeExplored.getId() + " are already explored.");
					// all neighbors were already explored.
				}
			} else {
				nonEmptyCellList.add(cellToBeExplored);
			}
		}
		if (!newThisRound.isEmpty()) {
			// there was an empty cell, explore its neighbors to find more empty cells.
			board.getState().setAutoExpand(true);
			return new ArrayList<>(newThisRound);
		} else {
			board.getState().setAutoExpand(false);
			createEquationsForEdge(board, nonEmptyCellList);
			List<Cell> probableSafeCells = equationInit(board);
			nonEmptyCellList.clear();
			if (probableSafeCells == null || probableSafeCells.isEmpty()) {
				// check the corners, if they are already explored take a random guess or else
				// go for the corner.
				Set<Equations> edgeEquations = board.getState().getListOfEquationsForEdge();
				List<Cell> unexploredCell = board.getState().getUnexploredCells();
				Set<Cell> probableRandomChoices = new HashSet<>();
				Set<Cell> discoveredMines = new HashSet<>(board.getState().getFlaggedMineCells());

				for (Cell c : unexploredCell) {
					boolean isAvailable = false;
					for (Equations eq : edgeEquations) {
						if (eq.getCellIds().contains(c.getId())) {
							isAvailable = true;
							continue;
						}
					}
					if (!isAvailable) {
						probableRandomChoices.add(c);
					}

				}
				Random random = new Random();
				if (probableRandomChoices.isEmpty()) {
					// every unexplored cell is part of the edge equations, choose a random one from
					// the edge.
					Set<Integer> cellIdsOnEdge = new HashSet<>();
					for (Equations eq : edgeEquations) {
						for (Integer cellId : eq.getCellIds()) {
							if (!discoveredMines.contains(board.getCellById(cellId))) {
								cellIdsOnEdge.add(cellId);
							}
						}
					}
					List<Integer> cellsList = new ArrayList<>(cellIdsOnEdge);
					System.out.println("proabable random choices cellsList - " + cellsList);
					int index = random.nextInt(cellsList.size());
					return Collections.singletonList(board.getCellById(cellsList.get(index)));
				} else {
					probableRandomChoices.removeAll(discoveredMines);
					System.out.println("proabable random choices - " + probableRandomChoices);
					List<Cell> comparisonList = new ArrayList<>(probableRandomChoices);
					if (!comparisonList.isEmpty()) {
						int index = random.nextInt(comparisonList.size());
						System.out.println("returning one of the random option - " + comparisonList.get(index));
						return Collections.singletonList(comparisonList.get(index));
					} else {
						System.out.println("total explored cells" + board.getTotalExplored());
						board.getState().setFinised(true);
						return Collections.emptyList();
					}
				}
			} else {
				Random random = new Random();
				int index = random.nextInt(probableSafeCells.size());
				return Collections.singletonList(probableSafeCells.get(index));
			}
		}
	}

	private static void updateEquationsForEdge(State state, Cell c) {
		// Also remove the equation if no cell id left.
		List<Equations> emptyEquations = new ArrayList<>();
		List<Equations> equations = new ArrayList<>(state.getListOfEquationsForEdge());
		for (Equations equation : equations) {
			if (equation.getCellIds().contains(c.getId())) {
				System.out
						.println("Updating the edge to remove the explored cell " + c + " from equation - " + equation);
				Set<Integer> cellIds = equation.getCellIds();
				cellIds.remove((Integer) c.getId());
				if (cellIds.isEmpty()) {
					emptyEquations.add(equation);
				}
			}
		}
		equations.removeAll(emptyEquations);
		state.getListOfEquationsForEdge().clear();
		state.getListOfEquationsForEdge().addAll(equations);
	}

	private static void createEquationsForEdge(Board board, List<Cell> edge) {
		for (Cell edgeCell : edge) {
			List<Cell> newNeighbors = BoardBuilder.getNeighbors(edgeCell.getX(), edgeCell.getY(), board);
			createEquation(board, edgeCell, newNeighbors);
		}
	}

	private static void exploreAllNeighbors(Board board, List<Cell> neighbors, Cell currentCell) {
		for (Cell c : neighbors) {
			c.setIsVisited();
			board.incrementTotalExplored();
			board.getState().addToExploredCells(c);
			board.getState().decrementUnexploredCells(c);
		}
	}

	private static void updateEquationsForEdgeToEliminateMine(State state, List<Cell> cells) {
		List<Equations> emptyEquations = new ArrayList<>();
		List<Equations> equations = new ArrayList<>(state.getListOfEquationsForEdge());
		for (Equations equation : equations) {
			for (Cell c : cells) {
				if (equation.getCellIds().contains(c.getId()) && c.getIsMine()) {
					System.out.println(
							"Updating the edge to remove the mine cell - " + c + " from equation - " + equation);
					equation.getCellIds().remove((Integer) c.getId());
					equation.setParentCellValue(equation.getParentCellValue() - 1);
					if (equation.getCellIds().isEmpty()) {
						emptyEquations.add(equation);
					}
				}
			}
		}
		equations.removeAll(emptyEquations);
		state.getListOfEquationsForEdge().clear();
		state.getListOfEquationsForEdge().addAll(equations);
	}

	public static List<Cell> equationInit(Board board) {
		System.out.println("State of the board - " + board.getState().getFlaggedMineCells());
		Equations equation = null;
		int varSize = 1;
		boolean notOver = true;
		List<Equations> eqList = new ArrayList<>(board.getState().getListOfEquationsForEdge());
		for (Equations eq : eqList) {
			GamePlayer.SolveEquationUsingPrevious(board, eq);
		}
		board.getState().getListOfEquationsForEdge().clear();
		board.getState().getListOfEquationsForEdge().addAll(eqList);
		while (notOver) {
			int indexOfForLoop = 0;
			List<Equations> equationList = new ArrayList<>(board.getState().getListOfEquationsForEdge());
			for (int index = 0; index < equationList.size(); index++) {
				List<Cell> safeCells = null;
				Permutations p = new Permutations();
				Map<Integer, Integer> localKnowledgeBase = new HashMap<>();
				equation = equationList.get(index);
				List<Integer> cellIds = new ArrayList<>(equation.getCellIds());
				if (knowledgeBase.keySet().containsAll(cellIds)) {
					indexOfForLoop++;
					continue;
				} else if (cellIds.size() == 1) {
					if (equation.getParentCellValue() == 0) {
						knowledgeBase.put(cellIds.get(0), equation.getParentCellValue());
						if (!board.getCellById(cellIds.get(0)).getIsMine()) {
							Set<Cell> emptyCells = new HashSet<>();
							emptyCells.add(board.getCellById(cellIds.get(0)));
							System.out.println("returning empty cell from here - " + emptyCells);
							return new ArrayList<>(emptyCells);
						}
					} else {
						knowledgeBase.put(cellIds.get(0), equation.getParentCellValue());
						board.getState().flagMineCells(board.getCellById(cellIds.get(0)));
						updateEquationsForEdgeToEliminateMine(board.getState(), new ArrayList<>(cellIds.get(0)));
						indexOfForLoop = 0;
						break;
					}
				} else {

					varSize = cellIds.size();
					switch (varSize) {
					case 0:
						if (equation.getParentCellValue() == 0) {
							indexOfForLoop++;
							continue;
						}
						board.getState().getListOfEquationsForEdge().remove(equation);
						indexOfForLoop = 0;
						break;
					case 2:
						if (equation.getParentCellValue() == 0) {
							indexOfForLoop++;
							continue;
						}
						if (equation.getParentCellValue() == 2) {
							knowledgeBase.put(cellIds.get(0), 1);
							knowledgeBase.put(cellIds.get(1), 1);
							board.getState().flagMineCells(board.getCellById(cellIds.get(0)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(1)));
							List<Cell> cells = new ArrayList<>();
							cells.add(board.getCellById(cellIds.get(0)));
							cells.add(board.getCellById(cellIds.get(1)));
							updateEquationsForEdgeToEliminateMine(board.getState(), cells);
							indexOfForLoop = 0;
							break;
						} else {
							for (String v : p.getTwoMap().get(equation.getParentCellValue())) {
								localKnowledgeBase.clear();
								assignValues(localKnowledgeBase, equation.getCellIds(), v);
								safeCells = GamePlayer.SolveEquation(board, localKnowledgeBase, equation);
								if (safeCells == null) {
									continue;
								}
							}
							if (safeCells == null) {
								indexOfForLoop++;
								continue;
							}
						}
						break;
					case 3:
						if (equation.getParentCellValue() == 0) {
							indexOfForLoop++;
							continue;
						}
						if (equation.getParentCellValue() == 3) {
							knowledgeBase.put(cellIds.get(0), 1);
							knowledgeBase.put(cellIds.get(1), 1);
							knowledgeBase.put(cellIds.get(2), 1);
							board.getState().flagMineCells(board.getCellById(cellIds.get(0)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(1)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(2)));
							List<Cell> cells = new ArrayList<>();
							cells.add(board.getCellById(cellIds.get(0)));
							cells.add(board.getCellById(cellIds.get(1)));
							cells.add(board.getCellById(cellIds.get(2)));
							updateEquationsForEdgeToEliminateMine(board.getState(), cells);
							indexOfForLoop = 0;
							break;
						} else {
							for (String v : p.getThreeMap().get(equation.getParentCellValue())) {
								localKnowledgeBase.clear();
								assignValues(localKnowledgeBase, equation.getCellIds(), v);
								safeCells = GamePlayer.SolveEquation(board, localKnowledgeBase, equation);
								if (safeCells == null) {
									continue;
								}
							}
							if (safeCells == null) {
								indexOfForLoop++;
								continue;
							}
						}
						break;

					case 4:
						if (equation.getParentCellValue() == 0) {
							indexOfForLoop++;
							continue;
						}
						if (equation.getParentCellValue() == 4) {
							knowledgeBase.put(cellIds.get(0), 1);
							knowledgeBase.put(cellIds.get(1), 1);
							knowledgeBase.put(cellIds.get(2), 1);
							knowledgeBase.put(cellIds.get(3), 1);
							board.getState().flagMineCells(board.getCellById(cellIds.get(0)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(1)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(2)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(3)));
							List<Cell> cells = new ArrayList<>();
							cells.add(board.getCellById(cellIds.get(0)));
							cells.add(board.getCellById(cellIds.get(1)));
							cells.add(board.getCellById(cellIds.get(2)));
							cells.add(board.getCellById(cellIds.get(3)));
							updateEquationsForEdgeToEliminateMine(board.getState(), cells);
							indexOfForLoop = 0;
							break;
						} else {
							for (String v : p.getFourMap().get(equation.getParentCellValue())) {
								localKnowledgeBase.clear();
								assignValues(localKnowledgeBase, equation.getCellIds(), v);
								safeCells = GamePlayer.SolveEquation(board, localKnowledgeBase, equation);
								if (safeCells == null) {
									continue;
								}
							}
							if (safeCells == null) {
								indexOfForLoop++;
								continue;
							}
						}
						break;
					case 5:
						if (equation.getParentCellValue() == 0) {
							indexOfForLoop++;
							continue;
						}
						if (equation.getParentCellValue() == 5) {
							knowledgeBase.put(cellIds.get(0), 1);
							knowledgeBase.put(cellIds.get(1), 1);
							knowledgeBase.put(cellIds.get(2), 1);
							knowledgeBase.put(cellIds.get(3), 1);
							knowledgeBase.put(cellIds.get(4), 1);
							board.getState().flagMineCells(board.getCellById(cellIds.get(0)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(1)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(2)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(3)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(4)));
							List<Cell> cells = new ArrayList<>();
							cells.add(board.getCellById(cellIds.get(0)));
							cells.add(board.getCellById(cellIds.get(1)));
							cells.add(board.getCellById(cellIds.get(2)));
							cells.add(board.getCellById(cellIds.get(3)));
							cells.add(board.getCellById(cellIds.get(4)));
							updateEquationsForEdgeToEliminateMine(board.getState(), cells);
							indexOfForLoop = 0;
							break;
						} else {
							for (String v : p.getFiveMap().get(equation.getParentCellValue())) {
								localKnowledgeBase.clear();
								assignValues(localKnowledgeBase, equation.getCellIds(), v);
								safeCells = GamePlayer.SolveEquation(board, localKnowledgeBase, equation);
								if (safeCells == null) {
									continue;
								}
							}
							if (safeCells == null) {
								indexOfForLoop++;
								continue;
							}
						}
						break;
					case 6:
						if (equation.getParentCellValue() == 0) {
							indexOfForLoop++;
							continue;
						}
						if (equation.getParentCellValue() == 6) {
							knowledgeBase.put(cellIds.get(0), 1);
							knowledgeBase.put(cellIds.get(1), 1);
							knowledgeBase.put(cellIds.get(2), 1);
							knowledgeBase.put(cellIds.get(3), 1);
							knowledgeBase.put(cellIds.get(4), 1);
							knowledgeBase.put(cellIds.get(5), 1);
							board.getState().flagMineCells(board.getCellById(cellIds.get(0)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(1)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(2)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(3)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(4)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(5)));
							List<Cell> cells = new ArrayList<>();
							cells.add(board.getCellById(cellIds.get(0)));
							cells.add(board.getCellById(cellIds.get(1)));
							cells.add(board.getCellById(cellIds.get(2)));
							cells.add(board.getCellById(cellIds.get(3)));
							cells.add(board.getCellById(cellIds.get(4)));
							cells.add(board.getCellById(cellIds.get(5)));
							updateEquationsForEdgeToEliminateMine(board.getState(), cells);
							indexOfForLoop = 0;
							break;
						} else {
							for (String v : p.getSixMap().get(equation.getParentCellValue())) {
								localKnowledgeBase.clear();
								assignValues(localKnowledgeBase, equation.getCellIds(), v);
								safeCells = GamePlayer.SolveEquation(board, localKnowledgeBase, equation);
								if (safeCells == null) {
									continue;
								}
							}
							if (safeCells == null) {
								indexOfForLoop++;
								continue;
							}
						}
						break;
					case 7:
						if (equation.getParentCellValue() == 0) {
							indexOfForLoop++;
							continue;
						}
						if (equation.getParentCellValue() == 7) {
							knowledgeBase.put(cellIds.get(0), 1);
							knowledgeBase.put(cellIds.get(1), 1);
							knowledgeBase.put(cellIds.get(2), 1);
							knowledgeBase.put(cellIds.get(3), 1);
							knowledgeBase.put(cellIds.get(4), 1);
							knowledgeBase.put(cellIds.get(5), 1);
							knowledgeBase.put(cellIds.get(6), 1);
							board.getState().flagMineCells(board.getCellById(cellIds.get(0)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(1)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(2)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(3)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(4)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(5)));
							board.getState().flagMineCells(board.getCellById(cellIds.get(6)));
							List<Cell> cells = new ArrayList<>();
							cells.add(board.getCellById(cellIds.get(0)));
							cells.add(board.getCellById(cellIds.get(1)));
							cells.add(board.getCellById(cellIds.get(2)));
							cells.add(board.getCellById(cellIds.get(3)));
							cells.add(board.getCellById(cellIds.get(4)));
							cells.add(board.getCellById(cellIds.get(5)));
							cells.add(board.getCellById(cellIds.get(6)));
							updateEquationsForEdgeToEliminateMine(board.getState(), cells);
							indexOfForLoop = 0;
							break;
						} else {
							for (String v : p.getSevenMap().get(equation.getParentCellValue())) {
								localKnowledgeBase.clear();
								assignValues(localKnowledgeBase, equation.getCellIds(), v);
								safeCells = GamePlayer.SolveEquation(board, localKnowledgeBase, equation);
								if (safeCells == null) {
									continue;
								}
							}
							if (safeCells == null) {
								indexOfForLoop++;
								continue;
							}
						}
						break;
					default:
						if (equation.getParentCellValue() == 0) {
							indexOfForLoop++;
							continue;
						}
						System.out.println("executing default case" + varSize);
						indexOfForLoop++;
					}

					if (indexOfForLoop == 0) {
						break;
					}
					// if there are some safe cells after solving one equation, just return them to
					// get on with the game instead of solving all the other equations. Other
					// equations will be solved in the next iteration.
					Set<Cell> removeRedundantCells = new HashSet<>();
					if (safeCells != null) {
						System.out.println("safe cells not null");
						for (Cell cell : safeCells) {
							if (cell.getIsVisited()) {
								removeRedundantCells.add(cell);
							}
						}
						safeCells.removeAll(removeRedundantCells);
						return safeCells;
					}
				}
			}
			if (indexOfForLoop == 0) {
				notOver = true;
			} else {
				notOver = false;
			}
		}
		return null;

	}

	private static void SolveEquationUsingPrevious(Board board, Equations equation) {
		List<Equations> toRemove = new ArrayList<>();
		List<Equations> equations = new ArrayList<>(board.getState().getListOfEquationsForEdge());
		for (Equations e : equations) {
			if (equation.getCellIds().containsAll(e.getCellIds())
					&& equation.getCellIds().size() > e.getCellIds().size()) {
				// the previous equation can be used to solve this one.
				toRemove.add(e);
			}
		}
		if (!toRemove.isEmpty()) {
			for (Equations eq : toRemove) {
				equation.getCellIds().removeAll(eq.getCellIds());
				equation.setParentCellValue(equation.getParentCellValue() > eq.getParentCellValue()
						? equation.getParentCellValue() - eq.getParentCellValue()
						: 0);
			}
		}
	}

	public static List<Cell> SolveEquation(Board board, Map<Integer, Integer> localKnowledgeBase,
			Equations originalEquation) {
		List<Cell> safeList = new ArrayList<>();
		List<Equations> equations = new ArrayList<>(board.getState().getListOfEquationsForEdge());
		for (int i = 0; i < equations.size(); i++) {
			Equations e = equations.get(i);
			if (GamePlayer.isTarget(e.getCellIds(), localKnowledgeBase)) {
				for (int id : e.getCellIds()) {
					if (localKnowledgeBase.get(id) != null) {
						continue;
					} else {
						int sum = 0;
						for (int value : localKnowledgeBase.values()) {
							sum += value;
						}
						localKnowledgeBase.put(id, (e.getParentCellValue() < sum) ? 0 : e.getParentCellValue() - sum);
					}
				}
			}
			if (localKnowledgeBase.keySet().containsAll(e.getCellIds())) {
				int sum = 0;
				for (int id : e.getCellIds()) {
					sum += localKnowledgeBase.get(id);
				}
				// not correct
				if (sum != e.getParentCellValue()) {
					return null;
				}
			}
			if (i == board.getState().getListOfEquationsForEdge().size() - 1) {
				return null;
			}
		}
		// find a cell that is not a mine.
		for (Map.Entry<Integer, Integer> entry : localKnowledgeBase.entrySet()) {
			// not a mine
			if (entry.getValue() == 0 && !originalEquation.getCellIds().contains(entry.getKey())) {
				System.out.println("equation for which adding safe cells - " + originalEquation.getCellIds());
				safeList.add(board.getCellById(entry.getKey()));
				knowledgeBase.put(entry.getKey(), entry.getValue());
			} else if (entry.getValue() == 1 && !originalEquation.getCellIds().contains(entry.getKey())) {
				board.getState().flagMineCells(board.getCellById(entry.getKey()));
				knowledgeBase.put(entry.getKey(), entry.getValue());
			}
		}
		return safeList;
	}

	public static boolean isTarget(Collection<Integer> cellIds, Map<Integer, Integer> localKnowledgeBase) {
		int difference = 0;
		boolean idExists = false;
		for (int id : cellIds) {
			if (localKnowledgeBase.keySet().contains(id)) {
				idExists = true;
				continue;
			} else {
				difference++;
			}
		}
		return difference == 1 && idExists;
	}

	public static void assignValues(Map<Integer, Integer> localKnowledgeBase, Set<Integer> cellIds,
			String permutation) {
		List<Integer> ids = new ArrayList<>(cellIds);
		for (int i = 0; i < ids.size(); i++) {
			localKnowledgeBase.put(ids.get(i), Integer.parseInt(permutation.substring(i, i + 1)));
		}
	}
}
