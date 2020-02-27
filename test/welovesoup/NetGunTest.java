package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class NetGunTest {

    @Test
    public void testNetGunCreation() {
        netGun ng1 = Mockito.mock(netGun.class);
        netGun ng2 = Mockito.mock(netGun.class);
        assertNotEquals(ng1, ng2);
    }
}
