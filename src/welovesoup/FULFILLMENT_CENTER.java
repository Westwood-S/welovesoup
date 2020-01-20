package welovesoup;

import battlecode.common.*;

public class FulfillmentCenter extends Building {

    public FulfillmentCenter(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        // will only actually happen if we haven't already broadcasted the creation
        comms.broadcastFulfillmentCenterCreation(rc.getLocation());
        //while(numOfLandscapers < 5) {
        for (Direction dir : Util.directions) {
            System.out.println("***150***");
            if(rc.getTeamSoup() >= 150) {
                System.out.println(rc.getTeamSoup());
                System.out.println("***150***");
            }

            if (tryBuild(RobotType.DELIVERY_DRONE, dir)) {
                System.out.println("made a delivery drone");
            }
        }
        //}
    }
}