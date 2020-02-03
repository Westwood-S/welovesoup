package welovesoup;

import static org.junit.Assert.*;

import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class HQTest {

	@Test
	public void testSanity() {
		assertEquals(2, 1+1);
	}

	@Test
	public void testHQCreation() throws GameActionException {
		HQ hq= Mockito.mock(HQ.class);
		assertEquals(0, hq.numMiners);
	}

}
