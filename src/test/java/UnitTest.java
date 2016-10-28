import core.spoof.Spoof;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by nitina on 10/20/16.
 */


public class UnitTest {

    private UnitTest subject;

    @Before
    public void setup() {
        subject = new UnitTest();
    }

    @Test
    public void testDummy() {

        Assert.assertEquals(true,true);
    }

    @Test
    public void testService(){
        Spoof spoof = new Spoof("epikos api");
        Assert.assertNotNull(spoof);
    }

}
