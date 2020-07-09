package com.jdbcly.utils;

import com.jdbcly.core.ESqlDataType;
import com.jdbcly.core.ResultItem;
import com.jdbcly.core.SelectStatement;
import com.jdbcly.core.SqlExpression;
import com.jdbcly.jdbc.JdbclyColumn;
import com.jdbcly.jdbc.JdbclyTable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Date: 7/1/2020
 */
public class JdbcUtils {

    public static String[] determineAvailableProjection(JdbclyColumn[] columns, SelectStatement statement) {
        Set<String> projection = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (JdbclyColumn column : columns) {
            projection.add(column.getName());
        }

        for (SqlExpression column : statement.getProjection()) {
            projection.add(column.getName());
        }

        return projection.toArray(new String[0]);
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
