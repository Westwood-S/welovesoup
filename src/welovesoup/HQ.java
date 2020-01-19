package welovesoup;
import battlecode.common.*;

public class HQ extends Shooter {
    static int numMiners = 0;

    public HQ(RobotController r) throws GameActionException {
        super(r);

        comms.sendHqLoc(rc.getLocation());
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();

        if(numMiners < 6) {
            //for (Direction dir : Util.directions)
            if (tryBuild(RobotType.MINER, Util.randomDirection())) {
                numMiners++;
            }
        }
    }
}