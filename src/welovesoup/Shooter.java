package welovesoup;
import battlecode.common.*;

public class Shooter extends Building {

    public Shooter(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();

        RobotInfo[] robots = rc.senseNearbyRobots(rc.getCurrentSensorRadiusSquared(),rc.getTeam().opponent());
        for (RobotInfo robot : robots) {
            if (robot.type == RobotType.LANDSCAPER || robot.type == RobotType.DESIGN_SCHOOL || robot.type == RobotType.MINER || robot.type == RobotType.DELIVERY_DRONE) {
                if (rc.canShootUnit(robot.ID)) {
                    rc.shootUnit(robot.ID);
                    rc.setIndicatorLine(rc.getLocation(), robot.location, 255, 255, 0);
                }
            }
        }
    }
}