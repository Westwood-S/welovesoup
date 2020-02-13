package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class CommunicationsTest {

    @Test
    public void testCommunicationsCreation() {
        Communications c1 = Mockito.mock(Communications.class);
        Communications c2 = Mockito.mock(Communications.class);
        assertNotEquals(c1, c2);
    }
}
