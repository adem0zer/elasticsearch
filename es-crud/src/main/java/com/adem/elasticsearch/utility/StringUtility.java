package com.adem.elasticsearch.utility;

public class StringUtility {

    public static String prefixWildCard(String s) {
        return "*" + s;
    }

    public static String suffixWildCard(String s) {
        return s + "*";
    }

    public static String wildCard(String s) {
        return "*" + s + "*";
    }
}
