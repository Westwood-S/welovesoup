package welovesoup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

public class DroneTest {

    @Test
    public void testDroneCreation() {
        Drone drone1 = Mockito.mock(Drone.class);
        Drone drone2 = Mockito.mock(Drone.class);
        assertNotEquals(drone1, drone2);
    }


}
