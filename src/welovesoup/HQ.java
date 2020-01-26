package welovesoup;
import battlecode.common.*;

public class HQ extends Shooter {
    static int numMiners = 0;
    Team team = rc.getTeam();
    MapLocation[] available =  new MapLocation[8];
    public HQ(RobotController r) throws GameActionException {
        super(r);

        comms.sendHqLoc(rc.getLocation());
        comms.broadcastSoupLocation(rc.senseNearbySoup()[0]);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        //System.out.println("Soup Locations:" + rc.senseNearbySoup());
        if(turnCount >= 430 && turnCount <= 432 && !AllLandScapers(rc.senseNearbyRobots(2, team))) {
            comms.broadcastNotSurrounded();
            System.out.println("Not sorrounded");
        }
        if(numMiners < 4) {
            //for (Direction dir : Util.directions)
            if (tryBuild(RobotType.MINER, Util.randomDirection())) {
                numMiners++;
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