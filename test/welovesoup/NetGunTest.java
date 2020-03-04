package welovesoup;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class NetGunTest {

    @Test
    public void testNetGunCreation() {
        netGun ng1 = Mockito.mock(netGun.class);
        netGun ng2 = Mockito.mock(netGun.class);
        assertNotEquals(ng1, ng2);
    }

    @Test
    public void testTakeTurn() throws GameActionException {
        netGun n = Mockito.mock(netGun.class);
        n.rc = Mockito.mock(RobotController.class);
        doAnswer((i)->{
            return true;
        }).when(n).takeTurn();
        n.takeTurn();
        verify(n, times(1)).takeTurn();
    }
}
