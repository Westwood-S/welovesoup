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
        //comms.broadcastFulfillmentCenterCreation(rc.getLocation());
        Direction dir = Util.randomDirection();
        while (numDrone<3) {
            //for (Direction dir : Util.directions) {
                while (!tryBuild(RobotType.DELIVERY_DRONE, dir)) {
                    dir = Util.randomDirection();
                }
                numDrone++;
                System.out.println("made a delivery drone");
                //dir = Util.randomDirection();
            //}
        }
    }
}
