package com.msolo.stockeye;

import android.content.Context;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class StockeyeAppTest extends ApplicationTestCase<StockeyeApp>
{

    private StockeyeApp appInstance = null;

    public StockeyeAppTest() {
        super(StockeyeApp.class);
    }

    /**
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        createApplication();
        testApplicationTestCaseSetUpProperly();

        appInstance = getApplication();
        assertNotNull(appInstance);
    }

    /**
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInit() {

        //fail("Not implemented yet");
        assertNotNull(StockeyeApp.appContext);
        assertEquals(appInstance.getApplicationContext(), StockeyeApp.appContext);
        assertNotNull(StockeyeApp.appContentResolver);
        assertEquals(appInstance.getContentResolver(), StockeyeApp.appContentResolver);
        assertNotNull(StockeyeApp.appSharedPref);
        assertEquals(appInstance.getSharedPreferences("stockeye_sharedpref", Context.MODE_PRIVATE), StockeyeApp.appSharedPref);

        //String stockcodeListStr = StockeyeApp.appSharedPref.getString("stockcode_list", "");
        //assertTrue(stockcodeListStr.length() >= 16);
        //assertEquals("sh000001_sz399001", stockcodeListStr.substring(0, 17));

    }

}