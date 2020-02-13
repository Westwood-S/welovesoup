package welovesoup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mockito;

public class DesignSchoolTest {

    @Test
    public void testDesignSchoolCreation() {
        DesignSchool ds1 = Mockito.mock(DesignSchool.class);
        DesignSchool ds2 = Mockito.mock(DesignSchool.class);
        assertNotEquals(ds1, ds2);
    }

    @Test
    public void testTakeTurn() throws GameActionException {
        DesignSchool ds = Mockito.mock(DesignSchool.class);
        doAnswer((i)->{
            return ds.turnCount+=1;
        }).when(ds).takeTurn();
        int i = ds.turnCount;
        ds.takeTurn();
        verify(ds,times(1)).takeTurn();
        assertEquals(ds.turnCount, i+1);
    }
}
