package lectureplayer;
import battlecode.common.*;

public class DesignSchool extends Building {
    public DesignSchool(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        
        // will only actually happen if we haven't already broadcasted the creation
        comms.broadcastDesignSchoolCreation(rc.getLocation());
        int i = 0;
        Direction dir = Util.randomDirection();
        while(i<15){
        //for (Direction dir : Util.directions) {
            dir = Util.randomDirection();
            while (!tryBuild(RobotType.LANDSCAPER, dir)) {
                dir = Util.randomDirection();
            }
            System.out.println("made a landscaper");
            i++;
        }
    }
}
