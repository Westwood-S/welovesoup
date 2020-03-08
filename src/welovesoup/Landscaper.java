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
//        digLocations.add(hqLoc.translate(2, 0 ));
//        digLocations.add(hqLoc.translate(-2, 0));
//        digLocations.add(hqLoc.translate(0, 2));
//        digLocations.add(hqLoc.translate(0, -2));
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
            if (Math.random()  < 0.4) {
                if (!nav.goTo(Util.randomDirection()))
                    if(rc.canDigDirt(dirtohq))
                        rc.digDirt(dirtohq);
            } else {
                if (!nav.goTo(hqLoc))
                    if(rc.canDigDirt(dirtohq))
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
                //System.out.println("wall under me");
            }
        }else if (dirtCarrying == 0 && rc.getLocation().distanceSquaredTo(hqLoc)<=2){
            tryDig2();
        }
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
}