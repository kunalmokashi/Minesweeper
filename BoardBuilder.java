package minesweeper;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
 * Class to build the board based on the input length and width  of the board. The mines will be distributed randomly across the board.
 */
public class BoardBuilder {
	public static Board build() {
		Scanner reader = new Scanner(System.in);
		System.out.println("Length: ");
		int length = reader.nextInt();
		System.out.println("Width: ");
		int width = reader.nextInt();
		Board board = new Board();
		board.setLength(length);
		board.setWidth(width);
		board.setUp();
		System.out.println("mineNumber: ");
		int mineNum = reader.nextInt();
		board.setMineNum(mineNum);
		for (int i = 0; i < mineNum; i++) {
			System.out.println(i + 1 + "th Coordinate: ");
//			String coordinate = reader.next();
//			String[] parts = coordinate.split(",");
//			if (parts.length == 2) {
//				String x = parts[0];
//				String y = parts[1];
//				System.out.println(x + " " + y);
//				board.getCell(Integer.valueOf(x), Integer.valueOf(y)).setIsMine();
//			}
			Random rand = new Random();
			int x = rand.nextInt(length);
			int y = rand.nextInt(width);
			System.out.println(x + "," + y);
			if (board.getCell(x, y).getIsMine()) {
				i--;
			} else
				board.getCell(x, y).setIsMine();
		}
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < width; y++) {
				int number = 0;
				Cell cur = board.getCell(x, y);
				List<Cell> neighbors = BoardBuilder.getNeighbors(x, y, board);
				for (Cell neighbor : neighbors) {
					if (neighbor.getIsMine()) {
						number++;
					}
				}
				cur.setNumber(cur.getIsMine() ? -1 : number);
			}
		}
		return board;
	}

	public static List<Cell> getNeighbors(int x, int y, Board board) {
		List<Cell> neighbors = new LinkedList<>();
		int length = board.getLength();
		int width = board.getWidth();
		if (y > 0 && !board.getCell(x, y - 1).getIsVisited()) {
			neighbors.add(board.getCell(x, y - 1));
		}
		if (y < width - 1 && !board.getCell(x, y + 1).getIsVisited()) {
			neighbors.add(board.getCell(x, y + 1));
		}
		if (x > 0 && !board.getCell(x - 1, y).getIsVisited()) {
			neighbors.add(board.getCell(x - 1, y));
		}
		if (x < length - 1 && !board.getCell(x + 1, y).getIsVisited()) {
			neighbors.add(board.getCell(x + 1, y));
		}
		if (y > 0 && x > 0 && !board.getCell(x - 1, y - 1).getIsVisited()) {
			neighbors.add(board.getCell(x - 1, y - 1));
		}
		if (y < width - 1 && x > 0 && !board.getCell(x - 1, y + 1).getIsVisited()) {
			neighbors.add(board.getCell(x - 1, y + 1));
		}
		if (y > 0 && x < length - 1 && !board.getCell(x + 1, y - 1).getIsVisited()) {
			neighbors.add(board.getCell(x + 1, y - 1));
		}
		if (y < width - 1 && x < length - 1 && !board.getCell(x + 1, y + 1).getIsVisited()) {
			neighbors.add((board.getCell(x + 1, y + 1)));
		}
		return neighbors;
	}
}
