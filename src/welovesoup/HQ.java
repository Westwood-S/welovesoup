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
        boolean isBuilt = false;

        if(numMiners < 5)
            for (Direction dir : Util.directions) {
                //isBuilt = tryBuild(RobotType.MINER, Util.randomDirection());
                //numMiners++;
                if (numMiners >= 5)
                    break;
                if (tryBuild(RobotType.MINER, dir))
                    numMiners++;
            }
    }
}