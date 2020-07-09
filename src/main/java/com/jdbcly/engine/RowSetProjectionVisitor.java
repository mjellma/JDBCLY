package com.jdbcly.engine;

import com.jdbcly.core.SelectStatement;
import com.jdbcly.core.SqlExpression;

import java.util.List;

/**
 * Date: 6/27/2020
 */
public class RowSetProjectionVisitor implements RowSetVisitor {

    private SelectStatement select;

    public RowSetProjectionVisitor(SelectStatement select) {
        this.select = select;
    }

    @Override
    public void visit(RowSet rowSet) {
        if (select.getProjection().isEmpty()) return;

        List<SqlExpression> columns = select.getProjection();
        String[] labels = columns.stream().map(SqlExpression::getName).toArray(String[]::new);

        rowSet.narrowProjection(labels);
    }
}