package com.jdbcly.engine;

import com.jdbcly.core.SelectOperation;
import com.jdbcly.files.IParser;
import com.jdbcly.jdbc.JdbclyColumn;
import com.jdbcly.jdbc.JdbclyTable;

/**
 * Date: 6/27/2020
 */
public abstract class Context {

    protected final Connection connection;

    public Context(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public abstract JdbclyTable[] getTables(String schema, String catalog);

    public abstract JdbclyColumn[] getColumns(JdbclyTable table);

    public abstract IParser getParser();

    public abstract SelectOperation getSelectOperation();
}
