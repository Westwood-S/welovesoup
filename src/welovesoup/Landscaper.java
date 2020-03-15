package welovesoup;
import battlecode.common.*;

import java.util.ArrayList;
import java.util.Map;

import static battlecode.common.Direction.*;

public class Landscaper extends Unit {
    int dirtCarrying = 0;
    boolean nextToHQ = false;
    public int surrounded = -10;
    ArrayList<MapLocation> digLocations= new ArrayList<MapLocation>();
    ArrayList<MapLocation> vaporatorLocs = new ArrayList<MapLocation>();

    public Landscaper(RobotController r) {
        super(r);
    }


    public void takeTurn() throws GameActionException {
        super.takeTurn();
        dirtCarrying = rc.getDirtCarrying();
        nextToHQ = rc.getLocation().isAdjacentTo(hqLoc);
        comms.updateVaporatorLocations(vaporatorLocs);

        if(!isMoreRoomCloseToHq()) {
            comms.broadcastSecondWall();
        }
        System.out.println("second wall can begin: "+ comms.getSecondWall());


        if(rc.getRoundNum() >= 500) {
            for (RobotInfo r:rc.senseNearbyRobots()) {
                if(r.type == RobotType.NET_GUN){
                    nav.goTo(r.location);
                }
            }
        }

        if(rc.getRoundNum() >= 351 && rc.getRoundNum() % 50 == 1)
            surrounded = comms.updateSurrounded();

        Direction dirtohq = rc.getLocation().directionTo(hqLoc);
        if (hqLoc != null && nextToHQ && dirtCarrying < RobotType.LANDSCAPER.dirtLimit) {
            if(rc.canDigDirt(dirtohq)){
                rc.digDirt(dirtohq);
            }
        }
        if (nextToHQ && dirtCarrying > 0 ){
            if(surrounded == 0){
                MapLocation bestPlaceToBuildWall = null;
                //find best place to build
                int lowestElevation = 9999999;
                for (Direction dir : Util.directions) {
                    if(dir != Direction.CENTER) {
                        MapLocation tileToCheck = hqLoc.add(dir);
                        if (rc.getLocation().distanceSquaredTo(tileToCheck) < 4
                                && rc.canDepositDirt(rc.getLocation().directionTo(tileToCheck))) {
                            if (rc.senseElevation(tileToCheck) < lowestElevation) {
                                lowestElevation = rc.senseElevation(tileToCheck);
                                bestPlaceToBuildWall = tileToCheck;
                            }
                        }
                    }
                }
                if (bestPlaceToBuildWall != null) {
                    if (rc.canDepositDirt(rc.getLocation().directionTo(bestPlaceToBuildWall))){
                        rc.depositDirt(rc.getLocation().directionTo(bestPlaceToBuildWall));
                    }
                }
            } else if(rc.canDepositDirt(Direction.CENTER)) {
                rc.depositDirt(Direction.CENTER);
            }
        } else if (dirtCarrying == 0 && rc.getLocation().distanceSquaredTo(hqLoc)<=2){
            tryDig2();
        } else {
            if(!comms.getSecondWall()) {
                System.out.println("IN IS MORE");
                nextToHq();
                System.out.println("OUTTT");
            } else {
                System.out.println("ELSE IS MORE");
                buildSecondWall();
            }
        }
    }


    boolean tryDig(Direction dir) throws GameActionException {
        if(rc.canDigDirt(dir) && !rc.isLocationOccupied(rc.adjacentLocation(dir))){
            rc.digDirt(dir);
            rc.setIndicatorDot(rc.getLocation().add(dir), 255, 0, 0);
            return true;
        }
        return false;
    }


    boolean isMoreRoomCloseToHq() throws GameActionException {
        System.out.println(hqLoc);
        ArrayList<RobotInfo> landscapersNextToHQ = new ArrayList<>();
//        boolean flag = false;
//        do {
//            try {
//                flag = false;
//                System.out.println("range: " + rc.getLocation().distanceSquaredTo(hqLoc) + "current rad: " + rc.getCurrentSensorRadiusSquared());
                RobotInfo[] robotInfos = rc.senseNearbyRobots();
                for(RobotInfo ri : robotInfos) {
                    if(ri.type == RobotType.LANDSCAPER) {
                        System.out.println("Location COORD: " + ri.getLocation());
                        try {
                            if(ri.location.isAdjacentTo(hqLoc))
                                landscapersNextToHQ.add(ri);
                        } catch (Exception e) {}
                    }
                }
                System.out.println(landscapersNextToHQ.size());
                if(landscapersNextToHQ.size() != 8)
                    return true;
                return false;
//                if (rc.isLocationOccupied(new MapLocation(hqLoc.add(NORTH).x, hqLoc.add(NORTH).y)) &&
//                        rc.isLocationOccupied(new MapLocation(hqLoc.add(NORTHWEST).x, hqLoc.add(NORTHWEST).y)) &&
//                        rc.isLocationOccupied(new MapLocation(hqLoc.add(NORTHEAST).x, hqLoc.add(NORTHEAST).y)) &&
//                        rc.isLocationOccupied(new MapLocation(hqLoc.add(EAST).x, hqLoc.add(EAST).y)) &&
//                        rc.isLocationOccupied(new MapLocation(hqLoc.add(WEST).x, hqLoc.add(WEST).y)) &&
//                        rc.isLocationOccupied(new MapLocation(hqLoc.add(SOUTH).x, hqLoc.add(SOUTH).y)) &&
//                        rc.isLocationOccupied(new MapLocation(hqLoc.add(SOUTHEAST).x, hqLoc.add(SOUTHEAST).y)) &&
//                        rc.isLocationOccupied(new MapLocation(hqLoc.add(SOUTHWEST).x, hqLoc.add(SOUTHWEST).y))) {
//                    return false;
//                }
//            } catch (GameActionException e) {
//                System.out.println("exception thwron for isLocationOccupied");
//                //nav.goTo(hqLoc);
//                flag = true;
//                return false;
//            }
//        } while (flag == true);
//        return true;
    }

    MapLocation nextToHq() throws GameActionException {
        System.out.println("nextToHQ");
        try {
            if (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTH).x, hqLoc.add(Direction.NORTH).y))) {
                //nav.goTo(new MapLocation(hqLoc.add(Util.randomDirection()).x, hqLoc.add(Util.randomDirection()).y));
                nav.goTo(new MapLocation(hqLoc.add(Direction.NORTH).x, hqLoc.add(Direction.NORTH).y));
            }
            if (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTHEAST).x, hqLoc.add(Direction.NORTHEAST).y))) {
                //nav.goTo(new MapLocation(hqLoc.add(Util.randomDirection()).x, hqLoc.add(Util.randomDirection()).y));
                nav.goTo(new MapLocation(hqLoc.add(Direction.NORTHEAST).x, hqLoc.add(Direction.NORTHEAST).y));
            }
            if (!rc.isLocationOccupied(new MapLocation(hqLoc.add(NORTHWEST).x, hqLoc.add(NORTHWEST).y))) {
                nav.goTo(new MapLocation(hqLoc.add(Util.randomDirection()).x, hqLoc.add(Util.randomDirection()).y));
                nav.goTo(new MapLocation(hqLoc.add(NORTHWEST).x, hqLoc.add(NORTHWEST).y));
            }
            if (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.EAST).x, hqLoc.add(Direction.EAST).y))) {
                nav.goTo(new MapLocation(hqLoc.add(Util.randomDirection()).x, hqLoc.add(Util.randomDirection()).y));
                nav.goTo(new MapLocation(hqLoc.add(Direction.EAST).x, hqLoc.add(Direction.EAST).y));
            }
            if (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.WEST).x, hqLoc.add(Direction.WEST).y))) {
                nav.goTo(new MapLocation(hqLoc.add(Util.randomDirection()).x, hqLoc.add(Util.randomDirection()).y));
                nav.goTo(new MapLocation(hqLoc.add(Direction.WEST).x, hqLoc.add(Direction.WEST).y));
            }
            if (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.SOUTH).x, hqLoc.add(Direction.SOUTH).y))) {
                nav.goTo(new MapLocation(hqLoc.add(Util.randomDirection()).x, hqLoc.add(Util.randomDirection()).y));
                nav.goTo(new MapLocation(hqLoc.add(Direction.SOUTH).x, hqLoc.add(Direction.SOUTH).y));
            }
            if (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.SOUTHWEST).x, hqLoc.add(Direction.SOUTHWEST).y))) {
                nav.goTo(new MapLocation(hqLoc.add(Util.randomDirection()).x, hqLoc.add(Util.randomDirection()).y));
                nav.goTo(new MapLocation(hqLoc.add(Direction.SOUTHWEST).x, hqLoc.add(Direction.SOUTHWEST).y));
            }
            if (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.SOUTHEAST).x, hqLoc.add(Direction.SOUTHEAST).y))) {
                nav.goTo(new MapLocation(hqLoc.add(Util.randomDirection()).x, hqLoc.add(Util.randomDirection()).y));
                nav.goTo(new MapLocation(hqLoc.add(Direction.SOUTHEAST).x, hqLoc.add(Direction.SOUTHEAST).y));
            }
        } catch (Exception e) {}
        if (rc.canDigDirt(Direction.CENTER))
            return rc.getLocation();
        return null;
    }


    void tryDig2() throws GameActionException {
        Direction dir;
        if(nextToHQ){
           dir = hqLoc.directionTo(rc.getLocation()); //Direction in relation to hq
           switch (dir) {
               case NORTH: if(!tryDig(NORTH)) tryDig(NORTHEAST); break;
               case NORTHEAST: if(!tryDig(NORTHWEST)) tryDig(SOUTHEAST); break;
               case NORTHWEST:  if(!tryDig(NORTHEAST)) tryDig(SOUTHEAST); break;
               case WEST:  if(!tryDig(WEST)) tryDig(SOUTHEAST); break;
               case EAST:   if(!tryDig(EAST)) tryDig(SOUTHEAST); break;
               case SOUTH: if(!tryDig(SOUTH)) tryDig(SOUTHEAST); break;
               case SOUTHEAST: if(!tryDig(NORTHEAST)) tryDig(SOUTHWEST);  break;
               case SOUTHWEST: if(!tryDig(NORTHWEST)) tryDig(SOUTHWEST); break;
           }
        }
        else{
            tryDig(NORTH);
        }
    }

    void tryDig3(Direction dir) throws GameActionException {
        if(nextToHQ){
            //dir = hqLoc.directionTo(rc.getLocation()); //Direction in relation to hq
            switch (dir) {
                case NORTH: if(!tryDig(NORTH)) tryDig(NORTHEAST); break;
                case NORTHEAST: if(!tryDig(NORTHWEST)) tryDig(SOUTHEAST); break;
                case NORTHWEST:  if(!tryDig(NORTHEAST)) tryDig(SOUTHEAST); break;
                case WEST:  if(!tryDig(WEST)) tryDig(SOUTHEAST); break;
                case EAST:   if(!tryDig(EAST)) tryDig(SOUTHEAST); break;
                case SOUTH: if(!tryDig(SOUTH)) tryDig(SOUTHEAST); break;
                case SOUTHEAST: if(!tryDig(NORTHEAST)) tryDig(SOUTHWEST);  break;
                case SOUTHWEST: if(!tryDig(NORTHWEST)) tryDig(SOUTHWEST); break;
            }
        }
        else{
            tryDig(NORTH);
        }
    }

    MapLocation randPos() throws GameActionException {
        MapLocation p1 = new MapLocation(hqLoc.x + 3, hqLoc.y + 3);
        MapLocation p2 = new MapLocation(hqLoc.x + 3, hqLoc.y + 2);
        MapLocation p3 = new MapLocation(hqLoc.x + 3, hqLoc.y + 1);
        MapLocation p4 = new MapLocation(hqLoc.x + 3, hqLoc.y);
        MapLocation p5 = new MapLocation(hqLoc.x + 3, hqLoc.y - 1);
        MapLocation p6 = new MapLocation(hqLoc.x + 3, hqLoc.y - 2);
        MapLocation p7 = new MapLocation(hqLoc.x + 3, hqLoc.y - 3);
        MapLocation[] wall = {p1, p2, p3, p4, p5, p6, p7};
        return wall[(int) (Math.random() * wall.length-1)];
    }

    public void buildSecondWall() throws GameActionException {
        System.out.println("build second wall" + new MapLocation(hqLoc.x + 3, hqLoc.y + 3));
        MapLocation p1 = new MapLocation(hqLoc.x + 3, hqLoc.y + 3);
        MapLocation p2 = new MapLocation(hqLoc.x + 3, hqLoc.y + 2);
        MapLocation p3 = new MapLocation(hqLoc.x + 3, hqLoc.y + 1);
        MapLocation p4 = new MapLocation(hqLoc.x + 3, hqLoc.y);
        MapLocation p5 = new MapLocation(hqLoc.x + 3, hqLoc.y - 1);
        MapLocation p6 = new MapLocation(hqLoc.x + 3, hqLoc.y - 2);
        MapLocation p7 = new MapLocation(hqLoc.x + 3, hqLoc.y - 3);
        MapLocation myLoc = rc.getLocation();
        boolean flag = false;
        if(myLoc.equals(p1) || myLoc.equals(p2) || myLoc.equals(p3) || myLoc.equals(p4) || myLoc.equals(p5))
            flag = true;
        try {
            if (!flag && !rc.isLocationOccupied(p1)) {
                nav.goTo(p1);
                flag = true;
            }
        } catch (Exception e) {
        }
        try {
            if (!flag && !rc.isLocationOccupied(p2)) {
                nav.goTo(p2);
                flag = true;
            }
        } catch (Exception e) {
        }
        try {
            if (!flag && !rc.isLocationOccupied(p3)) {
                nav.goTo(p3);
                flag = true;
            }
        } catch (Exception e) {
        }
        try {
            if (!flag && !rc.isLocationOccupied(p4)) {
                nav.goTo(p4);
                flag = true;
            }
        } catch (Exception e) {
        }
        try {
            if (!flag && !rc.isLocationOccupied(p5)) {
                nav.goTo(p5);
                flag = true;
            }
        } catch (Exception e) {
        }
        tryDig(WEST);
        tryDig(EAST);
        tryDig(SOUTH);
        tryDig(SOUTHEAST);
        tryDig(SOUTHWEST);
        tryDig2();
        if (rc.canDepositDirt(Direction.CENTER))
            rc.depositDirt(Direction.CENTER);
    }

    void whereToGo(){
        int vision = rc.getCurrentSensorRadiusSquared();
        MapLocation me = rc.getLocation();
//        Direction[] directionArray = dir

    }

}