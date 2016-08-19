package Backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameOfLife {
	private HashSet<Cell> aliveCells;
	private HashSet<Cell> checkCells;
	private HashMap<String, ArrayList<Cell>> patterns;
	
	public GameOfLife() {
		aliveCells = new HashSet<Cell>();
		checkCells = new HashSet<Cell>();
	}

	public void addCell(int x, int y) {
		aliveCells.add(new Cell(x, y));
	}

	public HashSet<Cell> getAliveCells() {
		return aliveCells;
	}

	public void nextGeneration() {
		// update the states of each cell in checkCells based on rule
		HashSet<Cell> changedCells = new HashSet<Cell>();
		int counter = 0;
		getNeighbours();
		for (Cell cell : checkCells) {
			counter = checkNeighbours(cell);
			if (aliveCells.contains(cell)) {
				if (counter <= 2 || counter >= 5) {
					changedCells.add(cell);
				}
			} else {
				if (counter == 3) {
					changedCells.add(cell);
				}
			}
		}

		for (Cell cell : changedCells) {
			if (aliveCells.contains(cell)) {
				aliveCells.remove(cell);
			} else {
				aliveCells.add(cell);
			}
		}
	}

	public void restart() {
		aliveCells.clear();
		checkCells.clear();
	}

	private void getNeighbours() {
		// populates the check cells for the next generation
		// alive cell plus its 8 neighbouring cells
		checkCells.clear();
		for (Cell cell : aliveCells) {
			checkCells.add(cell); // current alive cell
			checkCells.add(new Cell(cell.getX(), cell.getY() - 1)); // N
			checkCells.add(new Cell(cell.getX(), cell.getY() + 1)); // S
			checkCells.add(new Cell(cell.getX() - 1, cell.getY())); // W
			checkCells.add(new Cell(cell.getX() + 1, cell.getY())); // E
			checkCells.add(new Cell(cell.getX() - 1, cell.getY() - 1)); // NW
			checkCells.add(new Cell(cell.getX() + 1, cell.getY() - 1)); // NE
			checkCells.add(new Cell(cell.getX() - 1, cell.getY() + 1)); // SW
			checkCells.add(new Cell(cell.getX() + 1, cell.getY() + 1)); // SE
		}
	}

	private int checkNeighbours(Cell cell) {
		// Counts the number of alive cells the current cell is neighbour to
		int counter = 0;

		Cell N = new Cell(cell.getX(), cell.getY() - 1);
		if (aliveCells.contains(N))
			counter++;

		Cell S = new Cell(cell.getX(), cell.getY() + 1);
		if (aliveCells.contains(S))
			counter++;

		Cell W = new Cell(cell.getX() - 1, cell.getY());
		if (aliveCells.contains(W))
			counter++;

		Cell E = new Cell(cell.getX() + 1, cell.getY());
		if (aliveCells.contains(E))
			counter++;

		Cell NW = new Cell(cell.getX() - 1, cell.getY() - 1);
		if (aliveCells.contains(NW))
			counter++;

		Cell NE = new Cell(cell.getX() + 1, cell.getY() - 1);
		if (aliveCells.contains(NE))
			counter++;

		Cell SW = new Cell(cell.getX() - 1, cell.getY() + 1);
		if (aliveCells.contains(SW))
			counter++;

		Cell SE = new Cell(cell.getX() + 1, cell.getY() + 1);
		if (aliveCells.contains(SE))
			counter++;

		return counter;
	}
	
	private void setPatterns() {
		patterns = new HashMap<String, ArrayList<Cell>>();
	}
}
