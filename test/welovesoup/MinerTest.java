package welovesoup;

import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class MinerTest {

    @Test
    public void testMinerCreation() {
        Miner m1 = Mockito.mock(Miner.class);
        Miner m2 = Mockito.mock(Miner.class);
        assertNotEquals(m1, m2);
    }

    @Test
    public void testNewMove() throws GameActionException {
        Miner m = Mockito.mock(Miner.class);
        m.rc = Mockito.mock(RobotController.class);
        m.nav = Mockito.mock(Navigation.class);
        doAnswer((i)->{
            m.hqLoc = new MapLocation(1,2);
            m.rc.depositDirt(Direction.CENTER);
            m.nav.goTo(m.hqLoc);
            m.nav.goTo(new MapLocation(2,2));
            return true;
        }).when(m).newMove();
        m.newMove();
        verify(m, times(1)).newMove();
        verify(m.rc, times(1)).depositDirt(Direction.CENTER);
        verify(m.nav, times(1)).goTo(new MapLocation(2,2));
        assertEquals(new MapLocation(1,2).toString(), m.hqLoc.toString());
    }

    @Test
    public void testBuild() throws GameActionException {
        Miner m = Mockito.mock(Miner.class);
        m.rc = Mockito.mock(RobotController.class);
        m.nav = Mockito.mock(Navigation.class);
        doAnswer((i)->{
            m.hqLoc = new MapLocation(1,2);
            m.rc.depositDirt(Direction.CENTER);
            m.nav.goTo(m.hqLoc);
            m.nav.goTo(new MapLocation(2,2));
            return true;
        }).when(m).build(RobotType.REFINERY);
        m.build(RobotType.REFINERY);
        verify(m.rc, times(1)).depositDirt(Direction.CENTER);
        verify(m.nav, times(1)).goTo(new MapLocation(2,2));
        verify(m, times(1)).build(RobotType.REFINERY);
        assertEquals(new MapLocation(1,2).toString(), m.hqLoc.toString());
    }

    @Test
    public void testBuild_FullfillmentCenter() throws GameActionException {
        Miner m = Mockito.mock(Miner.class);
        m.rc = Mockito.mock(RobotController.class);
        m.nav = Mockito.mock(Navigation.class);
        doAnswer((i)->{
            m.hqLoc = new MapLocation(1,2);
            m.rc.depositDirt(Direction.CENTER);
            m.nav.goTo(m.hqLoc);
            m.nav.goTo(new MapLocation(2,2));
            return true;
        }).when(m).build(RobotType.FULFILLMENT_CENTER);
        m.build(RobotType.FULFILLMENT_CENTER);
        verify(m.rc, times(1)).depositDirt(Direction.CENTER);
        verify(m.nav, times(1)).goTo(new MapLocation(2,2));
        verify(m, times(1)).build(RobotType.FULFILLMENT_CENTER);
        assertEquals(new MapLocation(1,2).toString(), m.hqLoc.toString());
    }

    @Test
    public void testBuild_DesignSchool() throws GameActionException {
        Miner m = Mockito.mock(Miner.class);
        m.rc = Mockito.mock(RobotController.class);
        m.nav = Mockito.mock(Navigation.class);
        doAnswer((i)->{
            m.hqLoc = new MapLocation(1,2);
            m.rc.depositDirt(Direction.CENTER);
            m.nav.goTo(m.hqLoc);
            m.nav.goTo(new MapLocation(2,2));
            return true;
        }).when(m).build(RobotType.DESIGN_SCHOOL);
        m.build(RobotType.DESIGN_SCHOOL);
        verify(m.rc, times(1)).depositDirt(Direction.CENTER);
        verify(m.nav, times(1)).goTo(new MapLocation(2,2));
        verify(m, times(1)).build(RobotType.DESIGN_SCHOOL);
        assertEquals(new MapLocation(1,2).toString(), m.hqLoc.toString());
    }

    @Test
    public void testBuild_Vaporator() throws GameActionException {
        Miner m = Mockito.mock(Miner.class);
        m.rc = Mockito.mock(RobotController.class);
        m.nav = Mockito.mock(Navigation.class);
        doAnswer((i)->{
            m.hqLoc = new MapLocation(1,2);
            m.rc.depositDirt(Direction.CENTER);
            m.nav.goTo(m.hqLoc);
            m.nav.goTo(new MapLocation(2,2));
            return true;
        }).when(m).build(RobotType.VAPORATOR);
        m.build(RobotType.VAPORATOR);
        verify(m.rc, times(1)).depositDirt(Direction.CENTER);
        verify(m.nav, times(1)).goTo(new MapLocation(2,2));
        verify(m, times(1)).build(RobotType.VAPORATOR);
        assertEquals(new MapLocation(1,2).toString(), m.hqLoc.toString());
    }

    @Test
    public void testBuild_netgun() throws GameActionException {
        Miner m = Mockito.mock(Miner.class);
        m.rc = Mockito.mock(RobotController.class);
        m.nav = Mockito.mock(Navigation.class);
        doAnswer((i)->{
            m.hqLoc = new MapLocation(1,2);
            m.rc.depositDirt(Direction.CENTER);
            m.nav.goTo(m.hqLoc);
            m.nav.goTo(new MapLocation(2,2));
            return true;
        }).when(m).build(RobotType.NET_GUN);
        m.build(RobotType.NET_GUN);
        verify(m.rc, times(1)).depositDirt(Direction.CENTER);
        verify(m.nav, times(1)).goTo(new MapLocation(2,2));
        verify(m, times(1)).build(RobotType.NET_GUN);
        assertEquals(new MapLocation(1,2).toString(), m.hqLoc.toString());
    }

    @Test
    public void testCheckRefny() throws GameActionException {
        Miner m = Mockito.mock(Miner.class);
        m.rc = Mockito.mock(RobotController.class);
        m.nav = Mockito.mock(Navigation.class);
        m.refineryLocations = new ArrayList<>(1);
        m.refineryLocations.add(new MapLocation(1,1));
        doAnswer((i)->{
            m.refineryLocations.remove(0);
            m.hqLoc = new MapLocation(1,2);
            m.rc.depositDirt(Direction.CENTER);
            m.nav.goTo(m.hqLoc);
            m.nav.goTo(new MapLocation(2,2));
            return true;
        }).when(m).checkRefny();
        m.checkRefny();
        verify(m.rc, times(1)).depositDirt(Direction.CENTER);
        verify(m.nav, times(1)).goTo(new MapLocation(2,2));
        verify(m, times(1)).checkRefny();
        assertEquals(new MapLocation(1,2).toString(), m.hqLoc.toString());
        assertEquals(0, m.refineryLocations.size());
    }

    @Test
    public void testCheckSoup() throws GameActionException {
        Miner m = Mockito.mock(Miner.class);
        m.rc = Mockito.mock(RobotController.class);
        m.nav = Mockito.mock(Navigation.class);
        m.soupLocations = new ArrayList<>(1);
        m.soupLocations.add(new MapLocation(1,1));
        doAnswer((i)->{
            m.soupLocations.remove(0);
            m.hqLoc = new MapLocation(1,2);
            m.rc.depositDirt(Direction.CENTER);
            m.nav.goTo(m.hqLoc);
            m.nav.goTo(new MapLocation(2,2));
            return true;
        }).when(m).checkRefny();
        m.checkRefny();
        verify(m.rc, times(1)).depositDirt(Direction.CENTER);
        verify(m.nav, times(1)).goTo(new MapLocation(2,2));
        verify(m, times(1)).checkRefny();
        assertEquals(new MapLocation(1,2).toString(), m.hqLoc.toString());
        assertEquals(0, m.soupLocations.size());
    }

    @Test
    public void testTryRefine() throws GameActionException {
        Miner m = Mockito.mock(Miner.class);
        m.rc = Mockito.mock(RobotController.class);
        m.nav = Mockito.mock(Navigation.class);
        m.soupLocations = new ArrayList<>(1);
        m.soupLocations.add(new MapLocation(1,1));
        doAnswer((i)->{
            m.soupLocations.remove(0);
            m.hqLoc = new MapLocation(1,2);
            m.rc.depositDirt(Direction.CENTER);
            m.nav.goTo(m.hqLoc);
            m.nav.goTo(new MapLocation(2,2));
            m.rc.depositSoup(Direction.CENTER, 1);
            return true;
        }).when(m).tryRefine(Direction.CENTER);
        m.tryRefine(Direction.CENTER);
        verify(m.rc, times(1)).depositDirt(Direction.CENTER);
        verify(m.rc, times(1)).depositSoup(Direction.CENTER, 1);
        verify(m.nav, times(1)).goTo(new MapLocation(2,2));
        verify(m, times(1)).tryRefine(Direction.CENTER);
        assertEquals(new MapLocation(1,2).toString(), m.hqLoc.toString());
        assertEquals(0, m.soupLocations.size());
    }
}
