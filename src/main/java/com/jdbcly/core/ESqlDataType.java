package com.jdbcly.core;

import java.sql.Types;

/**
 * Date: 7/1/2020
 * <p>
 * Represents the supported SQL data types.
 */
public enum ESqlDataType {
    INTEGER(Types.INTEGER, "INTEGER", Integer.class),
    DOUBLE(Types.DOUBLE, "DOUBLE", Double.class),
    VARCHAR(Types.VARCHAR, "VARCHAR", String.class);

    private int sqlType;
    private String sqlTypeName;
    private Class dataClass;

    ESqlDataType(int sqlType, String sqlTypeName, Class dataClass) {
        this.sqlType = sqlType;
        this.sqlTypeName = sqlTypeName;
        this.dataClass = dataClass;
    }

    public int getSqlType() {
        return sqlType;
    }

    public String getSqlTypeName() {
        return sqlTypeName;
    }

    public Class getDataClass() {
        return dataClass;
    }

    public boolean isAssignableTo(ESqlDataType type) {
        if (this == VARCHAR) return type == VARCHAR;
        if (this == DOUBLE) return type == DOUBLE || type == VARCHAR;
        return this == INTEGER;
    }

    public Object parseString(String value) {
        if (this == VARCHAR) return value;
        if (this == DOUBLE) return Double.parseDouble(value);
        if (this == INTEGER) return Integer.parseInt(value);
        return null;
    }
}
