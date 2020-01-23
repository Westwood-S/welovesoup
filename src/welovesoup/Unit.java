package welovesoup;
import battlecode.common.*;

import java.util.LinkedList;
import java.util.Queue;

public class Unit extends Robot {

    Navigation nav;
    Queue<MapLocation> previousLocations = new LinkedList<MapLocation>();
    MapLocation hqLoc;

    public Unit(RobotController r) {
        super(r);
        nav = new Navigation(rc);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        previousLocations.add(rc.getLocation());
        if(previousLocations.size() > 7){
            previousLocations.remove();
        }
        if(turnCount < 5)
            findHQ();
    }

    public void findHQ() throws GameActionException {
        if (hqLoc == null) {
            // search surroundings for HQ
            RobotInfo[] robots = rc.senseNearbyRobots();
            for (RobotInfo robot : robots) {
                if (robot.type == RobotType.HQ && robot.team == rc.getTeam()) {
                    hqLoc = robot.location;
                }
            }
            if(hqLoc == null) {
                // if still null, search the blockchain
                hqLoc = comms.getHqLocFromBlockchain();
            }
        }
    }

}