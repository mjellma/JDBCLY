package com.jdbcly.core;

import com.jdbcly.engine.ResultItem;
import com.jdbcly.jdbc.JdbclyColumn;
import com.jdbcly.jdbc.JdbclyTable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Date: 7/1/2020
 */
public class JdbcUtils {
    public static String[] extractColumnNames(JdbclyColumn[] columns) {
        return Arrays.stream(columns).map(JdbclyColumn::getName).toArray(String[]::new);
    }

    public static JdbclyTable extractTable(JdbclyTable[] tables, String table) {
        table = table
                .replace("\"", "")
                .replace("'", "")
                .replace("`", "");

        for (JdbclyTable t : tables) {
            if (t.getName().equalsIgnoreCase(table)) {
                return t;
            }
        }
        throw new RuntimeException("Table not found: " + table);
    }

    /**
     * Supports only values defined in {@code ESqlDataType}.
     *
     * @return a map of (columnName) -> (columnType)
     */
    public static ResultItem<ESqlDataType> determineColumnTypes(List<ResultItem<?>> rows) {
        ResultItem<ESqlDataType> types = new ResultItem<>();

        ESqlDataType type;
        for (ResultItem<?> row : rows) {
            for (Map.Entry<String, ?> entry : row.entrySet()) {
                type = types.get(entry.getKey());

                if (type == ESqlDataType.VARCHAR) {
                    // String is the lowest type denominator
                    continue;
                }

                if (isPossibleDouble(entry.getValue())) {
                    types.put(entry.getKey(), ESqlDataType.DOUBLE);
                } else {
                    types.put(entry.getKey(), ESqlDataType.VARCHAR);
                }
            }
        }

        return types;
    }

    private static boolean isPossibleDouble(Object value) {
        if (value instanceof Number) {
            return true;
        }

        if (value instanceof String) {
            try {
                Double.parseDouble((String) value);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }
}
