package welovesoup;

import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class LandscaperTest {

    @Test
    public void testLandscaperCreation() {
        Landscaper l1 = Mockito.mock(Landscaper.class);
        Landscaper l2 = Mockito.mock(Landscaper.class);
        assertNotEquals(l1, l2);
    }

    @Test
    public void testTryDig() throws GameActionException {
        Landscaper landscaper = Mockito.mock(Landscaper.class);
        landscaper.rc = Mockito.mock(RobotController.class);
        doAnswer((i)->{
            return true;
        }).when(landscaper).tryDig2();
        landscaper.tryDig2();
        verify(landscaper, times(1)).tryDig2();
    }

    @Test
    public void testTryDig_dig() throws GameActionException {
        Landscaper landscaper = Mockito.mock(Landscaper.class);
        landscaper.rc = Mockito.mock(RobotController.class);
        doAnswer((i)->{
            landscaper.rc.digDirt(Direction.CENTER);
            return true;
        }).when(landscaper).tryDig2();
        landscaper.tryDig2();
        verify(landscaper, times(1)).tryDig2();
        verify(landscaper.rc, times(1)).digDirt(Direction.CENTER);
    }

    @Test
    public void testTakeTurn() throws GameActionException {
        Landscaper landscaper = Mockito.mock(Landscaper.class);
        landscaper.rc = Mockito.mock(RobotController.class);
        landscaper.nav = Mockito.mock(Navigation.class);
        doAnswer((i)->{
            landscaper.hqLoc = new MapLocation(1,2);
            landscaper.dirtCarrying = 10;
            landscaper.rc.depositDirt(Direction.CENTER);
            landscaper.nav.goTo(landscaper.hqLoc);
            return true;
        }).when(landscaper).takeTurn();
        landscaper.takeTurn();
        verify(landscaper, times(1)).takeTurn();
    }

    @Test
    public void testTakeTurn_hqloc() throws GameActionException {
        Landscaper landscaper = Mockito.mock(Landscaper.class);
        landscaper.rc = Mockito.mock(RobotController.class);
        landscaper.nav = Mockito.mock(Navigation.class);
        doAnswer((i)->{
            landscaper.hqLoc = new MapLocation(1,2);
            landscaper.dirtCarrying = 10;
            landscaper.rc.depositDirt(Direction.CENTER);
            landscaper.nav.goTo(landscaper.hqLoc);
            return true;
        }).when(landscaper).takeTurn();
        landscaper.takeTurn();
        verify(landscaper, times(1)).takeTurn();
        assertEquals(new MapLocation(1,2).toString(), landscaper.hqLoc.toString());
    }

    @Test
    public void testTakeTurn_depositDirt() throws GameActionException {
        Landscaper landscaper = Mockito.mock(Landscaper.class);
        landscaper.rc = Mockito.mock(RobotController.class);
        landscaper.nav = Mockito.mock(Navigation.class);
        doAnswer((i)->{
            landscaper.hqLoc = new MapLocation(1,2);
            landscaper.dirtCarrying = 10;
            landscaper.rc.depositDirt(Direction.CENTER);
            landscaper.nav.goTo(landscaper.hqLoc);
            return true;
        }).when(landscaper).takeTurn();
        landscaper.takeTurn();
        verify(landscaper, times(1)).takeTurn();
        assertEquals(10, landscaper.dirtCarrying);
        verify(landscaper.rc, times(1)).depositDirt(Direction.CENTER);
        assertEquals(new MapLocation(1,2).toString(), landscaper.hqLoc.toString());
    }

    @Test
    public void testTakeTurn_nav() throws GameActionException {
        Landscaper landscaper = Mockito.mock(Landscaper.class);
        landscaper.rc = Mockito.mock(RobotController.class);
        landscaper.nav = Mockito.mock(Navigation.class);
        doAnswer((i)->{
            landscaper.hqLoc = new MapLocation(1,2);
            landscaper.dirtCarrying = 10;
            landscaper.rc.depositDirt(Direction.CENTER);
            landscaper.nav.goTo(landscaper.hqLoc);
            return true;
        }).when(landscaper).takeTurn();
        landscaper.takeTurn();
        verify(landscaper, times(1)).takeTurn();
        assertEquals(10, landscaper.dirtCarrying);
        verify(landscaper.rc, times(1)).depositDirt(Direction.CENTER);
        verify(landscaper.nav, times(1)).goTo(landscaper.hqLoc);
        assertEquals(new MapLocation(1,2).toString(), landscaper.hqLoc.toString());
    }
}
