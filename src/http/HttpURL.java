/*
 * Re * Created on May 5, 2013 11:11:36 PM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package http;

import gui.QueryProfileTab;

import java.util.HashMap;

/**
 * @author Phoenix Xu
 */
public final class HttpURL {

    private final static String                  URL_BASE      = "https://95598.gd.csg.cn/";

    public final static String                   URL_CAPTCHA   = "https://95598.gd.csg.cn/Session?name=rand";

    private final static HashMap<String, String> CityCodeToUrl = new HashMap<String, String>();

    static {
        String normalUrlr = URL_BASE + "loginTiao.do?areaCode=";
        for (String cityCode : QueryProfileTab.CITYCODE) {
            try {
                Integer.valueOf(cityCode);
                CityCodeToUrl.put(cityCode, normalUrlr + cityCode);
            } catch (NumberFormatException e) {
                CityCodeToUrl.put(cityCode, "https://95598.guangzhou.csg.cn/");
            }
        }
    }

    /**
     * Get the URL of the web application by the city code.
     * 
     * @param cityCode The city code
     * @return
     */
    public static String getURLByCity(String cityCode) {
        return CityCodeToUrl.get(cityCode);
    }

    public static String getBillURL() {
        return URL_BASE + "userBilling.do";
    }

    public static String getBaseURL() {
        return URL_BASE;
    }

    public static String getCaptchaURL() {
        return URL_BASE + "check?" + System.currentTimeMillis();
    }

    public static String getRandURL() {
        return URL_BASE + "Session?name=rand";
    }

    public static String getLoginURL() {
        return URL_BASE + "login.do";
    }

    public static String getLogoffURL(String sessionId) {
        return URL_BASE + "logoff.do?sessid=" + sessionId;
    }
}
