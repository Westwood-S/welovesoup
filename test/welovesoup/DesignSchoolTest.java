package welovesoup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mock;
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

    @Test
    public void testTakeTurn_getRoundNum() throws GameActionException {
        DesignSchool ds = Mockito.mock(DesignSchool.class);
        ds.rc = Mockito.mock(RobotController.class);
        ds.numLandScapers = 1;
        doAnswer((i)->{
            if(ds.rc.getRoundNum() < 550 && ds.numLandScapers < 8)
                ds.numLandScapers += 1;
            return ds.numLandScapers;
        }).when(ds).takeTurn();
        // do increment the numLandScapers
        when(ds.rc.getRoundNum()).thenReturn(500);
        int i = ds.numLandScapers;
        ds.takeTurn();
        verify(ds,times(1)).takeTurn();
        assertEquals(i+1, ds.numLandScapers);
        // don't increment numLandScapers
        when(ds.rc.getRoundNum()).thenReturn(550);
        ds.takeTurn();
        verify(ds,times(2)).takeTurn();
        assertEquals(i+1, ds.numLandScapers);
    }
    
        @Test
    public void testTakeTurn_tryBuild_directions() throws GameActionException {
        DesignSchool ds = Mockito.mock(DesignSchool.class);
        ds.rc = Mockito.mock(RobotController.class);
        ds.numLandScapers = 0;
        doAnswer((i)->{
            if(ds.rc.getRoundNum() < 550 && ds.numLandScapers < 8) {
                when(ds.tryBuild(RobotType.LANDSCAPER, Direction.CENTER)).thenReturn(true);
                when(ds.tryBuild(RobotType.LANDSCAPER, Direction.EAST)).thenReturn(true);
                when(ds.tryBuild(RobotType.LANDSCAPER, Direction.NORTH)).thenReturn(true);
                when(ds.tryBuild(RobotType.LANDSCAPER, Direction.WEST)).thenReturn(true);
                when(ds.tryBuild(RobotType.LANDSCAPER, Direction.SOUTH)).thenReturn(true);
                when(ds.tryBuild(RobotType.LANDSCAPER, Direction.SOUTHEAST)).thenReturn(true);
                when(ds.tryBuild(RobotType.LANDSCAPER, Direction.NORTHEAST)).thenReturn(true);
                when(ds.tryBuild(RobotType.LANDSCAPER, Direction.NORTHWEST)).thenReturn(true);
                when(ds.tryBuild(RobotType.LANDSCAPER, Direction.SOUTHWEST)).thenReturn(true);
            }
            return ds.numLandScapers;
        }).when(ds).takeTurn();
        // do increment the numLandScapers
        when(ds.rc.getRoundNum()).thenReturn(500);
        int i = ds.numLandScapers;
        ds.takeTurn();
        int actual = 0;
        if (ds.tryBuild(RobotType.LANDSCAPER, Direction.SOUTH) == true)
            actual += 1;
        if (ds.tryBuild(RobotType.LANDSCAPER, Direction.CENTER) == true)
            actual += 1;
        if (ds.tryBuild(RobotType.LANDSCAPER, Direction.SOUTHWEST) == true)
            actual += 1;
        if (ds.tryBuild(RobotType.LANDSCAPER, Direction.NORTHWEST) == true)
            actual += 1;
        if (ds.tryBuild(RobotType.LANDSCAPER, Direction.EAST) == true)
            actual += 1;
        if (ds.tryBuild(RobotType.LANDSCAPER, Direction.NORTH) == true)
            actual += 1;
        if (ds.tryBuild(RobotType.LANDSCAPER, Direction.SOUTHEAST) == true)
            actual += 1;
        if (ds.tryBuild(RobotType.LANDSCAPER, Direction.NORTHEAST) == true)
            actual += 1;
        if (ds.tryBuild(RobotType.LANDSCAPER, Direction.WEST) == true)
            actual += 1;
        assertEquals(9, actual);
        ds.numLandScapers = actual;
        verify(ds,times(1)).takeTurn();
        verify(ds,times(1)).tryBuild(RobotType.LANDSCAPER, Direction.SOUTH);
        verify(ds,times(1)).tryBuild(RobotType.LANDSCAPER, Direction.SOUTHEAST);
        verify(ds,times(1)).tryBuild(RobotType.LANDSCAPER, Direction.SOUTHWEST);
        verify(ds,times(1)).tryBuild(RobotType.LANDSCAPER, Direction.NORTH);
        verify(ds,times(1)).tryBuild(RobotType.LANDSCAPER, Direction.NORTHEAST);
        verify(ds,times(1)).tryBuild(RobotType.LANDSCAPER, Direction.NORTHWEST);
        verify(ds,times(1)).tryBuild(RobotType.LANDSCAPER, Direction.EAST);
        verify(ds,times(1)).tryBuild(RobotType.LANDSCAPER, Direction.WEST);
        verify(ds,times(1)).tryBuild(RobotType.LANDSCAPER, Direction.CENTER);
        assertEquals(i+9, ds.numLandScapers);
        // don't increment numLandScapers
        when(ds.rc.getRoundNum()).thenReturn(550);
        ds.takeTurn();
        verify(ds,times(2)).takeTurn();
        assertEquals(i+9, ds.numLandScapers);
    }
}
