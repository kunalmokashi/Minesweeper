package minesweeper;

public class Board {
	private int length;
	private int width;
	private int mineNum;
	private boolean isExplored = false;
	private int totalExplored;
	private Cell[][] cellList;
	private State state;

	public Board() {
	}

	public void setUp() {
		this.cellList = new Cell[length][width];
		// initialize the coordinates and set the number.
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < width; y++) {
				cellList[x][y] = new Cell(x, y);
				cellList[x][y].setId(x * width + y);
			}
		}
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public int getWidth() {
		return width;
	}

	public int getMineNum() {
		return mineNum;
	}

	public Cell getCell(int x, int y) {
		return this.cellList[x][y];
	}

	public int getTotalExplored() {
		return totalExplored;
	}

	public void incrementTotalExplored() {
		this.totalExplored++;
	}

	public int getSize() {
		return length * width;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Cell getCellById(int cellId) {
		for (Cell[] cells : cellList) {
			for (Cell cell : cells) {
				if (cell.getId() == cellId) {
					return cell;
				}
			}
		}
		return null;
	}

	public void setMineNum(int mineNum) {
		this.mineNum = mineNum;
	}
}
