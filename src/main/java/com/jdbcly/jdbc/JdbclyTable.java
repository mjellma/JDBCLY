package com.jdbcly.jdbc;

import com.jdbcly.engine.ResultItem;

/**
 * Date: 6/27/2020
 */
public class JdbclyTable {

    private String catalog;
    private String schema;
    private String name;
    private String type;

    public JdbclyTable(String catalog, String schema, String name, String type) {
        this.catalog = catalog;
        this.schema = schema;
        this.name = name;
        this.type = type;
    }

    public ResultItem<String> getMetadataValues() {
        ResultItem<String> map = new ResultItem<>();
        map.put("TABLE_CAT", catalog);
        map.put("TABLE_SCHEM", schema);
        map.put("TABLE_NAME", name);
        map.put("TABLE_TYPE", type);

        map.put("REMARKS", null);
        map.put("TYPE_CAT", null);
        map.put("TYPE_SCHEM", null);
        map.put("TYPE_NAME", null);
        map.put("SELF_REFERENCING_COL_NAME", null);
        map.put("REF_GENERATION", null);
        return map;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
