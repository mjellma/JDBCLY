package com.jdbcly.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 6/30/2020
 *
 * Attempts to conserve memory during heavy parsing of data - while avoiding String interning.
 * This is necessary until pagination support is added.
 */
public class StringsCache {
    private final Map<String, String> cache = new HashMap<>();

    public String getOrPut(String s) {
        String cached = cache.get(s);
        if (cached == null) {
            cache.put(s, s);
            return s;
        }
        return cached;
    }
}
