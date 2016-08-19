package Backend;

import java.util.ArrayList;

public class Patterns {
	private static String[] patternNames = { ".", "block", "beehive", "blinker", "penta-decathlon" };

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
