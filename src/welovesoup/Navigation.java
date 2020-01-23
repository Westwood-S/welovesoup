package welovesoup;
import battlecode.common.*;

public class Navigation {
    RobotController rc;

    // state related only to navigation should go here

    public Navigation(RobotController r) {
        rc = r;
    }
    
    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    boolean tryMove(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canMove(dir)) {
            if (rc.getType()!=RobotType.DELIVERY_DRONE && (rc.senseFlooding(rc.getLocation().add(dir)) ||  rc.sensePollution(rc.getLocation().add(dir))>2000 || rc.senseElevation(rc.getLocation())!=rc.senseElevation(rc.getLocation().add(dir)))){
                return false;
            }
            else {
                rc.move(dir);
                return true;
            }
        } else return false;
    }

    boolean hasnearbyRobots(RobotType target) throws GameActionException {
        RobotInfo[] robots = rc.senseNearbyRobots();
        for (RobotInfo r : robots) {
            if (r.getType() == target)
                return true;
        }
        return false;
    }

    // tries to move in the general direction of dir
    boolean goTo(Direction dir) throws GameActionException {
        Direction[] toTry = {dir, dir.rotateLeft(), dir.rotateRight(), dir.rotateLeft().rotateLeft(), dir.rotateRight().rotateRight(), dir.rotateLeft().rotateLeft()};
        for (Direction d : toTry){
            if(tryMove(d))
                return true;
        }
        return false;
    }

    // navigate towards a particular location
    boolean goTo(MapLocation destination) throws GameActionException {
        return goTo(rc.getLocation().directionTo(destination));
    }

    void trailBlazer(Direction dir) throws GameActionException {
        
            if(tryMove(dir)){}
            else if (rc.senseElevation(rc.getLocation())<rc.senseElevation(rc.getLocation().add(dir))){
                while(rc.canDigDirt(dir))
                    rc.digDirt(dir);
                tryMove(dir);
            }
            else if (rc.senseFlooding(rc.getLocation().add(dir))){
                while(rc.canDepositDirt(dir)) 
                    rc.depositDirt(dir);
                tryMove(dir);
            }

    }

    void trailBlazer(MapLocation destination) throws GameActionException {
        trailBlazer(rc.getLocation().directionTo(destination));
    }
}