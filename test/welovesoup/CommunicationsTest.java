package welovesoup;

import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mockito;

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


}
