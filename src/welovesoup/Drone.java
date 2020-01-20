package welovesoup;

import battlecode.common.*;

public class Drone extends Unit {
    public Drone(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        RobotInfo[] robotInfos = rc.senseNearbyRobots();
        for(RobotInfo r: robotInfos) {
            if(r.type == RobotType.COW) {
                if(rc.canPickUpUnit(r.getID())) {
                    rc.pickUpUnit(r.getID());
                    nav.goTo(new MapLocation(5,5));
                    rc.dropUnit(Direction.SOUTH);
                }
                System.out.println("drone delivered a cow to enemy");
            }
        }
    }
}
