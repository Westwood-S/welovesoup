package welovesoup;
import battlecode.common.*;
import java.util.ArrayList;
import java.util.Map;

public class Miner extends Unit {

    int numDesignSchools = 0;
    ArrayList<MapLocation> refineryLocations = new ArrayList<MapLocation>();
    ArrayList<MapLocation> soupLocations = new ArrayList<MapLocation>();

    public Miner(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();

        numDesignSchools += comms.getNewDesignSchoolCount();
        comms.updateSoupLocations(soupLocations);
        checkSoup();

        for (Direction dir : Util.directions)
            if (tryRefine(dir))
                System.out.println("I refined soup! " + rc.getTeamSoup());
        for (Direction dir : Util.directions)
            if (tryMine(dir)) {
                MapLocation soupLoc = rc.getLocation().add(dir);
                if (!soupLocations.contains(soupLoc))
                    comms.broadcastSoupLocation(soupLoc);

            }
        if (numDesignSchools < 3){
            if(tryBuild(RobotType.DESIGN_SCHOOL, Util.randomDirection()))
                System.out.println("created a design school");
        }
        else {
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
