package welovesoup;
import battlecode.common.*;

public class DesignSchool extends Building {

    int numLandScapers=0;

    public DesignSchool(RobotController r) {
        super(r);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        
        // will only actually happen if we haven't already broadcasted the creation
        comms.broadcastDesignSchoolCreation(rc.getLocation());

        if ( numLandScapers < 8) {

//            for (Direction dir : Util.directions)
                if (tryBuild(RobotType.LANDSCAPER, Direction.EAST)) {
                    System.out.println("made a landscaper");
                    numLandScapers++;
                }else{
                    if(tryBuild(RobotType.LANDSCAPER, Direction.SOUTHEAST)){
                        numLandScapers++;
                    }

                }
        } else if (rc.getTeamSoup() > 600 && numLandScapers < 27){
//            for (Direction dir : Util.directions)
                if (tryBuild(RobotType.LANDSCAPER, Direction.EAST)) {
                    System.out.println("made a landscaper");
                    numLandScapers++;
                }
        }
            
    }
}
