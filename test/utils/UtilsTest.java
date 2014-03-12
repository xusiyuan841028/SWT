/*
 * Created on May 27, 2013 4:59:22 AM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Phoenix Xu
 */
public class UtilsTest extends Utils {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link utils.Utils#getUUID()}.
     */
    @Test
    public void testGetUUID() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link utils.Utils#getPropValue(java.util.Properties, java.lang.String)}.
     */
    @Test
    public void testGetPropValue() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link utils.Utils#getResourceString(java.util.ResourceBundle, java.lang.String)}.
     */
    @Test
    public void testGetResourceStringResourceBundleString() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link utils.Utils#getResourceString(java.util.ResourceBundle, java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testGetResourceStringResourceBundleStringObjectArray() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link utils.Utils#concatenate(java.lang.String[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void concatenate_A$StringArray_null() {
        String[] strings = null;
        Utils.concatenate(strings);
    }

    /**
     * Test method for {@link utils.Utils#concatenate(java.lang.String[])}.
     */
    @Test
    public void concatenate_A$StringArray_empty() {
        String[] strings = new String[0];
        assertEquals("[]", Utils.concatenate(strings));
    }

    /**
     * Test method for {@link utils.Utils#concatenate(java.lang.String[])}.
     */
    @Test
    public void conctenate_A$StringArray_OneElem() {
        String[] strings = new String[] { "element_1" };
        assertEquals("[element_1]", Utils.concatenate(strings));
    }

    /**
     * Test method for {@link utils.Utils#concatenate(java.lang.String[])}.
     */
    @Test
    public void conctenate_A$StringArray_MultipleElem() {
        String[] strings = new String[] { "element_1", "element_2", "element_3" };
        assertEquals("[element_1, element_2, element_3]", Utils.concatenate(strings));
    }
}
