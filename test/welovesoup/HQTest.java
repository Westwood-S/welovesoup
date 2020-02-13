package welovesoup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import battlecode.common.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class HQTest {

	@Mock
	HQ hq;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void finalize() {
		hq.numMiners = 0;
	}

	@Test
	public void testHQCreation() throws GameActionException {
		assertEquals(0, hq.numMiners);
	}

	@Test
	public void testAllLandScapers() {
		RobotInfo[] robots = new RobotInfo[1];
		doAnswer((i)->{
			if(robots.length < 8)
				return false;
			else
				return true;
		}).when(hq).AllLandScapers(robots);
		int i = robots.length;
		hq.AllLandScapers(robots);
		verify(hq,times(1)).AllLandScapers(robots);
		assertEquals(false, hq.AllLandScapers(robots));
	}

	@Test
	public void testTakeTurnCreateOneMiner() throws GameActionException {
//		HQ hq = Mockito.mock(HQ.class);
		doAnswer((i)->{
			hq.numMiners++;
			return hq.numMiners;
		}).when(hq).takeTurn();
		hq.takeTurn();
		verify(hq,times(1)).takeTurn();
		assertEquals(1, hq.numMiners);
	}

	@Test
	public void testTakeTurnCreateTwoMiners() throws GameActionException {
		//HQ hq = Mockito.mock(HQ.class);
		hq.numMiners = 0;
		assertEquals(hq.numMiners, 0);
		doAnswer((i)->{
			if(hq.numMiners < 7)
				hq.numMiners++;
			return hq.numMiners;
		}).when(hq).takeTurn();
		hq.takeTurn();
		hq.takeTurn();
		verify(hq,times(2)).takeTurn();
		assertEquals(2, hq.numMiners);
	}

	@Test
	public void testTakeTurnCreateMoreThanSevenMiners() throws GameActionException {
		//HQ hq = Mockito.mock(HQ.class);
		hq.numMiners = 0;
		assertEquals(hq.numMiners, 0);
		doAnswer((i)->{
			if(hq.numMiners < 7)
				hq.numMiners++;
			return hq.numMiners;
		}).when(hq).takeTurn();
		for(int i=0; i<8; i++)
			hq.takeTurn();
		verify(hq,times(8)).takeTurn();
		assertEquals( 7, hq.numMiners);
	}
}
