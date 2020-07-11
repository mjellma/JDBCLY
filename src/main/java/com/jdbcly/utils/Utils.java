package com.jdbcly.utils;

import com.jdbcly.core.ESqlDataType;
import org.apache.commons.beanutils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 6/27/2020
 */
public class Utils {
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String getNonNull(String s) {
        if (s == null) return "";
        return s;
    }

    public static <T> List<T> getNonNull(List<T> list) {
        if (list == null) return new ArrayList<>(0);
        return list;
    }

    public static String toString(Object o) {
        if (o instanceof String) {
            return "'" + o + "'";
        }
        return o.toString();
    }

    public static String replace(String original, String toReplace, String value) {
        if (original == null) return null;
        return original.replace(toReplace, value);
    }

    public static Object convert(Object value, ESqlDataType type) {
        // Apache's bean utils won't convert Strings properly, unless a pattern is specified
        if (value instanceof String) {
            return type.parseString((String) value);
        }
        return ConvertUtils.convert(value, type.getDataClass());
    }
}
