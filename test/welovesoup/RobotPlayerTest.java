package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class RobotPlayerTest {
    @Test
    public void testRobotPlayerCreation() {
        RobotPlayer r1 = Mockito.mock(RobotPlayer.class);
        RobotPlayer r2 = Mockito.mock(RobotPlayer.class);
        assertNotEquals(r1, r2);
    }
}
