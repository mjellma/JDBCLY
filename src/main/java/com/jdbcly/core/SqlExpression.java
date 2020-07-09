package com.jdbcly.core;

/**
 * Date: 6/28/2020
 */
public abstract class SqlExpression {
    private String name;

    public SqlExpression(String name) {
        this.name = name.replace("`", "");
    }

    public String getName() {
        return name;
    }
}
