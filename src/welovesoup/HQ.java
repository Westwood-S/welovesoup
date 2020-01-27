package welovesoup;
import battlecode.common.*;

import java.util.ArrayList;

public class HQ extends Shooter {
    static int numMiners = 0;
    Team team = rc.getTeam();
    ArrayList<MapLocation> placeToDig = new ArrayList<MapLocation>();
    public HQ(RobotController r) throws GameActionException {
        super(r);
        MapLocation loc = rc.getLocation();
        int x = loc.x;
        int y = loc.y;
        comms.sendHqLoc(loc);
        comms.broadcastSoupLocation(rc.senseNearbySoup()[0]);
        placeToDig.add(loc.translate(2, 0 ));
        placeToDig.add(loc.translate(-2, 0));
        placeToDig.add(loc.translate(0, 2));
        placeToDig.add(loc.translate(0, -2));
        comms.broadcastDigLocations(placeToDig);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        //System.out.println("Soup Locations:" + rc.senseNearbySoup());
        if(turnCount >= 430 && turnCount <= 432 && !AllLandScapers(rc.senseNearbyRobots(2, team))) {
            comms.broadcastNotSurrounded();
            System.out.println("Not sorrounded");
        }
        if(rc.getMapHeight() < 64){
            if(numMiners < 4) {
                //for (Direction dir : Util.directions)
                if (tryBuild(RobotType.MINER, Util.randomDirection())) {
                    numMiners++;
                }
            }
        } else {
            if(numMiners < 7) {
                if (tryBuild(RobotType.MINER, Util.randomDirection())){
                    numMiners++;
                }
            }
        }
    }

    boolean AllLandScapers(RobotInfo[] robots){
        if(robots.length < 8) {
            System.out.println("There wasn't enough " + robots.length);
            return false;
        }
        for(RobotInfo rob : robots){
          if(rob.getTeam() != team){
              System.out.println("They weren't from out team: " + rob.getTeam());
              return false;
          }
          if(rob.getType() != RobotType.LANDSCAPER){
              System.out.println("They were: " + rob.getType());
              return false;
          }
        }
        return true;
    }
}