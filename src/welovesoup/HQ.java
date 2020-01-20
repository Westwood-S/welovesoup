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
        if(turnCount > 200 && turnCount < 202 && !AllLandScapers(rc.senseNearbyRobots(4, team))) {
            comms.broadcastNotSorrounded();
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
        if(robots.length < 8) return false;
        for(RobotInfo rob : robots){
          if(rob.getTeam() != team) return false;
          if(rob.getType() != RobotType.LANDSCAPER) return false;
        }
        return true;
    }
}