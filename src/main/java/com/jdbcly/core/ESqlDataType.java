package com.jdbcly.core;

import java.sql.Types;

/**
 * Date: 7/1/2020
 */
public enum ESqlDataType {
    VARCHAR(Types.VARCHAR, "VARCHAR", String.class),
    DOUBLE(Types.DOUBLE, "DOUBLE", Double.class);

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
}
