package com.jdbcly.jdbc;

import com.jdbcly.core.ESqlDataType;
import com.jdbcly.core.ResultItem;

import java.sql.DatabaseMetaData;

/**
 * Date: 6/27/2020
 */
public class JdbclyColumn {

    public static final int DEFAULT_COLUMN_SIZE = 500;
    private JdbclyTable table;
    private String name;
    private ESqlDataType dataType;
    private int ordinalIndex;

    public JdbclyColumn(JdbclyTable table, String name, ESqlDataType dataType, int ordinalIndex) {
        this.table = table;
        this.name = name;
        this.dataType = dataType;
        this.ordinalIndex = ordinalIndex;
    }

    public ResultItem<String> getMetadataValues() {
        ResultItem<String> map = new ResultItem<>();
        map.put("TABLE_CAT", table.getCatalog());
        map.put("TABLE_SCHEM", table.getSchema());
        map.put("TABLE_NAME", table.getName());
        map.put("COLUMN_NAME", name);
        map.put("DATA_TYPE", "" + dataType.getSqlType());

        map.put("TYPE_NAME", dataType.getSqlTypeName());
        map.put("COLUMN_SIZE", "" + DEFAULT_COLUMN_SIZE);
        map.put("BUFFER_LENGTH", null);
        map.put("DECIMAL_DIGITS", "" + 4);
        map.put("NUM_PREC_RADIX", "" + 10);
        map.put("NULLABLE", "" + DatabaseMetaData.columnNullable);
        map.put("REMARKS", null);

        map.put("COLUMN_DEF", null);
        map.put("SQL_DATA_TYPE", "" + 0);
        map.put("SQL_DATETIME_SUB", "" + 0);
        map.put("CHAR_OCTET_LENGTH", "" + 16);
        map.put("ORDINAL_POSITION", "" + ordinalIndex);

        map.put("SCOPE_CATALOG", null);
        map.put("SCOPE_SCHEMA", null);
        map.put("SCOPE_TABLE", null);
        map.put("SOURCE_DATA_TYPE", null);
        map.put("IS_AUTOINCREMENT", "NO");
        map.put("IS_GENERATEDCOLUMN", "NO");
        return map;
    }

    public String getName() {
        return name;
    }

    public ESqlDataType getDataType() {
        return dataType;
    }
}
