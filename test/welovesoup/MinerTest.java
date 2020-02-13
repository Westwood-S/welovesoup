package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class MinerTest {

    @Test
    public void testMinerCreation() {
        Miner m1 = Mockito.mock(Miner.class);
        Miner m2 = Mockito.mock(Miner.class);
        assertNotEquals(m1, m2);
    }
}
