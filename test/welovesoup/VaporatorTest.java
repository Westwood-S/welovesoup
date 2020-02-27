package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class VaporatorTest {
    @Test
    public void testVaporatorCreation() {
        Vaporator v1 = Mockito.mock(Vaporator.class);
        Vaporator v2 = Mockito.mock(Vaporator.class);
        assertNotEquals(v1, v2);
    }
}
