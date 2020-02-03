package welovesoup;

import static org.junit.Assert.*;

import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class BuildingTest {

    @Test
    public void testBuildingCreation() {
        Building building1 = Mockito.mock(Building.class);
        Building building2 = Mockito.mock(Building.class);
        assertNotEquals(building1, building2);
    }
}
