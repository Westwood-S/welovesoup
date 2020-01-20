package welovesoup;
import battlecode.common.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Miner extends Unit {

    int numDesignSchools = 0;
    int numNetgun = 0;
    //struct locatio
    ArrayList<MapLocation> refineryLocations = new ArrayList<MapLocation>();
    ArrayList<MapLocation> vaporatorLocations = new ArrayList<MapLocation>();
    ArrayList<MapLocation> soupLocations = new ArrayList<MapLocation>();
    int[][] map = new MapLocation;
    public Miner(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        
        numDesignSchools += comms.getNewDesignSchoolCount();
        soupLocations.addAll(Arrays.asList(rc.senseNearbySoup()));
        comms.updateSoupLocations(soupLocations); //System.out.println("Soup locations: " + soupLocations.size()); //Debugging purpose
        comms.updateRefnyLocations(refineryLocations);  //System.out.println("Refinery locations: " + refineryLocations.size());
        comms.updateVaporatorLocations(vaporatorLocations);
        checkSoup();
        checkRefny();

        int disToHQ = rc.getLocation().distanceSquaredTo(hqLoc);
        int Soup = rc.getTeamSoup();

        Direction randomDir = Util.randomDirection();
//---------------------------------------Trying to build------------------------------------
//Refinery
        if (turnCount> 50 && Soup > 200 &&rc.getLocation().distanceSquaredTo(hqLoc) > 35 &&  refineryLocations.size()<1) {
            System.out.println("Trying Refin"); build(RobotType.REFINERY);   }
//Design school
        if (turnCount > 125 && numDesignSchools == 0 && rc.getLocation().distanceSquaredTo(hqLoc)>4) {
            System.out.println("Trying School"); build(RobotType.DESIGN_SCHOOL); }
//Vaporator
        if(turnCount> 100 && Soup > 500 && vaporatorLocations.size() == 0 && rc.getLocation().distanceSquaredTo(hqLoc)> 4){
            System.out.println("Trying to build vaporator"); build(RobotType.VAPORATOR); }
//net gun
        if(turnCount>150 && Soup > 250 && numNetgun == 0 && rc.getLocation().distanceSquaredTo(hqLoc)> 4) {
            System.out.println("Trying gun"); build(RobotType.NET_GUN); }


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
            //EXplore
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
            System.out.println("SoupLocations cleared");
            soupLocations.clear();
        }
    }
    void checkRefny() throws GameActionException {
        if (refineryLocations.size()!=0 && rc.getLocation().distanceSquaredTo(refineryLocations.get(0))>100) {
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
