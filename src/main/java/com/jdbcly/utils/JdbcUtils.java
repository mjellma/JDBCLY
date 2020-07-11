package com.jdbcly.utils;

import com.jdbcly.core.ESqlDataType;
import com.jdbcly.core.ResultItem;
import com.jdbcly.core.SelectStatement;
import com.jdbcly.core.SqlExpression;
import com.jdbcly.jdbc.JdbclyColumn;
import com.jdbcly.jdbc.JdbclyTable;
import org.apache.commons.lang3.math.NumberUtils;

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
            projection.add(column.getNameAlias());
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

        ESqlDataType currentType;
        ESqlDataType newType;
        for (ResultItem<?> row : rows) {
            for (Map.Entry<String, ?> entry : row.entrySet()) {
                currentType = types.get(entry.getKey());

                if (currentType == ESqlDataType.VARCHAR) {
                    // String is the lowest type denominator
                    continue;
                }

                newType = determineType(entry.getValue());
                if (currentType == null || (currentType != newType && currentType.isAssignableTo(newType))) {
                    types.put(entry.getKey(), newType);
                }
            }
        }

        return types;
    }

    public static ESqlDataType determineType(Object value) {
        if (value instanceof String) {
            try {
                return determineNumberType(NumberUtils.createNumber((String) value));
            } catch (NumberFormatException e) {
                return ESqlDataType.VARCHAR;
            }
        } else if (value instanceof Number) {
            return determineNumberType((Number) value);
        }

        return ESqlDataType.VARCHAR;
    }

    public static ESqlDataType determineNumberType(Number number) {
        if (number.intValue() == number.doubleValue()) {
            return ESqlDataType.INTEGER;
        }
        return ESqlDataType.DOUBLE;
    }
}
