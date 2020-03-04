package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class UnitTest {

    @Test
    public void testUnitCreation() {
        Unit unit1 = Mockito.mock(Unit.class);
        Unit unit2 = Mockito.mock(Unit.class);
        assertNotEquals(unit1, unit2);
    }


}
