package com.jdbcly.files;

import com.jdbcly.core.Constants;
import com.jdbcly.core.metadata.TablesDefinition;
import com.jdbcly.engine.Context;
import com.jdbcly.jdbc.JdbclyTable;

/**
 * Date: 6/27/2020
 */
public class FileTablesDefinition extends TablesDefinition {

    public FileTablesDefinition(Context context) {
        super(context);
    }

    @Override
    public JdbclyTable[] getTables(String schema, String catalog) {
        return new JdbclyTable[]{
                new JdbclyTable(Constants.Tables.CATALOG, Constants.Tables.SCHEMA, Constants.Tables.TABLE, Constants.Tables.TABLE)
        };
    }
}
