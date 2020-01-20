package welovesoup;

import battlecode.common.*;

public class Drone extends Unit {
    public Drone(RobotController r) {
        super(r);
    }
    MapLocation mapp = null;
    public void takeTurn() throws GameActionException {
        super.takeTurn();
        RobotInfo[] robotInfos = rc.senseNearbyRobots(GameConstants.DELIVERY_DRONE_PICKUP_RADIUS_SQUARED,rc.getTeam().opponent());
        if(rc.isCurrentlyHoldingUnit()) {
            nav.goTo(Direction.WEST);
            while(!rc.canDropUnit(Direction.WEST))
                nav.goTo(Direction.WEST);
            rc.dropUnit(Direction.WEST);
        }
        for(RobotInfo r: robotInfos) {
            if(r.type == RobotType.LANDSCAPER) {
                mapp = r.location;
                System.out.println(nav.goTo(r.location));
                if (rc.canPickUpUnit(r.getID()))
                    rc.pickUpUnit(r.getID());
                while(!rc.isCurrentlyHoldingUnit()) {
                    nav.goTo(r.location);
                        if (rc.canPickUpUnit(r.getID()))
                            rc.pickUpUnit(r.getID());
                }
                System.out.println(r.getID());
            }
        }
        Direction direction = Util.randomDirection();
        boolean bb = false;
        if (mapp != null)
            bb = nav.goTo(mapp);
        else
            nav.goTo(direction);
        while(!bb) {
            direction = Util.randomDirection();
            if (mapp != null)
                bb = nav.goTo(mapp);
            else
                nav.goTo(direction);
        }
    }
}
