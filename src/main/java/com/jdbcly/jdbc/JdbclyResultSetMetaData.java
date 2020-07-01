package com.jdbcly.jdbc;

import com.jdbcly.engine.RowSet;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Date: 6/27/2020
 */
public class JdbclyResultSetMetaData implements ResultSetMetaData {

    private final RowSet rowSet;
    private final JdbclyTable table;

    public JdbclyResultSetMetaData(RowSet rowSet, JdbclyTable table) {
        this.rowSet = rowSet;
        this.table = table;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return rowSet.getColumnCount();
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        return false;
    }

    @Override
    public int isNullable(int column) throws SQLException {
        return ResultSetMetaData.columnNullable;
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        return true;
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        return 0;
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        return rowSet.getColumnName(column);
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return rowSet.getColumnName(column);
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        return table.getSchema();
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        return 0;
    }

    @Override
    public int getScale(int column) throws SQLException {
        return 0;
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return table.getName();
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        return table.getCatalog();
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        return 0;
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        return null;
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        return true;
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        return false;
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
