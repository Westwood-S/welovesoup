package welovesoup;
import battlecode.common.*;

public class DesignSchool extends Building {
    private int numOfLandscapers = 0;
    public DesignSchool(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        
        // will only actually happen if we haven't already broadcasted the creation
        comms.broadcastDesignSchoolCreation(rc.getLocation());
        //while(numOfLandscapers < 5) {
//            for (Direction dir : Util.directions) {
//                if (tryBuild(RobotType.LANDSCAPER, dir)) {
//                    System.out.println("made a landscaper");
//                }
//            }
//        //}
//    }

        int i =0;
        Direction dir;
        while(i<10) {
            //for (Direction dir : Util.directions) {
            dir = Util.randomDirection();
            while(!tryBuild(RobotType.LANDSCAPER, dir)) {
                dir = Util.randomDirection();
            }
            System.out.println("made a landscaper");
            i += 1;
        }
    }
}
