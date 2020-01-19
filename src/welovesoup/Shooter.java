package welovesoup;
import battlecode.common.*;

public class Shooter extends Building {

    public Shooter(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();

        RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED,rc.getTeam().opponent());
        for (RobotInfo robot : robots) {
            if (robot.type == RobotType.LANDSCAPER || robot.type == RobotType.DELIVERY_DRONE || robot.type == RobotType.NET_GUN || robot.type == RobotType.MINER) {
                if (rc.canShootUnit(robot.ID)) {
                    rc.shootUnit(robot.ID);
                    rc.setIndicatorLine(rc.getLocation(), robot.location, 255, 255, 0);
                }
            }
        }
    }
}