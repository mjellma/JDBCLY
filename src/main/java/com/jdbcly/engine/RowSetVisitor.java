package com.jdbcly.engine;

/**
 * Date: 6/27/2020
 */
public interface RowSetVisitor {
    void visit(RowSet rowSet);
}
