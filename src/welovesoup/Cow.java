package welovesoup;
import battlecode.common.*;

public class Cow extends Unit {

    public Cow(RobotController r) throws GameActionException {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();

        rc.disintegrate();
    }
}