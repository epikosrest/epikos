package core.spoof;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by nitina on 10/28/16.
 */
public class SpoofTest {

    Spoof spoof;
    @Before
    public void setup() {
        spoof = new Spoof("spoof");
    }

    @Test
    public void testSpoof() throws Exception{
        Assert.assertNotSame(null,spoof);
        Assert.assertEquals("spoof",spoof.getApiName());
    }

    @Test
    public void testSetGetAPI() throws Exception{
        spoof.setApiName("test_api");
        Assert.assertEquals("test_api",spoof.getApiName());
    }
}
