package com.jdbcly.core.metadata;

import com.jdbcly.engine.Context;
import com.jdbcly.jdbc.JdbclyTable;

/**
 * Date: 6/27/2020
 */
public abstract class TablesDefinition {

    protected final Context context;

    public TablesDefinition(Context context) {
        this.context = context;
    }

    public static String[] getMetadataLabels() {
        return new String[]{
                "TABLE_CAT",
                "TABLE_SCHEM",
                "TABLE_NAME",
                "TABLE_TYPE",
                "REMARKS",
                "TYPE_CAT",
                "TYPE_SCHEM",
                "TYPE_NAME",
                "SELF_REFERENCING_COL_NAME",
                "REF_GENERATION"
        };
    }

    public abstract JdbclyTable[] getTables(String schema, String catalog);
}
