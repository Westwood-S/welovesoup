package welovesoup;
import battlecode.common.*;

import java.util.ArrayList;
import java.util.Map;

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
                        //rc.setIndicatorDot(bestPlaceToBuildWall, 0, 255, 0);
                        //System.out.println("wall best fit");
                    }
                }

            } else if(rc.canDepositDirt(Direction.CENTER)) {
                rc.depositDirt(Direction.CENTER);
                //System.out.println("wall under me");
            }
        }else if (dirtCarrying == 0 && rc.getLocation().distanceSquaredTo(hqLoc)<=2){
            tryDig();
        }
    }

    boolean tryDig() throws GameActionException {
        Direction dir;
        if(hqLoc == null){
            dir = Util.randomDirection();
        } else {
            dir = hqLoc.directionTo(rc.getLocation());
        }
        for(Direction dire : Util.directions) {
            if(dire != Direction.CENTER && !rc.getLocation().add(dire).isAdjacentTo(hqLoc)) {
                if (rc.canDigDirt(dire) && !rc.isLocationOccupied(rc.adjacentLocation(dire))) {
                    rc.digDirt(dire);
                    rc.setIndicatorDot(rc.getLocation().add(dire), 255, 0, 0);
                    return true;
                }
            }
        }
        return false;
    }
}