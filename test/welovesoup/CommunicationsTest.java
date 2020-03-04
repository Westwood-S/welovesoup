package welovesoup;

import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class CommunicationsTest {

    @Test
    public void testCommunicationsCreation() {
        Communications c1 = Mockito.mock(Communications.class);
        Communications c2 = Mockito.mock(Communications.class);
        assertNotEquals(c1, c2);
    }

    @Test
    public void testTeamSecreat() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);

        doAnswer((i)->{
            c.rc.submitTransaction(new int[7],1);
            return true;
        }).when(c).sendHqLoc(new MapLocation(1,1));
        c.sendHqLoc(new MapLocation(1,1));
        verify(c, times(1)).sendHqLoc(new MapLocation(1,1));
        verify(c.rc, times(1)).submitTransaction(new int[7], 1);
        assertEquals(163898578, c.teamSecret);
    }


    @Test
    public void testMessageType_HQ() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        System.out.println(c.messageType[0]);
        assertEquals(163898578, c.teamSecret);
        assertEquals("HQ loc", c.messageType[0]);
    }

    @Test
    public void testMessageType_DesignSchool() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        assertEquals(163898578, c.teamSecret);
        assertEquals("design school created", c.messageType[1]);
    }

    @Test
    public void testMessageType_SoupLocation() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        assertEquals(163898578, c.teamSecret);
        assertEquals("soup location", c.messageType[2]);
    }

    @Test
    public void testMessageType_Refinery() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        assertEquals(163898578, c.teamSecret);
        assertEquals("refinery created", c.messageType[3]);
    }

    @Test
    public void testMessageType_Fullfillment() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        assertEquals(163898578, c.teamSecret);
        assertEquals("fullfillment center created", c.messageType[4]);
    }

    @Test
    public void testMessageType_Vaporator() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        assertEquals(163898578, c.teamSecret);
        assertEquals("vaporator created", c.messageType[5]);
    }

    @Test
    public void testMessageType_NotSorrounded() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        assertEquals(163898578, c.teamSecret);
        assertEquals("Not sorrounded", c.messageType[6]);
    }

    @Test
    public void testMessageType_HasEnemy() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        assertEquals(163898578, c.teamSecret);
        assertEquals("Has enemy", c.messageType[7]);
    }

    @Test
    public void testFilfillmentcreationbroadcastedcreation() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        assertEquals(163898578, c.teamSecret);
        assertEquals(false, c.filfillmentcreationbroadcastedcreation);
    }

    @Test
    public void testSendHqLoc() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        doAnswer((i)->{
            c.rc.submitTransaction(new int[7],1);
            return true;
        }).when(c).sendHqLoc(new MapLocation(1,1));
        c.sendHqLoc(new MapLocation(1,1));
        verify(c, times(1)).sendHqLoc(new MapLocation(1,1));
        verify(c.rc, times(1)).submitTransaction(new int[7], 1);
    }

    @Test
    public void testGetHqLocFromBlockchain() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        when(c.rc.getRoundNum()).thenReturn(2);
        Transaction t = new Transaction(3,new int[]{2,3,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(new int[]{2,3,4,4,5,5,5},1);
            return new MapLocation(1,1);
        }).when(c).getHqLocFromBlockchain();
        c.getHqLocFromBlockchain();
        verify(c, times(1)).getHqLocFromBlockchain();
        verify(c.rc, times(1)).submitTransaction(new int[]{2,3,4,4,5,5,5}, 1);
        assertEquals(3, c.rc.getBlock(1)[0].getMessage()[1]);
    }

    @Test
    public void testBroadcastDesignSchoolCreation() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{2,1,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(new int[]{2,1,4,4,5,5,5},3);
            return new MapLocation(1,1);
        }).when(c).getHqLocFromBlockchain();
        c.getHqLocFromBlockchain();
        verify(c, times(1)).getHqLocFromBlockchain();
        verify(c.rc, times(1)).submitTransaction(new int[]{2,1,4,4,5,5,5}, 3);
        assertEquals(1, c.rc.getBlock(1)[0].getMessage()[1]);
    }

    @Test
    public void testGetNewDesignSchoolCount() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{2,1,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        assertEquals(1, c.rc.getBlock(1)[0].getMessage()[1]);
    }

    @Test
    public void testBroadcastSoupLocation() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,2,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return true;
        }).when(c).broadcastSoupLocation(new MapLocation(1,1));
        c.broadcastSoupLocation(new MapLocation(1,1));
        verify(c, times(1)).broadcastSoupLocation(new MapLocation(1,1));
        assertEquals(2, c.rc.getBlock(1)[0].getMessage()[1]);
    }

    @Test
    public void testUpdateSoupLocations() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,2,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return true;
        }).when(c).broadcastSoupLocation(new MapLocation(1,1));
        c.updateSoupLocations(new ArrayList<>());
        verify(c, times(1)).updateSoupLocations(new ArrayList<>());
        assertEquals(2, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testBroadcastRefnyLocation() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,3,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return true;
        }).when(c).broadcastRefnyLocation(new MapLocation(1,1));
        c.broadcastRefnyLocation(new MapLocation(1,1));
        verify(c, times(1)).broadcastRefnyLocation(new MapLocation(1,1));
        assertEquals(3, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testUpdateRefnyLocations() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,3,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return true;
        }).when(c).broadcastRefnyLocation(new MapLocation(1,1));
        c.broadcastRefnyLocation(new MapLocation(1,1));
        verify(c, times(1)).broadcastRefnyLocation(new MapLocation(1,1));
        assertEquals(3, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testBroadcastFulfillmentCenterCreation() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,4,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return true;
        }).when(c).broadcastFulfillmentCenterCreation(new MapLocation(1,1));
        c.broadcastFulfillmentCenterCreation(new MapLocation(1,1));
        verify(c, times(1)).broadcastFulfillmentCenterCreation(new MapLocation(1,1));
        assertEquals(4, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testUpdateFFCCreation() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,4,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return true;
        }).when(c).updateFFCCreation(new ArrayList<>());
        c.updateFFCCreation(new ArrayList<>());
        verify(c, times(1)).updateFFCCreation(new ArrayList<>());
        assertEquals(4, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testGetNewFulfillmentCenterCount() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,4,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return 1;
        }).when(c).getNewFulfillmentCenterCount();
        c.getNewFulfillmentCenterCount();
        verify(c, times(1)).getNewFulfillmentCenterCount();
        assertEquals(4, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testBroadcastVaporatorLocation() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,5,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return 1;
        }).when(c).broadcastVaporatorLocation(new MapLocation(1,1));
        c.broadcastVaporatorLocation(new MapLocation(1,1));
        verify(c, times(1)).broadcastVaporatorLocation(new MapLocation(1,1));
        assertEquals(5, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testUpdateVaporatorLocations() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,5,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return 1;
        }).when(c).updateVaporatorLocations(new ArrayList<>());
        c.updateVaporatorLocations(new ArrayList<>());
        verify(c, times(1)).updateVaporatorLocations(new ArrayList<>());
        assertEquals(5, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testBroadcastNotSurrounded() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,6,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return 1;
        }).when(c).broadcastNotSurrounded();
        c.broadcastNotSurrounded();
        verify(c, times(1)).broadcastNotSurrounded();
        assertEquals(6, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testUpdateSurrounded() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,6,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return 1;
        }).when(c).broadcastNotSurrounded();
        c.broadcastNotSurrounded();
        verify(c, times(1)).broadcastNotSurrounded();
        assertEquals(6, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testBroadcastHasEnemy() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,7,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return 1;
        }).when(c).broadcastHasEnemy();
        c.broadcastHasEnemy();
        verify(c, times(1)).broadcastHasEnemy();
        assertEquals(7, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testUpdateHasEnemy() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,7,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return 1;
        }).when(c).updateHasEnemy();
        c.updateHasEnemy();
        verify(c, times(1)).updateHasEnemy();
        assertEquals(7, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testBroadcastDigLocations() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,7,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return true;
        }).when(c).broadcastDigLocations(new ArrayList<>());
        c.broadcastDigLocations(new ArrayList<>());
        verify(c, times(1)).broadcastDigLocations(new ArrayList<>());
        assertEquals(7, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }

    @Test
    public void testGetDigLocations() throws GameActionException {
        Communications c = Mockito.mock(Communications.class);
        c.rc = Mockito.mock(RobotController.class);
        Transaction t = new Transaction(3,new int[]{c.teamSecret,7,4,4,5,5,5}, 4);
        Transaction[] transactions = new Transaction[1];
        transactions[0] = t;
        when(c.rc.getBlock(1)).thenReturn(transactions);
        doAnswer((i)->{
            c.rc.submitTransaction(transactions[0].getMessage(), 3);
            return true;
        }).when(c).getDigLocations(new ArrayList<>());
        c.getDigLocations(new ArrayList<>());
        verify(c, times(1)).getDigLocations(new ArrayList<>());
        assertEquals(7, c.rc.getBlock(1)[0].getMessage()[1]);
        assertEquals(163898578, c.rc.getBlock(1)[0].getMessage()[0]);
    }
}
