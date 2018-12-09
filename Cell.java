package minesweeper;

//Cell class
public class Cell {
	private int id;
	private int x;
	private int y;
	private int number;
	private boolean isMine;
	private boolean isVisited = false;
	private boolean flagAsMine;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setNumber(int num) {
		this.number = num;
	}

	public int getNumber() {
		return number;
	}

	public boolean getIsMine() {
		return isMine;
	}

	public void setIsMine() {
		isMine = true;
	}

	public boolean getIsVisited() {
		return isVisited;
	}

	public void setIsVisited() {
		isVisited = true;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")" + " id:" + id + " num:" + number + " " + isMine;
	}

	public boolean isFlagAsMine() {
		return flagAsMine;
	}

	public void setFlagAsMine(boolean flagAsMine) {
		this.flagAsMine = flagAsMine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
