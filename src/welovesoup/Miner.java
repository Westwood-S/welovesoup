package welovesoup;
import battlecode.common.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Miner extends Unit {

    int numDesignSchools = 0;
    int numFulfillmentCenters = 0;
    int numNetgun=0;
    int numVaporators=0;

    ArrayList<MapLocation> refineryLocations = new ArrayList<MapLocation>();
    ArrayList<MapLocation> vaporatorLocations = new ArrayList<MapLocation>();
    ArrayList<MapLocation> soupLocations = new ArrayList<MapLocation>();
    ArrayList<MapLocation> mysoupLocations = new ArrayList<MapLocation>();

    ArrayList<MapLocation> FFLocs = new ArrayList<MapLocation>();
    boolean stuck = false;

    public Miner(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        
        numDesignSchools += comms.getNewDesignSchoolCount();
//        numNetgun += comms.getGunCount();
        //numFulfillmentCenters += comms.getNewFulfillmentCenterCount();

        if (mysoupLocations.size()!=0)
            mysoupLocations.clear();

        mysoupLocations.addAll(Arrays.asList(rc.senseNearbySoup()));
        comms.updateSoupLocations(soupLocations);
        comms.updateRefnyLocations(refineryLocations);

        comms.updateVaporatorLocations(vaporatorLocations);
        comms.updateFFCCreation(FFLocs);
        numVaporators = vaporatorLocations.size();

        checkSoup();
        //checkRefny();

        Direction randomDir = Util.randomDirection();
        int disToHQ = rc.getLocation().distanceSquaredTo(hqLoc);
        int Soup = rc.getTeamSoup();

        if(stuck){
            for(Direction dir : Util.directions)
                if(nav.tryMove(dir))
                    stuck = false;
        }
////---------------------------------------Trying to build------------------------------------
        if(rc.getRoundNum() < 500 && Soup > 150) {
//Vaporator cost 500
            if (rc.getRoundNum() > 150 && Soup >= 500 && disToHQ > 8 && vaporatorLocations.size() < 3 ) {
                System.out.println("Trying to build vaporator");
                build(RobotType.VAPORATOR);
            }

//Refinery cost 200
            if (rc.getRoundNum() > 200 && Soup >= 200 && disToHQ > 53 && refineryLocations.size() == 0) {
                build(RobotType.REFINERY);
            }
//Design school cost 150
            if (rc.getRoundNum() > 50 && numDesignSchools == 0 && (disToHQ >= 10 && disToHQ != 13 && disToHQ != 18 && disToHQ < 25)) {
                System.out.println("Trying School");
                build(RobotType.DESIGN_SCHOOL);
            }
//net gun cost 250
//        if(rc.getRoundNum()>300 && Soup >= 1050 && numNetgun < 5 && disToHQ> 8 && disToHQ < 20) {
//            System.out.println("Trying gun"); build(RobotType.NET_GUN); ++numNetgun; }


// Fulfillment Center cost 150
            //System.out.println("numfulfillmentcenters: " + numFulfillmentCenters);
            if (rc.getRoundNum() > 350 && FFLocs.size() == 0 && Math.random() * 1 < .25) {
                build(RobotType.FULFILLMENT_CENTER);
            }
        }else{

//Refinery cost 200
            if (Soup >= 200 && disToHQ > 53 && refineryLocations.size() == 0) {
                build(RobotType.REFINERY);
            }
            if (Soup >= 500 && disToHQ > 8) {
                System.out.println("Trying to build vaporator");
                build(RobotType.VAPORATOR);
            }
        }
////---------------------------------------Trying to build------------------------------------


//Refinery cost 200
//        if (rc.getRoundNum()> 100 && Soup >= 200 && disToHQ>53 && refineryLocations.size()==0) {
//           build(RobotType.REFINERY);   }
        //Design school cost 150
        if (rc.getRoundNum() > 60 && Soup >= 150 && numDesignSchools == 0 && (disToHQ<=17 && disToHQ>4 && disToHQ!=9 && disToHQ!=13 && disToHQ!=18)) {
            System.out.println("Trying School"); build(RobotType.DESIGN_SCHOOL); }
//Vaporator cost 500
//        if(rc.getRoundNum()> 100 && Soup >= 500 && disToHQ> 4){
//            System.out.println("Trying to build vaporator"); build(RobotType.VAPORATOR); }
//net gun cost 250
//        if(rc.getRoundNum()>150 && Soup >= 250 && numNetgun == 0 && disToHQ> 4) {
//            System.out.println("Trying gun"); build(RobotType.NET_GUN); }

        // Fulfillment Center cost 150
        //numFulfillmentCenters += comms.getNewFulfillmentCenterCount();
        System.out.println("numfulfillmentcenters: " + numFulfillmentCenters);
        if(rc.getRoundNum() > 350 && Soup >= 150 && numFulfillmentCenters < 2) {
            System.out.println("Drone facility in progress");
            build(RobotType.FULFILLMENT_CENTER);
            //while(!tryBuild(RobotType.FULFILLMENT_CENTER, Util.randomDirection())) ;
            comms.broadcastFulfillmentCenterCreation(rc.getLocation());
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
            }

       int maxSoup = RobotType.MINER.soupLimit;
//------------------------------Nav-----------------------------------
        if (rc.getRoundNum()>280 && rc.getLocation().isAdjacentTo(hqLoc))
            rc.disintegrate();
        if (rc.getRoundNum()>130) {
            if (rc.getSoupCarrying() == maxSoup){
                if (refineryLocations.size() == 0) {
                    if (rc.getRoundNum()> 100 && Soup >= 200 && disToHQ>53 && refineryLocations.size()==0) {
                        build(RobotType.REFINERY);
                    } else{
                        newMove();}
                } else
                    nav.goTo(refineryLocations.get(0));
            }
            else if (soupLocations.size() > 0) 
                nav.goTo(soupLocations.get(0));
            else if (mysoupLocations.size() > 0) 
                nav.goTo(mysoupLocations.get(0));
            else
                newMove();
        }
        else if (rc.getSoupCarrying() == maxSoup) {
            // time to go back to the HQ
            if (nav.goTo(hqLoc))
                System.out.println("moved towards HQ");
            else if (refineryLocations.size() > 0)
                nav.goTo(refineryLocations.get(0));
            else {
                newMove();
                //System.out.println("I moved randomly!");
            }
        } 
        else if (soupLocations.size() > 0) {
            nav.goTo(soupLocations.get(0));
        } 
        else if (mysoupLocations.size() > 0) {
            nav.goTo(mysoupLocations.get(0));
        } 
        else{
            newMove();
            //System.out.println("I moved randomly!");
        }
        //System.out.println("Bytes left: " + Clock.getBytecodesLeft());
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
        if (refineryLocations.size()>1 && rc.getLocation().distanceSquaredTo(refineryLocations.get(0))>50) {
            refineryLocations.remove(0);
        }
    }


    public void build(RobotType building) throws GameActionException {
        for(Direction dir : Util.directions){
            if (tryBuild(building, dir)) {
                MapLocation Location = rc.getLocation().add(dir);
                switch(building){
                    case REFINERY:              comms.broadcastRefnyLocation(Location); break;
                    case FULFILLMENT_CENTER:    comms.broadcastFulfillmentCenterCreation(Location);break;
                    case DESIGN_SCHOOL:         comms.broadcastDesignSchoolCreation(Location); break;
                    case VAPORATOR:             comms.broadcastVaporatorLocation(Location); break;
                    case NET_GUN:               System.out.println("Netgun created"); break;
                }
            } else {
                nav.tryMove(Util.randomDirection());
            }
        }
    }

    public void newMove() throws GameActionException {
        Direction dir = Util.randomDirection();
                if(!previousLocations.contains(rc.adjacentLocation(dir)) && rc.canMove(dir)){
                    nav.goTo(dir);
                    return;
                }
       // stuck = true;

        System.out.println("Stuck");
    }
}

