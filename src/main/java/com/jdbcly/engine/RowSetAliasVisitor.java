package com.jdbcly.engine;

import com.jdbcly.core.ResultItem;
import com.jdbcly.core.SelectStatement;
import com.jdbcly.core.SqlExpression;

/**
 * Date: 7/10/2020
 */
public class RowSetAliasVisitor implements RowSetVisitor {

    private final SelectStatement select;

    public RowSetAliasVisitor(SelectStatement select) {
        this.select = select;
    }

    @Override
    public void visit(RowSet rowSet) {
        ResultItem<Integer> indices = rowSet.getColumnSqlIndices();
        for (SqlExpression exp : select.getProjection()) {
            Integer originalIndex = indices.get(exp.getName());
            // may be null for functions
            if (exp.hasAlias() && originalIndex != null) {
                indices.put(exp.getNameAlias(), originalIndex);
                indices.remove(exp.getName());
            }
        }
    }
}
