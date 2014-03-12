/*
 * Created on May 20, 2013 12:36:26 AM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package formatter;

import static org.junit.Assert.fail;
import model.QueryProfile;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Phoenix Xu
 */
public class QueryProfileFormatterTest {

    private QueryProfileFormatter formatter;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test method for {@link formatter.QueryProfileFormatter#getQueryProfileRecord(model.QueryProfile)}.
     */
    @Test
    public void testGetQueryProfileRecord() {
        QueryProfile query = new QueryProfile("account", "password", "11", 2013, 5, 2);
        Assert.assertEquals("account,password,11,2013,5,2", this.formatter.getQueryProfileRecord(query));
    }

    /**
     * Test method for {@link formatter.QueryProfileFormatter#getQueryProfileFromRecord(java.lang.String)}.
     */
    @Test
    public void testGetQueryProfileFromRecord() {
        // Strin queryRecord = ""
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link formatter.QueryProfileFormatter#getQueryProfilesFromRecord(java.lang.String)}.
     */
    @Test
    public void testGetQueryProfilesFromRecord() {
        fail("Not yet implemented"); // TODO
    }
}
