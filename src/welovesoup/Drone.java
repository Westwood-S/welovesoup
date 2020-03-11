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
        try {
            opponentHQ = comms.getOpponentHQLoc();
        } catch (GameActionException ex) {
            ex.printStackTrace();
        }
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        findHQ();
        robotInfos = rc.senseNearbyRobots();
        if(rc.getRoundNum() > 1000) {
            attack();
            return;
        }
        if(rc.isCurrentlyHoldingUnit() && opponentHQ == null) {
            findOpponentHQ();
            if(opponentHQ != null)
                dropUnitToOpponentHQ();
        } else if (rc.isCurrentlyHoldingUnit()) {
            dropUnitToOpponentHQ();
        } else if (robotInfos.length > 0) {
            robotInfos = rc.senseNearbyRobots();
            for (RobotInfo r : robotInfos) {
                if (r.team == rc.getTeam().opponent()) {
                    if(rc.getLocation().distanceSquaredTo(opponentHQ) <= GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED) {
                        nav.goTo(rc.getLocation().directionTo(hqLoc));
                    }
                } else if ((r.type == RobotType.LANDSCAPER && r.team == rc.getTeam().opponent() && r.team == rc.getTeam() ||
                        r.type == RobotType.DESIGN_SCHOOL && r.team == rc.getTeam().opponent() || r.type == RobotType.MINER
                        && r.team == rc.getTeam().opponent()) &&
                        (rc.getLocation().distanceSquaredTo(r.location) > GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED)) {
                    if (rc.canPickUpUnit(r.getID()))
                        rc.pickUpUnit(r.getID());
                    while (!rc.isCurrentlyHoldingUnit()) {
                        nav.goTo(r.location);
                        if (rc.canPickUpUnit(r.getID()))
                            rc.pickUpUnit(r.getID());
                    }
                } else if (r.type == RobotType.COW && !rc.isCurrentlyHoldingUnit()) {
//                    if (opponentHQ != null && (rc.getLocation().distanceSquaredTo(opponentHQ) < GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED)) {
//                        continue;
//                    }
                    while (!rc.getLocation().isAdjacentTo(r.location)) {
                        nav.goTo(r.location);
                    }
                    if (rc.canPickUpUnit(r.getID()))
                        rc.pickUpUnit(r.getID());
                    System.out.println("COW caught!");
                }
            }
        }
        rotateAround(hqLoc, 5, 1500);
        if(opponentHQ == null) {
            findOpponentHQ();
        } else {
            System.out.println(opponentHQ);
            System.out.println("oppoHQ");
        }
    }

    public void droneFly(Direction dir) throws GameActionException {
        MapLocation target = new MapLocation(34, 5);
        if(dir == Direction.SOUTH) {
            while (!rc.getLocation().isAdjacentTo(new MapLocation(34, 5))) {
                checkRobots(rc.senseNearbyRobots());
                if(opponentHQ != null)
                    break;
                nav.goTo(target);
            }
        } else if (dir == Direction.WEST){
            target = new MapLocation(5,5);
            while (!rc.getLocation().isAdjacentTo(target)) {
                checkRobots(rc.senseNearbyRobots());
                if(opponentHQ != null)
                    break;
                nav.goTo(target);
            }
        } else if (dir == Direction.NORTH){
            if(opponentHQ != null)
                target = new MapLocation(5,20);
            else target = new MapLocation(5,21);
            while (!rc.getLocation().isAdjacentTo(target)) {
                checkRobots(rc.senseNearbyRobots());
                if(opponentHQ != null)
                    break;
                nav.goTo(target);
            }
        } else if (dir == Direction.EAST){
            target = new MapLocation(35,25);
            while (!rc.getLocation().isAdjacentTo(target)) {
                checkRobots(rc.senseNearbyRobots());
                if(opponentHQ != null)
                    break;
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
            //checkRobots(rc.senseNearbyRobots());
            nav.goTo(nextLoc);
            if ((int) angle >= 360)
                angle = 0;
            if (loops < cnt || opponentHQ == null)
                break;
            else
                cnt++;
        }
    }

    public void checkRobots(RobotInfo[] robotInfos) throws GameActionException {
        if(rc.senseFlooding(rc.getLocation()))
            comms.sendWaterLoc(rc.getLocation());
        if (robotInfos != null) {
            for (RobotInfo r : robotInfos) {
                if (opponentHQ == null && r.team == rc.getTeam().opponent()) {
                    System.out.println("opponent found!!!");
                    opponentHQ = r.location;
                    comms.sendOpponentHQLoc(opponentHQ);
                } else if ((r.type == RobotType.LANDSCAPER && r.team == rc.getTeam().opponent() ||
                        r.type == RobotType.MINER && r.team == rc.getTeam().opponent()) &&
                        (rc.getLocation().distanceSquaredTo(opponentHQ) > GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED)) {
                    if (rc.canPickUpUnit(r.getID()))
                        rc.pickUpUnit(r.getID());
                    while (!rc.isCurrentlyHoldingUnit()) {
                        nav.goTo(r.location);
                        if (rc.canPickUpUnit(r.getID()))
                            rc.pickUpUnit(r.getID());
                    }
                    System.out.println("r.type == RobotType.LANDSCAPER");
                } else if (!rc.isCurrentlyHoldingUnit() && r.type == RobotType.COW) {
                    if (opponentHQ != null && (rc.getLocation().distanceSquaredTo(opponentHQ) < GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED+5)) {
                        continue;
                    }
                    while (!rc.getLocation().isAdjacentTo(r.location)) {
                        nav.goTo(r.location);
                        if (rc.canPickUpUnit(r.getID()))
                            rc.pickUpUnit(r.getID());
//                        else if(rc.isCurrentlyHoldingUnit())
//                            break;
                    }
                    if (rc.canPickUpUnit(r.getID()))
                        rc.pickUpUnit(r.getID());

                    if(rc.isCurrentlyHoldingUnit()) {
                        dropUnitToOpponentHQ();
                        //dropUnitInWater();
                    }
                } else if (opponentHQ != null && rc.getLocation().distanceSquaredTo(opponentHQ) < GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED)
                        nav.goTo(hqLoc);
                if(r.type == RobotType.NET_GUN) {
                    if(rc.canPickUpUnit(r.getID()))
                        rc.pickUpUnit(r.getID());
                }
            }
        }
    }

    public void dropUnitInWater() throws GameActionException{
        MapLocation w = comms.getWaterLocFromBlockchain();
        System.out.println(w);
        System.out.println("dropUnitInWater!!!!!!!!!");
        while(!rc.senseFlooding(rc.getLocation())) {
            w = comms.getWaterLocFromBlockchain();
            if(w != null)
                nav.goTo(w);
        }
        if(rc.canDropUnit(Direction.CENTER))
            rc.dropUnit(Direction.CENTER);
    }

    public void dropUnitToOpponentHQ() throws GameActionException {
        MapLocation opHQ = comms.getOpponentHQLoc();
        System.out.println(opHQ);
        System.out.println("dropUnitToOpponentHQ!!!!!!!!!"+opponentHQ);
        if(opponentHQ == null)
            findOpponentHQ();
        while(opponentHQ != null && rc.getLocation().distanceSquaredTo(opponentHQ) > GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED
        && rc.getLocation().distanceSquaredTo(opponentHQ) < GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED+5) {
            //opHQ = comms.getOpponentHQLoc();
            if(opponentHQ != null)
                nav.goTo(opponentHQ);
        }
        Direction d;
        if(opponentHQ != null) {
            while (!rc.canDropUnit(d = Util.randomDirection())) ;
            rc.dropUnit(d);
            nav.goTo(hqLoc);
        }
        System.out.println("unit droped to opponent hq");
    }

    public void findOpponentHQ() throws GameActionException {
        opponentHQ = comms.getOpponentHQLoc();
        while(opponentHQ == null) {
            searchForOpponentHQ();
            droneFly(Direction.SOUTH);
            droneFly(Direction.WEST);
            droneFly(Direction.NORTH);
            droneFly(Direction.EAST);
        }
    }

    public void searchForOpponentHQ() throws GameActionException {
        RobotInfo[] robotInfos = rc.senseNearbyRobots();
        for(Direction direction : Util.directions) {
            nav.goTo(direction);
            for (RobotInfo robotInfo : robotInfos) {
                if (robotInfo.type == RobotType.HQ && robotInfo.team == rc.getTeam().opponent()) {
                    opponentHQ = robotInfo.location;
                    comms.sendOpponentHQLoc(opponentHQ);
                }
            }
        }
    }

    public void attack() throws GameActionException {
        //rotateAttack(new MapLocation(20, 18), 7, 1500);

        a();
    }

    public void rotateAttack(MapLocation target, int radius, int loops) throws GameActionException {
        origin = target;
        int cnt = 0;
        while (angle <= 360) {
            angle += PI / 20;
            radians = angle * (PI / 180);
            X = origin.x + cos(radians) * radius;
            Y = origin.y + sin(radians) * radius;
            nextLoc = new MapLocation((int) X, (int) Y);
            //checkRobots(rc.senseNearbyRobots());
//            if(rc.getLocation().distanceSquaredTo(opponentHQ) > GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED)
                nav.goTo(nextLoc);
//            else {
//                nextLoc = nextLoc.translate((GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED - rc.getLocation().distanceSquaredTo(opponentHQ)),
//                        (GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED - rc.getLocation().distanceSquaredTo(opponentHQ)));
//            }
            if ((int) angle >= 360)
                angle = 0;
            if (loops < cnt || opponentHQ == null)
                break;
            else
                cnt++;
        }
    }

    public void a() throws GameActionException {

//        nav.goTo(rc.getLocation().add(Direction.WEST));
//        nav.goTo(rc.getLocation().add(Direction.WEST));
//        nav.goTo(rc.getLocation().add(Direction.WEST));
//        nav.goTo(rc.getLocation().add(Direction.WEST));
//        nav.goTo(rc.getLocation().add(Direction.WEST));
//        nav.goTo(rc.getLocation().add(Direction.WEST));
        nav.goTo(rc.getLocation().add(Direction.SOUTH));
        nav.goTo(rc.getLocation().add(Direction.SOUTH));
        nav.goTo(rc.getLocation().add(Direction.SOUTH));
        nav.goTo(rc.getLocation().add(Direction.SOUTH));
//        nav.goTo(rc.getLocation().add(Direction.EAST));
//        nav.goTo(rc.getLocation().add(Direction.EAST));
//        nav.goTo(rc.getLocation().add(Direction.EAST));
//        nav.goTo(rc.getLocation().add(Direction.EAST));
//        nav.goTo(rc.getLocation().add(Direction.EAST));
//        nav.goTo(rc.getLocation().add(Direction.EAST));
//        nav.goTo(rc.getLocation().add(Direction.NORTHEAST));
        nav.goTo(rc.getLocation().add(Direction.NORTH));
        nav.goTo(rc.getLocation().add(Direction.NORTH));
        nav.goTo(rc.getLocation().add(Direction.NORTH));
        nav.goTo(rc.getLocation().add(Direction.NORTH));
        nav.goTo(rc.getLocation().add(Direction.NORTH));
        nav.goTo(rc.getLocation().add(Direction.NORTH));
//        nav.goTo(rc.getLocation().add(Direction.NORTHWEST));
    }

}
