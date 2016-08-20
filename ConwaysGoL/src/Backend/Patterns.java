package Backend;

import java.util.ArrayList;

public class Patterns {
	private static String[] patternNames = { ".", "barge", "block", "beehive", "blinker", "penta-decathlon", "snake", "glider gun" };

	public static ArrayList<Cell> getPattern(String name, int x, int y) {
		ArrayList<Cell> pattern = new ArrayList<Cell>();
		switch (name) {
		case "block":
			pattern.add(new Cell(x, y));
			pattern.add(new Cell(x + 1, y + 1));
			pattern.add(new Cell(x + 1, y));
			pattern.add(new Cell(x, y + 1));
			break;
		case "beehive":
			pattern.add(new Cell(x + 1, y));
			pattern.add(new Cell(x, y + 1));
			pattern.add(new Cell(x, y + 2));
			pattern.add(new Cell(x + 2, y + 1));
			pattern.add(new Cell(x + 2, y + 2));
			pattern.add(new Cell(x + 1, y + 3));
			break;
		case "blinker":
			for (int i = 0; i < 3; i++)
				pattern.add(new Cell(x + i, y));
			break;
		case "snake":
			pattern.add(new Cell(x, y));
			pattern.add(new Cell(x, y + 1));
			pattern.add(new Cell(x + 1, y + 1));
			pattern.add(new Cell(x + 2, y));
			pattern.add(new Cell(x + 3, y));
			pattern.add(new Cell(x + 3, y + 1));
			break;
		case "penta-decathlon":
			for (int i = 0; i < 10; i++)
				pattern.add(new Cell(x + i, y));
			break;
		case "barge":
			pattern.add(new Cell(x, y));
			pattern.add(new Cell(x + 1, y - 1));
			pattern.add(new Cell(x + 2, y));
			pattern.add(new Cell(x + 1, y + 1));
			pattern.add(new Cell(x + 3, y + 1));
			pattern.add(new Cell(x + 2, y + 2));
			break;
		case "glider gun":
			pattern.add(new Cell(x, y));
			pattern.add(new Cell(x-1, y));
			pattern.add(new Cell(x-1, y-1));
			pattern.add(new Cell(x-1, y+1));
			pattern.add(new Cell(x-2, y-2));
			pattern.add(new Cell(x-2, y+2));
			pattern.add(new Cell(x-3, y));
			pattern.add(new Cell(x-4, y-3));
			pattern.add(new Cell(x-4, y+3));
			pattern.add(new Cell(x-5, y-3));
			pattern.add(new Cell(x-5, y+3));
			pattern.add(new Cell(x-6, y-2));
			pattern.add(new Cell(x-6, y+2));
			pattern.add(new Cell(x-7, y));
			pattern.add(new Cell(x-7, y+1));
			pattern.add(new Cell(x-7, y-1));
			pattern.add(new Cell(x-16, y));
			pattern.add(new Cell(x-16, y-1));
			pattern.add(new Cell(x-17, y));
			pattern.add(new Cell(x-17, y-1));
			pattern.add(new Cell(x+3, y-1));
			pattern.add(new Cell(x+3, y-2));
			pattern.add(new Cell(x+3, y-3));
			pattern.add(new Cell(x+4, y-1));
			pattern.add(new Cell(x+4, y-2));
			pattern.add(new Cell(x+4, y-3));
			pattern.add(new Cell(x+5, y));
			pattern.add(new Cell(x+5, y-4));
			pattern.add(new Cell(x+7, y));
			pattern.add(new Cell(x+7, y+1));
			pattern.add(new Cell(x+7, y-4));
			pattern.add(new Cell(x+7, y-5));
			pattern.add(new Cell(x+17, y-2));
			pattern.add(new Cell(x+17, y-3));
			pattern.add(new Cell(x+18, y-2));
			pattern.add(new Cell(x+18, y-3));
			break;
		default:
			pattern.add(new Cell(x, y));
			break;
		}

		return pattern;
	}

	public static String[] getList() {
		return patternNames;
	}
}
