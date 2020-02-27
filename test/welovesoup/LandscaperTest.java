package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class LandscaperTest {

    @Test
    public void testLandscaperCreation() {
        Landscaper l1 = Mockito.mock(Landscaper.class);
        Landscaper l2 = Mockito.mock(Landscaper.class);
        assertNotEquals(l1, l2);
    }
}
