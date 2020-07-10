package com.jdbcly.core;

/**
 * Date: 7/9/2020
 */
public class SqlColumn extends SqlExpression {
    private Comparable value;

    public SqlColumn(String name) {
        this(name, null);
    }

    public SqlColumn(String name, String alias) {
        super(name, alias);
    }

    public Comparable getValue() {
        return value;
    }

    public void setValue(Comparable value) {
        this.value = value;
    }
}
