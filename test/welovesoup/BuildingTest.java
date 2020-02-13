package welovesoup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

public class BuildingTest {

    @Test
    public void testBuildingCreation() {
        Building building1 = Mockito.mock(Building.class);
        Building building2 = Mockito.mock(Building.class);
        assertNotEquals(building1, building2);
    }

    @Test
    public void testTakeTurn() throws GameActionException {
        Building building = Mockito.mock(Building.class);
        doAnswer((i)->{
            return building.turnCount+=1;
        }).when(building).takeTurn();
        int i = building.turnCount;
        building.takeTurn();
        verify(building,times(1)).takeTurn();
        assertEquals(building.turnCount, i+1);
    }
}
