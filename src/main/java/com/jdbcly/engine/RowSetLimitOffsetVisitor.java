package com.jdbcly.engine;

import com.jdbcly.core.SelectStatement;

/**
 * Date: 6/27/2020
 */
public class RowSetLimitOffsetVisitor implements RowSetVisitor {

    private final SelectStatement select;

    public RowSetLimitOffsetVisitor(SelectStatement select) {
        this.select = select;
    }

    @Override
    public void visit(RowSet rowSet) {
        if (select.getLimit() == 0) return;
        rowSet.limitOffset(select.getLimit(), select.getOffset());
    }
}
