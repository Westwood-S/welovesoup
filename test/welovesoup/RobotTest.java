package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class RobotTest {

    @Test
    public void testRobotCreation() {
        Robot r1 = Mockito.mock(Robot.class);
        Robot r2 = Mockito.mock(Robot.class);
        assertNotEquals(r1, r2);
    }
}
