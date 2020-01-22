package welovesoup;
import battlecode.common.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Miner extends Unit {

    int numDesignSchools = 0;
    int numFulfillmentCenters = 0;
    int numGun=0;  
    ArrayList<MapLocation> refineryLocations = new ArrayList<MapLocation>();
    ArrayList<MapLocation> vaporatorLocations = new ArrayList<MapLocation>();
    ArrayList<MapLocation> soupLocations = new ArrayList<MapLocation>();

    public Miner(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        
        numDesignSchools += comms.getNewDesignSchoolCount();
        numFulfillmentCenters += comms.getNewFulfillmentCenterCount();
        //soupLocations.addAll(Arrays.asList(rc.senseNearbySoup()));
        comms.updateSoupLocations(soupLocations);
        comms.updateRefnyLocations(refineryLocations);
        comms.updateVaporatorLocations(vaporatorLocations);
        //System.out.println("Vaporator Locations:" + vaporatorLocations);
        
        checkSoup();
        checkRefny();

        Direction randomDir = Util.randomDirection();
        int disToHQ = rc.getLocation().distanceSquaredTo(hqLoc);
        int Soup = rc.getTeamSoup();
////---------------------------------------Trying to build------------------------------------


//Refinery
        if (turnCount> 100 && disToHQ>35 && refineryLocations.size()<1) {
            System.out.println("Trying Refin"); build(RobotType.REFINERY);   }
//Design school
        if (turnCount > 60 && numDesignSchools == 0 && (disToHQ<=17 && disToHQ>8 && disToHQ!=9 && disToHQ!=13 && disToHQ!=18)) {
            System.out.println("Trying School"); build(RobotType.DESIGN_SCHOOL); }
//Vaporator
//        if(turnCount> 250 && Soup > 500 && vaporatorLocations.size() == 0 && disToHQ> 4){
//            System.out.println("Trying to build vaporator"); build(RobotType.VAPORATOR); }
//net gun
//        if(turnCount>150 && Soup > 250 && numNetgun == 0 && disToHQ> 4) {
//            System.out.println("Trying gun"); build(RobotType.NET_GUN); }

// Fulfillment Center
        if (turnCount >300 && numFulfillmentCenters < 1) { // && disToHQ>1) {
            randomDir = Util.randomDirection();
            while(!tryBuild(RobotType.FULFILLMENT_CENTER, randomDir)) {
                randomDir = Util.randomDirection();
            }
            System.out.println("created a fulfilment center");
            comms.broadcastFulfillmentCenterCreation(new MapLocation(randomDir.dx, randomDir.dy));
            
        }


//----------------------------------Searching for --------------------------------
 //Refinery
        for (Direction dir : Util.directions)
            tryRefine(dir);
//Soup
        for (Direction dir : Util.directions)
            if (tryMine(dir)) {
                MapLocation soupLoc = rc.getLocation().add(dir);
                if (!soupLocations.contains(soupLoc))
                    comms.broadcastSoupLocation(soupLoc);
                if (turnCount>80)
                     if(tryBuild(RobotType.REFINERY, randomDir)) {
                        MapLocation refnyLoc = rc.getLocation().add(randomDir);
                        comms.broadcastRefnyLocation(refnyLoc);
                    }
            }

//------------------------------Nav-----------------------------------
        if (turnCount>150) {
            if (rc.getSoupCarrying() == RobotType.MINER.soupLimit){
                if (refineryLocations.size() == 0)
                    nav.goTo(randomDir);
                else 
                    nav.goTo(refineryLocations.get(0));
            }
            else if (soupLocations.size() > 0) 
                nav.goTo(soupLocations.get(0));
            else 
                if (nav.goTo(randomDir))
                    System.out.println("I moved randomly!");
        }
        else if (rc.getSoupCarrying() == RobotType.MINER.soupLimit) {
            // time to go back to the HQ
            if (nav.goTo(hqLoc))
                System.out.println("moved towards HQ");
            else if (refineryLocations.size() > 0)
                nav.goTo(refineryLocations.get(0));
            else 
                if (nav.goTo(randomDir))
                    System.out.println("I moved randomly!");

        } else if (soupLocations.size() > 0) {
            nav.goTo(soupLocations.get(0));
        } else if (nav.goTo(randomDir)) {
            // otherwise, move randomly as usual
            System.out.println("I moved randomly!");
        }
    }

    /**
     * Attempts to mine soup in a given direction.
     *
     * @param dir The intended direction of mining
     * @return true if a move was performed
     * @throws GameActionException
     */
    boolean tryMine(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canMineSoup(dir)) {
            rc.mineSoup(dir);
            return true;
        } else return false;
    }

    /**
     * Attempts to refine soup in a given direction.
     *
     * @param dir The intended direction of refining
     * @return true if a move was performed
     * @throws GameActionException
     */
    boolean tryRefine(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canDepositSoup(dir)) {
            rc.depositSoup(dir, rc.getSoupCarrying());
            return true;
        } else return false;
    }

    void checkSoup() throws GameActionException {
        if (soupLocations.size() > 0) {
            MapLocation whereTheyGo = soupLocations.get(0);
            if (rc.canSenseLocation(whereTheyGo)
                    && rc.senseSoup(whereTheyGo) == 0) {
                soupLocations.remove(0);
            }
        }
    }
    void checkRefny() throws GameActionException {
        if (refineryLocations.size()>1 && rc.getLocation().distanceSquaredTo(refineryLocations.get(0))>100) {
            refineryLocations.remove(0);
        }
    }


    public void build(RobotType building) throws GameActionException {
        for(Direction dir : Util.directions){
            if (tryBuild(building, dir)) {
                MapLocation Location = rc.getLocation().add(dir);
                switch(building){
                    case REFINERY:              comms.broadcastRefnyLocation(Location); break;
                    case FULFILLMENT_CENTER:    System.out.println("Drones ready to be built"); break;
                    case DESIGN_SCHOOL:         comms.broadcastDesignSchoolCreation(Location); break;
                    case VAPORATOR:             comms.broadcastVaporatorLocation(Location); break;
                    case NET_GUN:               System.out.println("Netgun created"); break;
                }
            }
        }
    }
}

