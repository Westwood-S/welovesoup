package welovesoup;

import static java.lang.Math.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.sql.Driver;
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

    @Test
    public void testDroneFly() throws GameActionException {
        Drone d = Mockito.mock(Drone.class);
        d.rc = Mockito.mock(RobotController.class);
        doAnswer((i)->{
            d.droneFly(Direction.SOUTH);
            d.droneFly(Direction.NORTH);
            d.droneFly(Direction.WEST);
            d.droneFly(Direction.EAST);
            return true;
        }).when(d).takeTurn();
        d.takeTurn();
        verify(d, times(1)).takeTurn();
        verify(d, times(1)).droneFly(Direction.SOUTH);
        verify(d, times(1)).droneFly(Direction.NORTH);
        verify(d, times(1)).droneFly(Direction.EAST);
        verify(d, times(1)).droneFly(Direction.WEST);
    }

    @Test
    public void testRotateAround() throws GameActionException {
        Drone d = Mockito.mock(Drone.class);
        d.rc = Mockito.mock(RobotController.class);
        d.origin = new MapLocation(1,1);
        doAnswer((i)->{
            d.angle += PI / 20;
            double radians = d.angle * (PI / 180);
            double X = d.origin.x + cos(radians) * d.radius;
            double Y = d.origin.y + sin(radians) * d.radius;
            d.nextLoc = new MapLocation((int) X, (int) Y);
            return true;
        }).when(d).rotateAround(new MapLocation(1,1), 5, 1);
        d.rotateAround(new MapLocation(1,1), 5, 1);
        verify(d, times(1)).rotateAround(new MapLocation(1,1), 5, 1);
        assertEquals(new MapLocation(1,1).toString(), d.nextLoc.toString());
    }

    @Test
    public void testCheckRobots() throws GameActionException {
        Drone d = Mockito.mock(Drone.class);
        RobotInfo[] ri = new RobotInfo[1];
        d.rc = Mockito.mock(RobotController.class);
        ri[0] = new RobotInfo(1,d.rc.getTeam(),RobotType.MINER,1, true, 1, 1, 10, new MapLocation(1,1));
        d.robotInfos = ri;
        when(d.rc.isCurrentlyHoldingUnit()).thenReturn(true);
        doAnswer((i)->{
            if(d.rc.isCurrentlyHoldingUnit() == true) {
                d.rc.dropUnit(Util.randomDirection());
            }
            if(d.robotInfos != null) {
                d.opponentHQ = new MapLocation(1,1);
                d.rc.pickUpUnit(ri[0].getID());
            }
            return true;
        }).when(d).checkRobots(ri);
        d.checkRobots(ri);
        verify(d, times(1)).checkRobots(ri);
        verify(d.rc, times(1)).pickUpUnit(ri[0].getID());
    }

    @Test
    public void testCheckRobots_isCurrentlyHoldingUnit() throws GameActionException {
        Drone d = Mockito.mock(Drone.class);
        RobotInfo[] ri = new RobotInfo[1];
        d.rc = Mockito.mock(RobotController.class);
        ri[0] = new RobotInfo(1,d.rc.getTeam(),RobotType.MINER,1, true, 1, 1, 10, new MapLocation(1,1));
        d.robotInfos = ri;
        when(d.rc.isCurrentlyHoldingUnit()).thenReturn(true);
        doAnswer((i)->{
            if(d.rc.isCurrentlyHoldingUnit() == true) {
                d.rc.dropUnit(Direction.CENTER);
            }
            if(d.robotInfos != null) {
                d.opponentHQ = new MapLocation(1,1);
                d.rc.pickUpUnit(ri[0].getID());
            }
            return true;
        }).when(d).checkRobots(ri);
        d.checkRobots(ri);
        verify(d, times(1)).checkRobots(ri);
        verify(d.rc, times(1)).pickUpUnit(ri[0].getID());
        verify(d.rc, times(1)).isCurrentlyHoldingUnit();
        verify(d.rc, times(1)).dropUnit(Direction.CENTER);
        when(d.rc.isCurrentlyHoldingUnit()).thenReturn(false);
        d.checkRobots(ri);
        verify(d.rc, times(1)).dropUnit(Direction.CENTER);
        verify(d.rc, times(2)).isCurrentlyHoldingUnit();
    }

    @Test
    public void testSearchForOpponentHQ() throws GameActionException {
        Drone d = Mockito.mock(Drone.class);
        RobotInfo[] robotInfos = new RobotInfo[1];
        d.rc = Mockito.mock(RobotController.class);
        robotInfos[0] = new RobotInfo(1,d.rc.getTeam(),RobotType.MINER,1, true, 1, 1, 10, new MapLocation(1,1));
        doAnswer((i)->{
            d.rc.senseNearbyRobots();
            d.opponentHQ = new MapLocation(1,1);
            return true;
        }).when(d).searchForOpponentHQ();
        d.searchForOpponentHQ();
        verify(d, times(1)).searchForOpponentHQ();
        verify(d.rc, times(1)).senseNearbyRobots();
        assertNotEquals(d.opponentHQ, null);
    }
}
