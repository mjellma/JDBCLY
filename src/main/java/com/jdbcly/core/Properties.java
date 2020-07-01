package com.jdbcly.core;

/**
 * Date: 6/29/2020
 */
public class Properties {
    public static final Property URI = new Property<String>("uri", "");
    public static final Property SCAN_DEPTH = new Property<Integer>("scandepth", "10", (Integer::parseInt));

    public static class Csv {
        public static final Property DELIMITER = new Property<Character>("delimiter", ",", (p -> p.charAt(0)));
    }
}

