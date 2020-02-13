package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class ShooterTest {
    @Test
    public void testShooterCreation() {
        Shooter s1 = Mockito.mock(Shooter.class);
        Shooter s2 = Mockito.mock(Shooter.class);
        assertNotEquals(s1, s2);
    }
}
