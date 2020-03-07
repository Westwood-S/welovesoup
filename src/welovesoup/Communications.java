package welovesoup;
import battlecode.common.*;
import java.util.ArrayList;
import java.util.Map;

public class Communications {
    RobotController rc;

    public boolean filfillmentcreationbroadcastedcreation = false;
    // state related only to communications should go here

    // all messages from our team should start with this so we can tell them apart
    static final int teamSecret = 163898578;
    // the second entry in every message tells us what kind of message it is. e.g. 0 means it contains the HQ location
    static final String[] messageType = {                   // message[n] ==
        "HQ loc",                                           // 0
        "design school created",                            // 1
        "soup location",                                    // 2
        "refinery created",                                 // 3
        "fullfillment center created",                      // 4
        "vaporator created",                                // 5
        "Not sorrounded",                                   // 6
        "Has enemy"                                         // 7 
    };

    public Communications(RobotController r) {
        rc = r;
    }

    public void sendHqLoc(MapLocation loc) throws GameActionException {
        int[] message = new int[7];
        message[0] = teamSecret;
        message[1] = 0;
        message[2] = loc.x; // x coord of HQ
        message[3] = loc.y; // y coord of HQ
        if (rc.canSubmitTransaction(message, 1))
            rc.submitTransaction(message, 1);
    }

    public MapLocation getHqLocFromBlockchain() throws GameActionException {
        for (int i = 1; i < rc.getRoundNum(); i++){
            for(Transaction tx : rc.getBlock(i)) {
                int[] mess = tx.getMessage();
                if(mess[0] == teamSecret && mess[1] == 0){
                    System.out.println("found the HQ!");
                    return new MapLocation(mess[2], mess[3]);
                }
            }
        }
        return null;
    }

    public boolean broadcastedCreation = false;
    public void broadcastDesignSchoolCreation(MapLocation loc) throws GameActionException {
        if(broadcastedCreation) return; // don't re-broadcast

        int[] message = new int[7];
        message[0] = teamSecret;
        message[1] = 1;
        message[2] = loc.x; // x coord of HQ
        message[3] = loc.y; // y coord of HQ
        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
            broadcastedCreation = true;
        }
    }


    // check the latest block for unit creation messages
    public int getNewDesignSchoolCount() throws GameActionException {
        int count = 0;
        for(Transaction tx : rc.getBlock(rc.getRoundNum() - 1)) {
            int[] mess = tx.getMessage();
            if(mess[0] == teamSecret && mess[1] == 1){
                System.out.println("heard about a cool new school");
                count += 1;
            }
        }
        return count;
    }

    
    public void broadcastSoupLocation(MapLocation loc ) throws GameActionException {
        int[] message = new int[7];
        message[0] = teamSecret;
        message[1] = 2;
        message[2] = loc.x; // x coord of HQ
        message[3] = loc.y; // y coord of HQ
        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
            System.out.println("new soup!" + loc);
        }
    }

    public void updateSoupLocations(ArrayList<MapLocation> soupLocations) throws GameActionException {
        for(Transaction tx : rc.getBlock(rc.getRoundNum() - 1)) {
            int[] mess = tx.getMessage();
            if(mess[0] == teamSecret && mess[1] == 2){
                // TODO: don't add duplicate locations
                System.out.println("heard about a tasty new soup location");
                soupLocations.add(new MapLocation(mess[2], mess[3]));
            }
        }
    }

    public void broadcastRefnyLocation(MapLocation loc) throws GameActionException {
        int[] message = new int[7];
        message[0] = teamSecret;
        message[1] = 3;
        message[2] = loc.x; // x coord of HQ
        message[3] = loc.y; // y coord of HQ
        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
            System.out.println("new refinery!" + loc);
        }
    }

    public void updateRefnyLocations(ArrayList<MapLocation> refnyLocations) throws GameActionException {
        for(Transaction tx : rc.getBlock(rc.getRoundNum() - 1)) {
            int[] mess = tx.getMessage();
            if(mess[0] == teamSecret && mess[1] == 3){
                System.out.println("heard about a powerful new refinery");
                refnyLocations.add(new MapLocation(mess[2], mess[3]));
            }
        }
    }

    public void broadcastFulfillmentCenterCreation(MapLocation loc) throws GameActionException {
        //if(filfillmentcreationbroadcastedcreation) return; // don't re-broadcast
        int[] message = new int[7];
        message[0] = teamSecret;
        message[1] = 4;
        message[2] = loc.x; // x coord of HQ
        message[3] = loc.y; // y coord of HQ
        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
            System.out.println("new fulfillment center!" + loc);
            filfillmentcreationbroadcastedcreation = true;
        }
    }

    public void updateFFCCreation(ArrayList<MapLocation> FFCLoc) throws GameActionException{
        for(Transaction tx: rc.getBlock(rc.getRoundNum() - 1)){
            int[] mess = tx.getMessage();
            if(mess[0] == teamSecret && mess[1] == 4){
                System.out.println("New FFC!!!!");
                FFCLoc.add(new MapLocation(mess[2], mess[3]));
            }
        }
    }

    public int getNewFulfillmentCenterCount() throws GameActionException {
        int count = 0;
        for(Transaction tx : rc.getBlock(rc.getRoundNum() - 1)) {
            int[] mess = tx.getMessage();
            if(mess[0] == teamSecret && mess[1] == 4) {
                System.out.println("heard about a cool new fulfillment center");
                count += 1;
            }
        }
        return count;
    }

    public void broadcastVaporatorLocation(MapLocation loc) throws GameActionException {
        int[] message = new int[7];
        message[0] = teamSecret;
        message[1] = 5;
        message[2] = loc.x; // x coord of HQ
        message[3] = loc.y; // y coord of HQ
        if (rc.canSubmitTransaction(message, 5)) {
            rc.submitTransaction(message, 5);
            System.out.println("new Vaporator!" + loc);
        }
    }

    public void updateVaporatorLocations(ArrayList<MapLocation> vaporatorLocations) throws GameActionException{
        for(Transaction tx: rc.getBlock(rc.getRoundNum() - 1)){
            int[] mess = tx.getMessage();
            if(mess[0] == teamSecret && mess[1] == 5){
                System.out.println("New Vaporator!!!!");
                vaporatorLocations.add(new MapLocation(mess[2], mess[3]));
            }
        }
    }

    public void broadcastNotSurrounded() throws GameActionException{
        int[] message = new int[7];
        message[0] = teamSecret;
        message[1] = 6;
        if(rc.canSubmitTransaction(message, 6)){
            rc.submitTransaction(message, 6);
            System.out.println("Not sorrounded!!");
        }
    }

    public int updateSurrounded() throws GameActionException {
        for (Transaction tx : rc.getBlock(rc.getRoundNum() - 1)) {
            int[] mess = tx.getMessage();
            if(mess == null) return -1;
            if (mess[0] == teamSecret && mess[1] == 6) {
                System.out.println("NOT SORROUNDED");
                return 0;
            }
        }
        return 1;
    }

    public void broadcastHasEnemy() throws GameActionException{
        int[] message = new int[7];
        message[0] = teamSecret;
        message[1] = 7;
        if(rc.canSubmitTransaction(message, 3)){
            rc.submitTransaction(message, 3);
            System.out.println("HELP OMG!!!");
        }
    }

    public int updateHasEnemy() throws GameActionException {
        for (Transaction tx : rc.getBlock(rc.getRoundNum() - 1)) {
            int[] mess = tx.getMessage();
            if (mess == null) return -1;
            if (mess[0] == teamSecret && mess[1] == 7) {
                System.out.println("WHY EVERYBODY RUSH");
                return 1;
            }
        }
        return 0;
    }

    public boolean broadcastDigLocations(ArrayList<MapLocation> locations) throws GameActionException {
        if(locations.size() == 0) return false;
        int[] message = new int[7];
        int shift = 0;
        message[0] = teamSecret;
        int i = 0;
        for(MapLocation Loc : locations){
            shift = Loc.x;
            shift = shift << 6;
            shift += Loc.y;
            message[i+1] = shift;
            i++;
        }

        if(rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
            System.out.println("Dig locations");
            return true;
        }else{
            return false;
        }
    }

    public boolean getDigLocations(ArrayList<MapLocation> locations) throws GameActionException {
        for (Transaction tx : rc.getBlock(rc.getRoundNum() - 1)) {
            int[] mess = tx.getMessage();
            if(mess == null) return false;
            if (mess[0] == teamSecret) {
               if(mess[1] > 10){
                   

               }
            }
        }
        return false;
    }

    int getBidValue(){
        try {
            int r = rc.getRoundNum();
            if (r <= 1) return 1;
            Transaction[] transactions = rc.getBlock(r-1);
            int ans = 1;
            if (transactions.length < GameConstants.NUMBER_OF_TRANSACTIONS_PER_BLOCK) return 1;
            for (Transaction t : transactions){
                if (t == null) return 1;
                if ((t.getMessage()[6]) != r-1){
                    int b = t.getCost();
                    if (b >= ans) ans = b+1;
                }
            }
            return ans;
        } catch (Throwable t){
            t.printStackTrace();
        }
        return 1;
    }
}

