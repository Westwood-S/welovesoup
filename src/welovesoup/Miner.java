package welovesoup;
import battlecode.common.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Miner extends Unit {

    int numDesignSchools = 0;
    int numNetgun = 0;
    ArrayList<MapLocation> refineryLocations = new ArrayList<MapLocation>();
    ArrayList<MapLocation> vaporatorLocations = new ArrayList<MapLocation>();
    ArrayList<MapLocation> soupLocations = new ArrayList<MapLocation>();

    public Miner(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        
        numDesignSchools += comms.getNewDesignSchoolCount();
        soupLocations.addAll(Arrays.asList(rc.senseNearbySoup()));
        comms.updateSoupLocations(soupLocations); //System.out.println("Soup locations: " + soupLocations.size()); //Debugging purpose
        comms.updateRefnyLocations(refineryLocations);  System.out.println("Refinery locations: " + refineryLocations.size());
        comms.updateVaporatorLocations(vaporatorLocations);
        checkSoup();
        checkRefny();

        Direction randomDir = Util.randomDirection();
//---------------------------------------Trying to build------------------------------------
//Refinery
        if (turnCount>150 && refineryLocations.size()<1) {
            while(rc.getLocation().distanceSquaredTo(hqLoc)<45) {
                rc.move(randomDir);
            }
               // for (Direction dir : Util.directions)
                    if (tryBuild(RobotType.REFINERY, randomDir)) {
                        MapLocation refnyLoc = rc.getLocation().add(randomDir);
                        comms.broadcastRefnyLocation(refnyLoc);
                    }
        }
        //if (turnCount > 50 &&
////Vaporator
//        if(turnCount>250 && vaporatorLocations.size() == 0) {
//            System.out.println("Trying to build vaporator");
//            if (tryBuild(RobotType.VAPORATOR, randomDir)) {
//                System.out.println("Created a Vaporator");
//                MapLocation VapeLoc = rc.getLocation().add(randomDir);
//                comms.broadcastVaporatorLocation(VapeLoc);
//            }
//        }
////Design school
//        if (turnCount>50 && numDesignSchools == 0 && rc.getLocation().distanceSquaredTo(hqLoc)>4) {
//            if(tryBuild(RobotType.DESIGN_SCHOOL, randomDir))
//                System.out.println("created a design school");
//        }
//net gun
//        if(turnCount>75 && numNetgun == 0 && rc.getLocation().distanceSquaredTo(hqLoc)> 4)
//            if(tryBuild(RobotType.NET_GUN, randomDir)) {
//                System.out.println("Created a net gun somewhere random");
//            }

//----------------------------------Searching for --------------------------------
 //Refinery
        for (Direction dir : Util.directions)
            if (tryRefine(dir)){
                MapLocation refnyLoc = rc.getLocation().add(dir);
                if (refineryLocations.contains(refnyLoc) && refnyLoc != hqLoc) //this was !refinery
                    comms.broadcastRefnyLocation(refnyLoc);
            }
//Soup
        for (Direction dir : Util.directions)
            if (tryMine(dir)) {
                MapLocation soupLoc = rc.getLocation().add(dir);
                if (!soupLocations.contains(soupLoc))
                    comms.broadcastSoupLocation(soupLoc);
            }

//------------------------------Full of Soup-----------------------------------
        if (rc.getSoupCarrying() == RobotType.MINER.soupLimit) {
            // time to go back to the HQ
            if (numDesignSchools==0 || refineryLocations.size()==0)
                if(nav.goTo(hqLoc))
                    System.out.println("moved towards HQ");
            else if (refineryLocations.size() > 0)
                nav.goTo(refineryLocations.get(0));
            else 
                if (nav.goTo(randomDir))
                    System.out.println("I moved randomly!");

        } else if (soupLocations.size() > 0) {
            nav.goTo(soupLocations.get(0));
            //Here is where algorithm will go.
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
            if (rc.canSenseLocation(whereTheyGo) && rc.senseSoup(whereTheyGo) == 0) {
                soupLocations.remove(0);
            }
        }
        if(soupLocations.contains(rc.getLocation()) && rc.senseSoup(rc.getLocation()) == 0){
            soupLocations.clear();
        }
    }
    void checkRefny() throws GameActionException {
        if (refineryLocations.size()!=0 && rc.getLocation().distanceSquaredTo(refineryLocations.get(0))>100) {
            refineryLocations.remove(0);
        }
    }
}
