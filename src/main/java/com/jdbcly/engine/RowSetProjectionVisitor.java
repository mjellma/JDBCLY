package com.jdbcly.engine;

import com.jdbcly.core.SelectStatement;
import com.jdbcly.core.SqlColumn;

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

        List<SqlColumn> columns = select.getProjection();
        String[] labels = columns.stream().map(SqlColumn::getName).toArray(String[]::new);

        Comparable[] values;
        for (Row row : rowSet.rows) {
            values = new Comparable[labels.length];
            for (int i = 0; i < labels.length; i++) {
                int index = rowSet.getColumnIndex(labels[i]);
                values[i] = row.getValue(index);
            }
            row.setValues(values);
        }

        rowSet.labels = labels;
        rowSet.columnSqlIndices.clear();
        for (int i = 0; i < labels.length; i++) {
            rowSet.columnSqlIndices.put(labels[i], i + 1);
        }
    }
}