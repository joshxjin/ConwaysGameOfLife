package Tests;

import Backend.*;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CGoLTests {

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void AliveListTest() {
		GameOfLife game = new GameOfLife();
		game.addCell(0, 0);
		game.addCell(0, 1);
		game.addCell(0, -1);
		assertTrue(game.getAliveCells().contains(new Cell(0,0)));
	}

}
