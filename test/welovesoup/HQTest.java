package welovesoup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import battlecode.common.*;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

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

	@Test
	public void testAllLandScapers() {
		RobotInfo[] robots = new RobotInfo[1];
		HQ hq = Mockito.mock(HQ.class);
		doAnswer((i)->{
			if(robots.length < 8)
				return false;
			else
				return true;
		}).when(hq).AllLandScapers(robots);
		int i = robots.length;
		hq.AllLandScapers(robots);
		verify(hq,times(1)).AllLandScapers(robots);
		assertEquals(hq.AllLandScapers(robots), false);
	}
}
