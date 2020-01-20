package welovesoup;

import battlecode.common.*;

public class Drone extends Unit {

    Direction randomDir = Util.randomDirection();

    public Drone(RobotController r) {
        super(r);
    }
    MapLocation mapp = null;
    public void takeTurn() throws GameActionException {
        super.takeTurn();

        RobotInfo[] robotInfos = rc.senseNearbyRobots();
        if(rc.isCurrentlyHoldingUnit()) {
            nav.goTo(randomDir);
            if (rc.senseFlooding(rc.getLocation().add(randomDir)) && rc.canDropUnit(randomDir))
                rc.dropUnit(randomDir);
            nav.goTo(hqLoc);
        }
        for(RobotInfo r: robotInfos) {
            if(r.type == RobotType.LANDSCAPER && r.team == rc.getTeam().opponent() || r.type == RobotType.COW && r.team == rc.getTeam() || r.type == RobotType.DESIGN_SCHOOL && r.team == rc.getTeam().opponent() || r.type == RobotType.MINER && r.team == rc.getTeam().opponent()) {
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
        boolean bb = false;
        if (mapp != null)
            bb = nav.goTo(mapp);
        else
            nav.goTo(randomDir);
    }
}
