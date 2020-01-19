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
        if(numMiners < 150)
            //for (Direction dir : Util.directions)
            isBuilt = tryBuild(RobotType.MINER, Util.randomDirection());
        numMiners++;
        while(isBuilt != true)
            isBuilt = tryBuild(RobotType.MINER, Util.randomDirection());
            numMiners++;
    }
}