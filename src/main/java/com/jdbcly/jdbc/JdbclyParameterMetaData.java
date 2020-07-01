package com.jdbcly.jdbc;

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Date: 6/29/2020
 */
public class JdbclyParameterMetaData implements ParameterMetaData {

    private HashMap<Integer, Object> params;

    public JdbclyParameterMetaData(HashMap<Integer, Object> params) {
        this.params = params;
    }

    @Override
    public int getParameterCount() throws SQLException {
        return params.size();
    }

    @Override
    public int isNullable(int param) throws SQLException {
        return ParameterMetaData.parameterNullableUnknown;
    }

    @Override
    public boolean isSigned(int param) throws SQLException {
        return true;
    }

    @Override
    public int getPrecision(int param) throws SQLException {
        return 0;
    }

    @Override
    public int getScale(int param) throws SQLException {
        return 0;
    }

    @Override
    public int getParameterType(int param) throws SQLException {
        return 0;
    }

    @Override
    public String getParameterTypeName(int param) throws SQLException {
        return "";
    }

    @Override
    public String getParameterClassName(int param) throws SQLException {
        return "";
    }

    @Override
    public int getParameterMode(int param) throws SQLException {
        return 0;
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
