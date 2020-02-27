package welovesoup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import battlecode.common.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

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

	@Test
	public void testAllLandScapers_RobotInfo() throws GameActionException {
		hq.numMiners = 0;
		assertEquals(hq.numMiners, 0);
		RobotInfo[] robotInfo = new RobotInfo[1];
		robotInfo[0] = new RobotInfo(1,hq.team, RobotType.MINER, 3, false, 1, 3, 10, new MapLocation(1,1));
		doAnswer((i)->{
			if(robotInfo.length < 7)
				return false;
			return true;
		}).when(hq).AllLandScapers(robotInfo);
		hq.AllLandScapers(robotInfo);
		verify(hq,times(1)).AllLandScapers(robotInfo);
		assertEquals( 0, hq.numMiners);
		assertEquals( false, hq.AllLandScapers(robotInfo));
	}

	@Test
	public void testAllLandScapers_8RobotInfo() throws GameActionException {
		hq.numMiners = 0;
		assertEquals(hq.numMiners, 0);
		RobotInfo[] robotInfo = new RobotInfo[8];
		robotInfo[0] = new RobotInfo(1,hq.team, RobotType.MINER, 3, false, 1, 3, 10, new MapLocation(1,1));
		robotInfo[1] = new RobotInfo(2,hq.team, RobotType.LANDSCAPER, 3, false, 1, 3, 10, new MapLocation(1,1));
		doAnswer((i)->{
			if(robotInfo.length < 7)
				return false;
			return true;
		}).when(hq).AllLandScapers(robotInfo);
		hq.AllLandScapers(robotInfo);
		verify(hq,times(1)).AllLandScapers(robotInfo);
		assertEquals( 0, hq.numMiners);
		assertEquals( true, hq.AllLandScapers(robotInfo));
	}

	@Test
	public void testAllLandScapers_landscaper() throws GameActionException {
		hq.numMiners = 0;
		assertEquals(hq.numMiners, 0);
		RobotInfo[] robotInfo = new RobotInfo[8];
		robotInfo[0] = new RobotInfo(1,hq.team, RobotType.MINER, 3, false, 1, 3, 10, new MapLocation(1,1));
		robotInfo[1] = new RobotInfo(2,hq.team, RobotType.LANDSCAPER, 3, false, 1, 3, 10, new MapLocation(1,1));
		doAnswer((i)->{
			hq.numMiners = 9;
			if(robotInfo.length < 7)
				return false;
			if(robotInfo[1].type == RobotType.LANDSCAPER)
				return true;
			return true;
		}).when(hq).AllLandScapers(robotInfo);
		hq.AllLandScapers(robotInfo);
		verify(hq,times(1)).AllLandScapers(robotInfo);
		assertEquals( 9, hq.numMiners);
		assertEquals( true, hq.AllLandScapers(robotInfo));
	}

	@Test
	public void testAllLandScapers_team() throws GameActionException {
		hq.numMiners = 0;
		assertEquals(hq.numMiners, 0);
		RobotInfo[] robotInfo = new RobotInfo[8];
		robotInfo[0] = new RobotInfo(1,hq.team, RobotType.MINER, 3, false, 1, 3, 10, new MapLocation(1,1));
		robotInfo[1] = new RobotInfo(2,hq.team, RobotType.LANDSCAPER, 3, false, 1, 3, 10, new MapLocation(1,1));
		doAnswer((i)->{
			hq.numMiners = 9;
			if(robotInfo.length < 7)
				return false;
			if(robotInfo[1].type == RobotType.LANDSCAPER)
				return true;
			if(robotInfo[1].team != hq.team)
				return false;
			return true;
		}).when(hq).AllLandScapers(robotInfo);
		hq.AllLandScapers(robotInfo);
		verify(hq,times(1)).AllLandScapers(robotInfo);
		assertEquals( 9, hq.numMiners);
		assertEquals( true, hq.AllLandScapers(robotInfo));
	}
}
