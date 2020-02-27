package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class FulfillmentCenterTest {

    @Test
    public void testFulfillmentCenterCreation() {
        FulfillmentCenter fc1 = Mockito.mock(FulfillmentCenter.class);
        FulfillmentCenter fc2 = Mockito.mock(FulfillmentCenter.class);
        assertNotEquals(fc1, fc2);
    }
}
