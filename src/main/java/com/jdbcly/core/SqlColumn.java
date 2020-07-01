package com.jdbcly.core;

/**
 * Date: 6/28/2020
 */
public class SqlColumn {
    private String name;
    // may be extended with an Evaluatable interface -> value.evaluate() to support functions
    private Object value;

    public SqlColumn(String name) {
        this.name = name.replace("`", "");
    }

    public String getName() {
        return name;
    }

    public Comparable getValue() {
        return (Comparable) value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
