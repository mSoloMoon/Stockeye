package com.msolo.stockeye.gui.activity.quotation;

import com.msolo.stockeye.service.userdata.StockQuoteData;

import junit.framework.TestCase;

/**
 * Created by mSolo on 2014/8/7.
 */
public class StockQuoteDataTest extends TestCase {

    private StockQuoteData quoteData = null;

    public StockQuoteDataTest() {
        this("StockeyeAppVariableTest");
    }

    /**
     * @param name
     */
    public StockQuoteDataTest(String name) {
        super(name);
    }

    /**
     *  (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        quoteData = StockQuoteData.getInstance();
    }

    /**
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetInstance() {

        StockQuoteData appVariable1 = StockQuoteData.getInstance();
        StockQuoteData appVariable2 = StockQuoteData.getInstance();

        assertSame(appVariable1, appVariable2);

    }

    public void testGetStockcodeArray() {

        String[] stockcodeArray = quoteData.getStockcodeArray();

        assertNotNull(stockcodeArray);
        assertEquals(0, stockcodeArray.length);

    }

    public void testAddOneToStockcodeList() {

        quoteData.addOneToStockList("testCode", "testName");

        String[] stockcodeArray = quoteData.getStockcodeArray();

        assertEquals(1, stockcodeArray.length);
        assertEquals("testCode", stockcodeArray[stockcodeArray.length - 1]);

        quoteData.removeOneFromStockList("testCode");

    }

    public void testLoadSharedPrefToMemory() {

        quoteData.loadSharedPrefToMemory();
        String[] stockcodeArray = quoteData.getStockcodeArray();

        assertNotNull(stockcodeArray);
        assertTrue(stockcodeArray.length >= 2);

    }


}
