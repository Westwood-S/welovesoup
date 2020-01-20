package welovesoup;
import battlecode.common.*;
import java.util.ArrayList;
import java.util.Map;

public class Miner extends Unit {

    int numDesignSchools = 0;
    int numFulfillmentCenter = 0;
    int group = 0;
    ArrayList<MapLocation> refineryLocations = new ArrayList<MapLocation>();
    ArrayList<MapLocation> soupLocations = new ArrayList<MapLocation>();

    public Miner(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        RobotInfo[] robotInfos = rc.senseNearbyRobots();
        int miner_count = 0;
        for(RobotInfo r: robotInfos) {
            if(r.type == RobotType.MINER) {
                miner_count += 1;
            }
        }

        if (miner_count > 3 && (rc.senseFlooding(hqLoc))) {
            Direction di = Util.randomDirection();
            while(!nav.goTo(di)) {
                di = Util.randomDirection();
//                if(rc.canMove(di))
//                    nav.goTo(di);
            }
        } else {
            Direction d = rc.getLocation().directionTo(rc.senseNearbySoup()[0]); // Util.randomDirection();
            System.out.println(rc.senseNearbySoup().length);
            System.out.println("MINER!!!");
            if ((group % 2) == 0 && rc.canMove(d)) {
                nav.goTo(d);
                group += 1;
            }
        }
        numDesignSchools += comms.getNewDesignSchoolCount();
        numFulfillmentCenter += comms.getNewFulfillmentCenterCount();
        comms.updateSoupLocations(soupLocations);
        comms.updateRefnyLocations(refineryLocations);

        checkSoup();

        for (Direction dir : Util.directions) {
            if (tryRefine(dir))
                System.out.println("I refined soup! ");
        }
        for (Direction dir : Util.directions)
            if (tryMine(dir)) {
                MapLocation soupLoc = rc.getLocation().add(dir);
                if (!soupLocations.contains(soupLoc))
                    comms.broadcastSoupLocation(soupLoc);
            }

        RobotInfo[] ris = rc.senseNearbyRobots();
        int landscaper_count = 0;
        for(RobotInfo r: ris) {
            if(r.type == RobotType.LANDSCAPER) {
                landscaper_count += 1;
            }
        }
        System.out.println("landscaper_count: ");
        System.out.println(landscaper_count);




        boolean ds = false;
        Direction ds_dir = Util.randomDirection();
        if (numDesignSchools < 1 && rc.getTeamSoup() >= 150) {
            for(Direction dir : Util.directions) {
                ds = tryBuild(RobotType.DESIGN_SCHOOL, ds_dir);
                if (ds) {
                    //ds_dir = dir;
                    break;
                }
            }
            System.out.println("ds|");
            if(ds) {
                System.out.println("created a design school");
                MapLocation mm = rc.getLocation().add(ds_dir);
                comms.broadcastDesignSchoolCreation(mm);
                numDesignSchools += comms.getNewDesignSchoolCount();
            }
        } else  if (landscaper_count > 10){
            if ((refineryLocations.size()>0 && rc.getLocation().distanceSquaredTo(refineryLocations.get(0))>100) || refineryLocations.size()==0) {
                if (refineryLocations.size()>0)
                    refineryLocations.remove(0);
                if (tryBuild(RobotType.REFINERY, rc.getLocation().directionTo(hqLoc))) {
                    MapLocation refnyLoc = rc.getLocation().add(rc.getLocation().directionTo(hqLoc));
                    if (!refineryLocations.contains(refnyLoc))
                        comms.broadcastRefnyLocation(refnyLoc);
                }
            }
        }


        if (landscaper_count > 10 && numFulfillmentCenter < 1) {
            if(tryBuild(RobotType.FULFILLMENT_CENTER, Util.randomDirection()))
                System.out.println("created a fulfillment center");
            if (refineryLocations.size() == 0) {
                if (tryBuild(RobotType.REFINERY, rc.getLocation().directionTo(hqLoc))) {
                    MapLocation refnyLoc = rc.getLocation().add(rc.getLocation().directionTo(hqLoc));
                    if (!refineryLocations.contains(refnyLoc))
                        comms.broadcastRefnyLocation(refnyLoc);
                }
            }
        }

        if (rc.getSoupCarrying() == RobotType.MINER.soupLimit) {
            // time to go back to the HQ
            if (numDesignSchools<=3)
                if(nav.goTo(hqLoc))
                    System.out.println("moved towards HQ");
            else
                if (refineryLocations.size()!=0)
                    if(nav.goTo(refineryLocations.get(0)))
                        System.out.println("moved towards HQ");
                else if (nav.goTo(Util.randomDirection()))
                    System.out.println("I moved randomly!");
        } else if (soupLocations.size() > 0) {
            nav.goTo(soupLocations.get(0));
            rc.setIndicatorLine(rc.getLocation(), soupLocations.get(0), 255, 255, 0);
        } else if (nav.goTo(Util.randomDirection())) {
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
}
