package com.jdbcly.core.metadata;

import com.jdbcly.core.ResultItem;
import com.jdbcly.engine.Context;
import com.jdbcly.jdbc.JdbclyColumn;
import com.jdbcly.jdbc.JdbclyTable;

/**
 * Date: 6/27/2020
 *
 * ColumnsDefinition handles the reporting of the available columns, and their respective info.
 * Columns are cached on a per table basis.
 */
public abstract class ColumnsDefinition {
    protected final Context context;
    private ResultItem<JdbclyColumn[]> cache = new ResultItem<>();

    public ColumnsDefinition(Context context) {
        this.context = context;
    }

    public static String[] getMetadataLabels() {
        return new String[]{
                "TABLE_CAT",
                "TABLE_SCHEM",
                "TABLE_NAME",
                "COLUMN_NAME",
                "DATA_TYPE",
                "TYPE_NAME",
                "COLUMN_SIZE",
                "BUFFER_LENGTH",
                "DECIMAL_DIGITS",
                "NUM_PREC_RADIX",
                "NULLABLE",
                "REMARKS",
                "COLUMN_DEF",
                "SQL_DATA_TYPE",
                "SQL_DATETIME_SUB",
                "CHAR_OCTET_LENGTH",
                "ORDINAL_POSITION",
                "SCOPE_CATALOG",
                "SCOPE_SCHEMA",
                "SCOPE_TABLE",
                "SOURCE_DATA_TYPE",
                "IS_AUTOINCREMENT",
                "IS_GENERATEDCOLUMN",
        };
    }

    protected abstract JdbclyColumn[] getColumnsInternal(JdbclyTable table);

    public synchronized JdbclyColumn[] getColumns(JdbclyTable table) {
        JdbclyColumn[] cached = cache.get(table.getName());
        if (cached != null) {
            return cached;
        }

        JdbclyColumn[] columns = getColumnsInternal(table);
        cache.put(table.getName(), columns);
        return columns;
    }
}
