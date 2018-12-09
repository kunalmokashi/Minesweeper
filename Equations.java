package minesweeper;

import java.util.HashSet;
import java.util.Set;

/**
 * Equations object that holds the cell ids and the parent cell value that
 * corresponds to the number of mines in the given set of cells.
 * 
 * @author Kunal
 *
 */
public class Equations {

	private int parentCellValue;
	private Cell parentCell;
	private int cellId;
	private Set<Integer> cellIds = new HashSet<>();

	public int getParentCellValue() {
		return parentCellValue;
	}

	public void setParentCellValue(int parentCellValue) {
		this.parentCellValue = parentCellValue;
	}

	public Set<Integer> getCellIds() {
		return cellIds;
	}

	public void setCellIds(Set<Integer> cellIds) {
		this.cellIds = cellIds;
	}

	public void addCellId(Integer cellId) {
		this.cellIds.add(cellId);
	}

	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public Cell getParentCell() {
		return this.parentCell;
	}

	public void setParentCell(Cell parentCell) {
		this.parentCell = parentCell;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellIds == null) ? 0 : cellIds.hashCode());
		result = prime * result + parentCellValue;
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
		Equations other = (Equations) obj;
		if (cellIds == null) {
			if (other.cellIds != null)
				return false;
		} else if (!cellIds.equals(other.cellIds))
			return false;
		if (parentCellValue != other.parentCellValue)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Equations [parentCellValue=" + parentCellValue + ", parentCell=" + parentCell + ", cellIds=" + cellIds
				+ "]";
	}
}
