package welovesoup;

import battlecode.common.*;

import java.util.Map;

import static java.lang.Math.*;

public class Drone extends Unit {

    Direction randomDir = Util.randomDirection();
    MapLocation nextLoc = null;
    MapLocation origin = new MapLocation(25, 15);
    MapLocation opponentHQ = null;
    MapLocation target = null;
    RobotInfo[] robotInfos = null;
    double X;
    double Y;
    double angle = 0;
    double radius = 5;

    double radians = 0;

    public Drone(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        findHQ();
        robotInfos = rc.senseNearbyRobots();
        if(rc.isCurrentlyHoldingUnit()) {
            Direction dir = Util.randomDirection();
            while(!rc.senseFlooding(rc.getLocation().add(dir)))
                nav.goTo(Util.randomDirection());
            if(rc.canDropUnit(dir))
                rc.dropUnit(dir);
            System.out.println("drone holding unit");
        } else if (robotInfos.length > 0) {
            for (RobotInfo r : robotInfos) {
                if (r.team == rc.getTeam().opponent()) {
                    opponentHQ = r.location;
                    if(rc.getLocation().distanceSquaredTo(r.location) > GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED) {
                        System.out.println("far enough");
                        System.out.println(rc.getLocation().distanceSquaredTo(r.location));
                        nav.goTo(hqLoc);
                    } else {
                        System.out.println("too close: move towards HQ");
                        System.out.println(rc.getLocation().distanceSquaredTo(r.location));
                        nav.goTo(rc.getLocation().directionTo(hqLoc));
                    }
                } else if ((r.type == RobotType.LANDSCAPER && r.team == rc.getTeam().opponent() && r.team == rc.getTeam() ||
                        r.type == RobotType.DESIGN_SCHOOL && r.team == rc.getTeam().opponent() || r.type == RobotType.MINER
                        && r.team == rc.getTeam().opponent()) &&
                        (rc.getLocation().distanceSquaredTo(r.location) > GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED)) {
                    System.out.println(nav.goTo(r.location));
                    if (rc.canPickUpUnit(r.getID()))
                        rc.pickUpUnit(r.getID());
                    while (!rc.isCurrentlyHoldingUnit()) {
                        nav.goTo(r.location);
                        if (rc.canPickUpUnit(r.getID()))
                            rc.pickUpUnit(r.getID());
                    }
                    System.out.println(r.getID());
                } else if (r.type == RobotType.COW) {
                    while (!rc.getLocation().isAdjacentTo(r.location)) {
                        nav.goTo(r.location);
                    }
                    if (rc.canPickUpUnit(r.getID()))
                        rc.pickUpUnit(r.getID());
                } else {
                    //System.out.println("move square, circle or towards HQ");
                    if (opponentHQ != null && rc.getLocation().distanceSquaredTo(opponentHQ) < GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED) {
                        checkRobots(rc.senseNearbyRobots());
                        nav.goTo(hqLoc);
                    }
                    if(rc.getRoundNum() < 900) {
                        droneFly(Direction.SOUTH);
                        droneFly(Direction.WEST);
                        droneFly(Direction.NORTH);
                        droneFly(Direction.EAST);
                        //rotateAround(origin, 5);
                    } else if (rc.getRoundNum() < 2100) {
                        rotateAround(new MapLocation(15,16), 5, 1500);
                    } else nav.goTo(hqLoc);
                }
            }
        } else {
            if (opponentHQ == null)
                    searchForOpponentHQ();
            if (opponentHQ != null) {
                System.out.println("OPPONENT");
                System.out.println(rc.getLocation().distanceSquaredTo(opponentHQ));
                checkRobots(rc.senseNearbyRobots());
                nav.goTo(hqLoc);
            } else {
                System.out.println("drone is flying now :)");
                checkRobots(rc.senseNearbyRobots());
                droneFly(Direction.SOUTH);
                droneFly(Direction.WEST);
                droneFly(Direction.NORTH);
                droneFly(Direction.EAST);
                nav.goTo(hqLoc);
            }
        }
    }


    public void droneFly(Direction dir) throws GameActionException {
        MapLocation target = new MapLocation(34, 5);
        if(dir == Direction.SOUTH) {
            while (!rc.getLocation().isAdjacentTo(new MapLocation(34, 5))) {
                checkRobots(rc.senseNearbyRobots());
                nav.goTo(target);
            }
        } else if (dir == Direction.WEST){
            target = new MapLocation(5,5);
            while (!rc.getLocation().isAdjacentTo(target)) {
                checkRobots(rc.senseNearbyRobots());
                nav.goTo(target);
            }
        } else if (dir == Direction.NORTH){
            target = new MapLocation(5,21);
            while (!rc.getLocation().isAdjacentTo(target)) {
                checkRobots(rc.senseNearbyRobots());
                nav.goTo(target);
            }
        } else if (dir == Direction.EAST){
            target = new MapLocation(35,25);
            while (!rc.getLocation().isAdjacentTo(target)) {
                checkRobots(rc.senseNearbyRobots());
                nav.goTo(target);
            }
        }
    }

    public void rotateAround(MapLocation target, int radius, int loops) throws GameActionException {
        origin = target;
        int cnt = 0;
        while (angle <= 360) {
            angle += PI / 20;
            radians = angle * (PI / 180);
            X = origin.x + cos(radians) * radius;
            Y = origin.y + sin(radians) * radius;
            nextLoc = new MapLocation((int) X, (int) Y);
            checkRobots(rc.senseNearbyRobots());
            nav.goTo(nextLoc);
            if ((int) angle >= 360)
                angle = 0;
            if (loops < cnt)
                break;
            else
                cnt++;
        }
    }

    public void checkRobots(RobotInfo[] robotInfos) throws GameActionException {
        if(rc.isCurrentlyHoldingUnit()) {
            Direction dir = Util.randomDirection();
            while (!rc.senseFlooding(rc.getLocation().add(dir)))
                nav.goTo(Util.randomDirection());
            if (rc.canDropUnit(dir))
                rc.dropUnit(dir);
        }
        if (robotInfos != null) {
            for (RobotInfo r : robotInfos) {
                if (r.team == rc.getTeam().opponent()) {
                    opponentHQ = r.location;
                    if (rc.getLocation().distanceSquaredTo(r.location) > GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED) {
                        System.out.println("far enough");
                        System.out.println(rc.getLocation().distanceSquaredTo(r.location));
                    } else {
                        System.out.println("too close: move towards HQ");
                        System.out.println(rc.getLocation().distanceSquaredTo(r.location));
                        nav.goTo(rc.getLocation().directionTo(hqLoc));
                    }
                } else if ((r.type == RobotType.LANDSCAPER && r.team == rc.getTeam().opponent() && r.team == rc.getTeam() ||
                        r.type == RobotType.DESIGN_SCHOOL && r.team == rc.getTeam().opponent() || r.type == RobotType.MINER
                        && r.team == rc.getTeam().opponent()) &&
                        (rc.getLocation().distanceSquaredTo(r.location) > GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED)) {
                    System.out.println(nav.goTo(r.location));
                    if (rc.canPickUpUnit(r.getID()))
                        rc.pickUpUnit(r.getID());
                    while (!rc.isCurrentlyHoldingUnit()) {
                        nav.goTo(r.location);
                        if (rc.canPickUpUnit(r.getID()))
                            rc.pickUpUnit(r.getID());
                    }
                    System.out.println(r.getID());
                } else if (r.type == RobotType.COW) {
                    while (!rc.getLocation().isAdjacentTo(r.location)) {
                        nav.goTo(r.location);
                        if (rc.canPickUpUnit(r.getID()))
                            rc.pickUpUnit(r.getID());
                    }
                    if (rc.canPickUpUnit(r.getID()))
                        rc.pickUpUnit(r.getID());
                } else if (opponentHQ != null && rc.getLocation().distanceSquaredTo(opponentHQ) < GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED)
                        nav.goTo(hqLoc);
            }
        }
    }

    public void searchForOpponentHQ() throws GameActionException {
        RobotInfo[] robotInfos = rc.senseNearbyRobots();
        for(Direction direction : Util.directions) {
            nav.goTo(direction);
            for (RobotInfo robotInfo : robotInfos) {
                if (robotInfo.type == RobotType.HQ && robotInfo.team == rc.getTeam().opponent()) {
                    opponentHQ = robotInfo.location;
                }
            }
        }
    }
}
