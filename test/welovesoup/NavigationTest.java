package welovesoup;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotEquals;

public class NavigationTest {

    @Test
    public void testNavigationCreation() {
        Navigation n1 = Mockito.mock(Navigation.class);
        Navigation n2 = Mockito.mock(Navigation.class);
        assertNotEquals(n1, n2);
    }
}
