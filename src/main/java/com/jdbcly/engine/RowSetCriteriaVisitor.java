package com.jdbcly.engine;

import com.jdbcly.core.SelectStatement;

/**
 * Date: 6/28/2020
 */
public class RowSetCriteriaVisitor implements RowSetVisitor {

    private final SelectStatement select;

    public RowSetCriteriaVisitor(SelectStatement select) {
        this.select = select;
    }

    @Override
    public void visit(RowSet rowSet) {
        if (select.getCriteria() == null) return;

        rowSet.filter(row -> {
            CriteriaUtils.evaluateColumnValues(rowSet, row, select.getCriteria());
            return ((Criteria.BinaryCriteria) select.getCriteria()).evaluate();
        });
    }
}
