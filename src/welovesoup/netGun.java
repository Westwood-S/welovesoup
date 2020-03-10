package welovesoup;

import battlecode.common.*;

public class netGun extends Shooter {
    MapLocation myLoc;

    public netGun(RobotController r) {
        super(r);
        myLoc = rc.getLocation();
    }

    @Override
    public void takeTurn() throws GameActionException {
        super.takeTurn();
        tryShoot();
    }

    void tryShoot(){
        if (!rc.isReady()) return;
        netGun.ShootingTarget s = null;
        int sight = rc.getCurrentSensorRadiusSquared();
        if (sight > GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED) sight = GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED;
        RobotInfo[] rArray = rc.senseNearbyRobots(sight, rc.getTeam().opponent());
        for (RobotInfo r : rArray){
            if (rc.canShootUnit(r.getID())){
                netGun.ShootingTarget t = new netGun.ShootingTarget(r);
                if (t.isBetterThan(s)) s = t;
            }
        }
        if (s != null) s.shoot();
    }

    class ShootingTarget{
        int dist;
        int id;

        ShootingTarget(RobotInfo r){
            dist = r.location.distanceSquaredTo(myLoc);
            id = r.getID();
        }

        boolean isBetterThan(netGun.ShootingTarget s){
            if (s == null) return true;
            return dist < s.dist;
        }

        void shoot(){
            try{
                rc.shootUnit(id);
            } catch (Throwable t){
                t.printStackTrace();
            }
        }

    }

}
