package welovesoup;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class FulfillmentCenter extends Building {

    int numDrone=0;

    public FulfillmentCenter(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        // will only actually happen if we haven't already broadcasted the creation
        comms.broadcastFulfillmentCenterCreation(rc.getLocation());
        if (numDrone<6)
            for (Direction dir : Util.directions) {
                if(rc.getTeamSoup() >= 150) {
                    if (tryBuild(RobotType.DELIVERY_DRONE, dir)){
                        numDrone++;
                        System.out.println("made a delivery drone");
                    }   
                }
            }
    }
}
