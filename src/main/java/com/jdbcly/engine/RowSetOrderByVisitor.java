package com.jdbcly.engine;

import com.jdbcly.core.SelectStatement;

import java.util.List;

/**
 * Date: 6/27/2020
 */
public class RowSetOrderByVisitor implements RowSetVisitor {

    private final SelectStatement select;

    public RowSetOrderByVisitor(SelectStatement select) {
        this.select = select;
    }

    @Override
    public void visit(RowSet rowSet) {
        if (select.getOrderBy().isEmpty()) return;

        List<SelectStatement.OrderBy> order = select.getOrderBy();
        rowSet.reorderRows((row1, row2) -> {
            for (int i = 0; i < order.size(); i++) {
                SelectStatement.OrderBy orderBy = order.get(i);
                int columnIndex = rowSet.getColumnIndex(orderBy.getExpression().getName());
                Comparable o1 = row1.getValue(columnIndex);
                Comparable o2 = row2.getValue(columnIndex);

                int comparison = o1.compareTo(o2);
                if (comparison == 0) continue;

                if (!orderBy.isAsc()) {
                    comparison *= -1;
                }
                return comparison;
            }

            return 0;
        });
    }
}