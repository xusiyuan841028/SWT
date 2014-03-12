/*
 * Created on May 6, 2013 10:49:56 PM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package utils;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.UUID;

import org.springframework.util.Assert;

/**
 * @author Phoenix Xu
 */
public class Utils {

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getPropValue(Properties props, String paramName) {
        String paramValue = props.getProperty(paramName);
        if (paramValue == null) {
            return "";
        }

        try {
            return new String(paramValue.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return paramValue;
    }

    /**
     * Gets a string from the resource bundle.
     * We don't want to crash because of a missing String.
     * Returns the key if not found.
     */
    public static String getResourceString(ResourceBundle resourceBundle, String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        } catch (NullPointerException e) {
            return "!" + key + "!";
        }
    }

    /**
     * Gets a string from the resource bundle and binds it
     * with the given arguments. If the key is not found,
     * return the key.
     */
    public static String getResourceString(ResourceBundle resourceBundle, String key, Object[] args) {
        try {
            return MessageFormat.format(getResourceString(resourceBundle, key), args);
        } catch (MissingResourceException e) {
            return key;
        } catch (NullPointerException e) {
            return "!" + key + "!";
        }
    }

    /**
     * Concatenate a string array into a string.
     * 
     * @param strings A String array
     * @return
     */
    public static String concatenate(String[] strings) {
        Assert.notNull(strings);
        StringBuilder sBuilder = new StringBuilder("[");
        for (String string : strings) {
            sBuilder.append(string).append(", ");
        }
        if (strings.length > 0) {
            sBuilder.delete(sBuilder.length() - 2, sBuilder.length());
        }
        sBuilder.append("]");

        return sBuilder.toString();
    }
}
