package welovesoup;
import battlecode.common.*;

public class HQ extends Shooter {
    static int numMiners = 0;
    MapLocation[] available =  new MapLocation[8];
    public HQ(RobotController r) throws GameActionException {
        super(r);

        comms.sendHqLoc(rc.getLocation());
        comms.broadcastSoupLocation(rc.senseNearbySoup()[0]);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        //System.out.println("Soup Locations:" + rc.senseNearbySoup());
        if(numMiners < 4) {
            //for (Direction dir : Util.directions)
            if (tryBuild(RobotType.MINER, Util.randomDirection())) {
                numMiners++;
            }
        }
    }
}