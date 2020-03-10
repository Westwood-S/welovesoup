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

        if(rc.getRoundNum() >= 351 && rc.getRoundNum() % 50 == 1)
            surrounded = comms.updateSurrounded();

        Direction dirtohq = rc.getLocation().directionTo(hqLoc);
        if (hqLoc != null && nextToHQ && dirtCarrying < RobotType.LANDSCAPER.dirtLimit) {
            if(rc.canDigDirt(dirtohq)){
                rc.digDirt(dirtohq);
            }
        } else {
//            if (Math.random()  < 0.4) {
//                if (!nav.goTo(Util.randomDirection()))
//                    if(rc.canDigDirt(dirtohq))
//                        rc.digDirt(dirtohq);
//            } else {
    //                if (!nav.goTo(hqLoc))
    //                    if(rc.canDigDirt(dirtohq))
    //                        rc.digDirt(dirtohq);
//            }
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
                //System.out.println("wall under me");
            }
        } else if (dirtCarrying == 0 && rc.getLocation().distanceSquaredTo(hqLoc)<=2){
            tryDig2();
        } else {
//            while(!rc.getLocation().isAdjacentTo(new MapLocation(33, 27)))
//                nav.goTo(new MapLocation(33, 27));
//            if(rc.canDepositDirt(Direction.CENTER))
//                rc.depositDirt(Direction.CENTER);
            isMoreRoomCloseToHq();
            nextToHq();
        }

        if(!nextToHQ) {
            //Here
            System.out.println("We made it"); }
    }


    boolean tryDig(Direction dir) throws GameActionException {
//        Direction dir;
        if(rc.canDigDirt(dir) && !rc.isLocationOccupied(rc.adjacentLocation(dir))){
            rc.digDirt(dir);
            rc.setIndicatorDot(rc.getLocation().add(dir), 255, 0, 0);
            return true;
        }
        if(hqLoc == null){
            dir = Util.randomDirection();
        } else {
            dir = hqLoc.directionTo(rc.getLocation());
        }
        return false;
    }


    boolean isMoreRoomCloseToHq() throws GameActionException {
        System.out.println(hqLoc);

        return true;
    }

    MapLocation nextToHq() throws GameActionException {

//        System.out.println(rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTH).x,hqLoc.add(Direction.NORTH).y)));
//        System.out.println(rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTH).x,hqLoc.add(Direction.NORTHEAST).y)));
//        System.out.println(rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTH).x,hqLoc.add(Direction.NORTHWEST).y)));
//        System.out.println(rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTH).x,hqLoc.add(Direction.SOUTH).y)));
//        System.out.println(rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTH).x,hqLoc.add(Direction.SOUTHEAST).y)));
//        System.out.println(rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTH).x,hqLoc.add(Direction.SOUTHWEST).y)));
//        System.out.println(rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTH).x,hqLoc.add(Direction.EAST).y)));
//        System.out.println(rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTH).x,hqLoc.add(Direction.WEST).y)));

        while (!rc.getLocation().isAdjacentTo(new MapLocation(hqLoc.add(Direction.NORTH).x, hqLoc.add(Direction.NORTH).y)))
            nav.goTo(new MapLocation(hqLoc.add(Direction.NORTH).x, hqLoc.add(Direction.NORTH).y));
        while (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTH).x, hqLoc.add(Direction.NORTH).y)))
            nav.goTo(new MapLocation(hqLoc.add(Direction.NORTH).x, hqLoc.add(Direction.NORTH).y));
        while (!rc.getLocation().isAdjacentTo(new MapLocation(hqLoc.add(Direction.NORTHEAST).x, hqLoc.add(Direction.NORTHEAST).y)))
            nav.goTo(new MapLocation(hqLoc.add(Direction.NORTHEAST).x, hqLoc.add(Direction.NORTHEAST).y));
        while (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.NORTHEAST).x, hqLoc.add(Direction.NORTHEAST).y)))
            nav.goTo(new MapLocation(hqLoc.add(Direction.NORTHEAST).x, hqLoc.add(Direction.NORTHEAST).y));
        while (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.EAST).x, hqLoc.add(Direction.EAST).y)))
            nav.goTo(new MapLocation(hqLoc.add(Direction.EAST).x, hqLoc.add(Direction.EAST).y));
        while (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.WEST).x, hqLoc.add(Direction.WEST).y)))
            nav.goTo(new MapLocation(hqLoc.add(Direction.WEST).x, hqLoc.add(Direction.WEST).y));
        while (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.SOUTH).x, hqLoc.add(Direction.SOUTH).y)))
            nav.goTo(new MapLocation(hqLoc.add(Direction.SOUTH).x, hqLoc.add(Direction.SOUTH).y));
        while (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.SOUTHWEST).x, hqLoc.add(Direction.SOUTHWEST).y)))
            nav.goTo(new MapLocation(hqLoc.add(Direction.SOUTHWEST).x, hqLoc.add(Direction.SOUTHWEST).y));
        while (!rc.isLocationOccupied(new MapLocation(hqLoc.add(Direction.SOUTHEAST).x, hqLoc.add(Direction.SOUTHEAST).y)))
            nav.goTo(new MapLocation(hqLoc.add(Direction.SOUTHEAST).x, hqLoc.add(Direction.SOUTHEAST).y));

        while (!rc.getLocation().isAdjacentTo(hqLoc))
            nav.goTo(new MapLocation(hqLoc.add(Direction.NORTH).x, hqLoc.add(Direction.NORTH).y));
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

    void whereToGo(){
        int vision = rc.getCurrentSensorRadiusSquared();
        MapLocation me = rc.getLocation();
//        Direction[] directionArray = dir

    }

}