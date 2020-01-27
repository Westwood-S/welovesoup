package welovesoup;
import battlecode.common.*;

import java.util.ArrayList;
import java.util.Map;

public class Landscaper extends Unit {
    int dirtCarrying = 0;
    boolean nextToHQ = false;
    public boolean surrounded = true;
    ArrayList<MapLocation> digLocations= new ArrayList<MapLocation>();
    public Landscaper(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        dirtCarrying = rc.getDirtCarrying();
        nextToHQ = rc.getLocation().isAdjacentTo(hqLoc);

        if(rc.getRoundNum() >= 430 && rc.getRoundNum() <= 432)
            if(comms.updateSurrounded() == 0) surrounded = false;

        if (hqLoc != null && nextToHQ) {
            Direction dirtohq = rc.getLocation().directionTo(hqLoc);
            if(rc.canDigDirt(dirtohq)){
                rc.digDirt(dirtohq);
            }
        } else {
            if (Math.random() < 0.7) {
                nav.goTo(Util.randomDirection());
            } else {
                nav.goTo(hqLoc);
            }
        }
        if (nextToHQ && dirtCarrying > 0 ){
            if(!surrounded){
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
                if (bestPlaceToBuildWall != null && rc.senseRobotAtLocation(bestPlaceToBuildWall).getType() != RobotType.LANDSCAPER) {
                    rc.depositDirt(rc.getLocation().directionTo(bestPlaceToBuildWall));
//                    rc.setIndicatorDot(bestPlaceToBuildWall, 0, 255, 0);
                    System.out.println("wall best fit");
                }

            } else if(rc.canDepositDirt(Direction.CENTER)) {
                rc.depositDirt(Direction.CENTER);
                System.out.println("wall under me");
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