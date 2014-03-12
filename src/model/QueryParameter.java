/*
 * Created on May 7, 2013 12:01:50 AM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package model;

import java.util.Calendar;

import org.springframework.util.Assert;

/**
 * Query parameter.
 * 
 * @author Phoenix Xu
 */
public class QueryParameter {

    public static final int INTERVAL_MINIMUM = 1;

    public static final int INTERVAL_MAXIMUM = 10;

    /**
     * Default query interval by year.
     */
    public static final int DEFAULT_INTERVAL = INTERVAL_MINIMUM;

    public static final int FIRST_YEAR       = 2000;

    public static final int LAST_YEAR        = Calendar.getInstance().get(Calendar.YEAR);

    public int              year;

    public int              interval;

    /**
     * Query Constructor
     * 
     * @param year The last year
     * @param interval The query interval by year
     */
    public QueryParameter(int year, int interval) {
        super();
        Assert.isTrue(year >= FIRST_YEAR, "The first year of system data is " + FIRST_YEAR + "!");
        Assert.isTrue(interval > 0, "The query interval should be greater than 0!");
        Assert.isTrue(year - interval >= FIRST_YEAR, "The first year of system data is 2000, so the value (" + year + interval
                + ") of the last year subtracting interval should be not less than " + FIRST_YEAR + "!");
        this.year = year;
        this.interval = interval;
    }

    /**
     * Query Constructor
     * 
     * @param year The last year
     * @param month The last month
     */
    public QueryParameter(int year) {
        this(year, DEFAULT_INTERVAL);
    }

    public String getYearString() {
        return String.valueOf(this.year);
    }
}
