package welovesoup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mockito;


public class CowTest {

    @Test
    public void testCowCreation() {
        Cow c1 = Mockito.mock(Cow.class);
        Cow c2 = Mockito.mock(Cow.class);
        assertNotEquals(c1, c2);
    }

    @Test
    public void testTakeTurn() throws GameActionException {
        Cow cow = Mockito.mock(Cow.class);
        doAnswer((i)->{
            return cow.turnCount+=1;
        }).when(cow).takeTurn();
        int i = cow.turnCount;
        cow.takeTurn();
        verify(cow,times(1)).takeTurn();
        assertEquals(cow.turnCount, i+1);
    }
}
