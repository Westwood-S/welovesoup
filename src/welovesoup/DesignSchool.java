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

        if (numLandScapers < 3)
            for (Direction dir : Util.directions) 
                if(tryBuild(RobotType.LANDSCAPER, dir)) 
                    numLandScapers++;
                
            
    }
}
