package welovesoup;
import battlecode.common.*;

// This is a file to accumulate all the random helper functions
// which don't interact with the game, but are common enough to be used in multiple places.
// For example, lots of logic involving MapLocations and Directions is common and ubiquitous.
public class Util {
    static Direction[] directions = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST,
        Direction.CENTER,
    };

    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    //.length -1 so that we can never get center.
    static Direction randomDirection() { return directions[(int) (Math.random() * directions.length-1)]; }
}
