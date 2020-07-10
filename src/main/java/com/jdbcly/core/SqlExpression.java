package com.jdbcly.core;

/**
 * Date: 6/28/2020
 */
public abstract class SqlExpression {
    private String name;
    private String alias;

    public SqlExpression(String name, String alias) {
        this.name = name.replace("`", "");
        if (alias != null) {
            this.alias = alias.replace("`", "");
        }
    }

    /**
     * Represents the original name of the expression, ie column name, function name etc.
     */
    public String getName() {
        return name;
    }

    /**
     * Represents the name to be used when referring to the expression.
     * All operations will be based on this name/alias.
     */
    public String getNameAlias() {
        if (alias == null) return name;
        return alias;
    }

    public boolean hasAlias() {
        return alias != null;
    }
}
