package welovesoup;

import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class FulfillmentCenterTest {

    @Test
    public void testFulfillmentCenterCreation() {
        FulfillmentCenter fc1 = Mockito.mock(FulfillmentCenter.class);
        FulfillmentCenter fc2 = Mockito.mock(FulfillmentCenter.class);
        assertNotEquals(fc1, fc2);
    }

    @Test
    public void testTakeTurn() throws GameActionException {
        FulfillmentCenter fc = Mockito.mock(FulfillmentCenter.class);
        fc.rc = Mockito.mock(RobotController.class);
        fc.numDrone = 0;
        when(fc.tryBuild(RobotType.DELIVERY_DRONE, Direction.CENTER)).thenReturn(true);
        doAnswer((i)->{
            while (fc.numDrone < 3) {
                when(fc.tryBuild(RobotType.DELIVERY_DRONE, Direction.CENTER)).thenReturn(true);
                fc.tryBuild(RobotType.DELIVERY_DRONE, Direction.CENTER);
                fc.numDrone += 1;
            }
            return true;
        }).when(fc).takeTurn();
        fc.takeTurn();
        verify(fc, times(1)).takeTurn();
        verify(fc, times(3)).tryBuild(RobotType.DELIVERY_DRONE, Direction.CENTER);
        assertEquals(3, fc.numDrone);
    }

    @Test
    public void testTakeTurn_doNothing() throws GameActionException {
        FulfillmentCenter fc = Mockito.mock(FulfillmentCenter.class);
        fc.rc = Mockito.mock(RobotController.class);
        fc.numDrone = 6;
        when(fc.tryBuild(RobotType.DELIVERY_DRONE, Direction.CENTER)).thenReturn(true);
        doAnswer((i)->{
            while (fc.numDrone < 3) {
                fc.tryBuild(RobotType.DELIVERY_DRONE, Direction.CENTER);
                fc.numDrone += 1;
            }
            return true;
        }).when(fc).takeTurn();
        fc.takeTurn();
        verify(fc, times(1)).takeTurn();
        verify(fc, times(0)).tryBuild(RobotType.DELIVERY_DRONE, Direction.CENTER);
        assertEquals(6, fc.numDrone);
    }
}
