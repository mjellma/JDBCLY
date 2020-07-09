package com.jdbcly.jdbc;

import com.jdbcly.core.ESqlDataType;
import com.jdbcly.core.ResultItem;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 6/30/2020
 */
public class TypeInfo {

    public static String[] getMetadataLabels() {
        return new String[]{
                "TYPE_NAME",
                "DATA_TYPE",
                "PRECISION",
                "LITERAL_PREFIX",
                "LITERAL_SUFFIX",
                "CREATE_PARAMS",
                "NULLABLE",
                "CASE_SENSITIVE",
                "SEARCHABLE",
                "UNSIGNED_ATTRIBUTE",
                "FIXED_PREC_SCALE",
                "AUTO_INCREMENT",
                "LOCAL_TYPE_NAME",
                "MINIMUM_SCALE",
                "MAXIMUM_SCALE",
                "SQL_DATA_TYPE",
                "SQL_DATETIME_SUB",
                "NUM_PREC_RADIX",
        };
    }

    public static List<ResultItem<?>> getSupportedTypes() {
        List<ResultItem<?>> list = new ArrayList<>();

        for (ESqlDataType type : ESqlDataType.values()) {
            ResultItem<String> map = new ResultItem<>();

            map.put("TYPE_NAME", type.getSqlTypeName());
            map.put("DATA_TYPE", "" + type.getSqlType());
            map.put("PRECISION", "" + 10);

            map.put("LITERAL_PREFIX", null);
            map.put("LITERAL_SUFFIX", null);
            map.put("CREATE_PARAMS", null);

            map.put("NULLABLE", "" + ResultSetMetaData.columnNullable);
            map.put("CASE_SENSITIVE", "" + false);
            map.put("SEARCHABLE", "typePredNone");
            map.put("UNSIGNED_ATTRIBUTE", "" + true);
            map.put("FIXED_PREC_SCALE", "" + true);

            map.put("AUTO_INCREMENT", "" + false);
            map.put("LOCAL_TYPE_NAME", null);

            map.put("MINIMUM_SCALE", "" + 0);
            map.put("MAXIMUM_SCALE", "" + 1000);
            map.put("SQL_DATA_TYPE", "" + "" + type.getSqlType());
            map.put("SQL_DATETIME_SUB", "");
            map.put("NUM_PREC_RADIX", "");

            list.add(map);
        }

        return list;
    }
}
