package welovesoup;

import battlecode.common.*;

public class Drone extends Unit {
    public Drone(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        Direction direction = Util.randomDirection();
        while(!nav.goTo(direction))
            direction = Util.randomDirection();
        System.out.println("Moving drone");
//        RobotInfo[] robotInfos = rc.senseNearbyRobots();
//        for(RobotInfo r: robotInfos) {
//            if(r.type == RobotType.COW) {
//                while(!nav.goTo(Util.randomDirection())) ;
//                System.out.println("Moving a COOWW");
////                if(rc.canPickUpUnit(r.getID())) {
////                    System.out.println("^^^^^^^^^^^^^^^^^^^^^^^");
////                    rc.pickUpUnit(r.getID());
////                    nav.goTo(new MapLocation(5,5));
////                    rc.dropUnit(Direction.SOUTH);
////                }
//                System.out.println("drone delivered a cow to enemy");
//            }
//        }
    }
}
