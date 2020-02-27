package welovesoup;
import battlecode.common.*;

import java.util.ArrayList;

public class HQ extends Shooter {
    static int numMiners = 0;
<<<<<<< HEAD
    Team team = rc.getTeam();
    MapLocation[] available =  new MapLocation[8];
=======
    Team team;
    ArrayList<MapLocation> placeToDig = new ArrayList<MapLocation>();
>>>>>>> master
    public HQ(RobotController r) throws GameActionException {
        super(r);
        MapLocation loc = rc.getLocation();
         this.team = rc.getTeam();
        comms.sendHqLoc(loc);
        comms.broadcastSoupLocation(rc.senseNearbySoup()[0]);

<<<<<<< HEAD
        comms.sendHqLoc(rc.getLocation());
        comms.broadcastSoupLocation(rc.senseNearbySoup()[0]);
=======
//        comms.broadcastDigLocations(placeToDig);
>>>>>>> master
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();

<<<<<<< HEAD
        if(turnCount >= 430 && turnCount <= 432 && !AllLandScapers(rc.senseNearbyRobots(4, team))) {
            comms.broadcastNotSurrounded();
            System.out.println("Not sorrounded");
        }
        if(numMiners < 6) {
            if (tryBuild(RobotType.MINER, Util.randomDirection())) {
                numMiners++;
=======
        if(turnCount >= 350 && turnCount % 50 == 0 &&!AllLandScapers(rc.senseNearbyRobots(4, team))) {
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
>>>>>>> master
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