/*
 * Created on May 6, 2013 11:59:22 PM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package model;

/**
 * @author Phoenix Xu
 */
public class Account {

    public String accountNum;

    public String password;

    public String city;

    public Account(String accountNum, String password, String city) {
        super();
        this.accountNum = accountNum;
        this.password = password;
        this.city = city;
    }
}
