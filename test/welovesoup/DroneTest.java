package welovesoup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

public class DroneTest {

    @Test
    public void testDroneCreation() {
        Drone drone1 = Mockito.mock(Drone.class);
        Drone drone2 = Mockito.mock(Drone.class);
        assertNotEquals(drone1, drone2);
    }

    @Test
    public void testTakeTurn() throws GameActionException {
        Drone d = Mockito.mock(Drone.class);
        d.rc = Mockito.mock(RobotController.class);
        d.X = 0;
        d.Y = 0;
        d.angle = 0;
        d.radians = 0;
        d.robotInfos = new RobotInfo[1];
        when(d.rc.isCurrentlyHoldingUnit()).thenReturn(false);
        doAnswer((i)->{
            if(d.rc.isCurrentlyHoldingUnit()) {
                d.rc.dropUnit(Util.randomDirection());
                return true;
            } else if(d.robotInfos != null && d.robotInfos.length > 0) {
                d.opponentHQ = new MapLocation(2,4);
            } else {
                d.searchForOpponentHQ();
            }
            return d.angle;
        }).when(d).takeTurn();
        d.takeTurn();
        verify(d,times(1)).takeTurn();
        verify(d,times(0)).searchForOpponentHQ();
        d.robotInfos = null;
        assertNotEquals(d.opponentHQ, null);
        d.takeTurn();
        verify(d,times(2)).takeTurn();
        verify(d,times(1)).searchForOpponentHQ();
    }
}
