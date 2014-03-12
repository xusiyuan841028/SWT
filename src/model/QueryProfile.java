/*
 * Created on May 10, 2013 3:03:07 AM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package model;

import java.util.Calendar;

import utils.Utils;

/**
 * @author Phoenix Xu
 */
public class QueryProfile {

    public String         uuid;

    public Account        account;

    public QueryParameter parameter;

    public QueryProfile(Account account, QueryParameter parameter) {
        this.uuid = Utils.getUUID();
        this.account = account;
        this.parameter = parameter;
    }

    public QueryProfile(String accountNum, String password, String city, int year, int interval) {
        this(new Account(accountNum, password, city), new QueryParameter(year, interval));
    }

    public QueryProfile() {
        this.uuid = Utils.getUUID();
        this.account = new Account("", "", "");
        Calendar date = Calendar.getInstance();
        this.parameter = new QueryParameter(date.get(Calendar.YEAR), 1);
    }
}
