package core.error;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by nitina on 10/28/16.
 */
public class EpikosErrorTest {

    EpikosError error;

    @Before
    public void setup(){
        error = new EpikosError();
    }

    @Test
    public void testEpikosError() throws Exception{
        Assert.assertNotEquals(null,error);
    }

    @Test
    public void testGetSetMessageAndId(){
        error.setId(1);
        error.setMessage("test_message");
        Assert.assertEquals(1,error.getId());
        Assert.assertEquals("test_message",error.getMessage());
    }
}
