package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class RefineryTest {

    @Test
    public void testRefineryCreation() {
        Refinery r1 = Mockito.mock(Refinery.class);
        Refinery r2 = Mockito.mock(Refinery.class);
        assertNotEquals(r1, r2);
    }
}
