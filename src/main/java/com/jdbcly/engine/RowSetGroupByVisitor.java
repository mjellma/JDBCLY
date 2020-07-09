package com.jdbcly.engine;

import com.jdbcly.core.SelectStatement;
import com.jdbcly.core.SqlFunctionAggregate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Date: 7/8/2020
 */
public class RowSetGroupByVisitor implements RowSetVisitor {

    private final SelectStatement select;

    public RowSetGroupByVisitor(SelectStatement select) {
        this.select = select;
    }

    @Override
    public void visit(RowSet rowSet) {
        List<SqlFunctionAggregate> aggregateFunctions = select.getProjection().stream()
                .filter(c -> c instanceof SqlFunctionAggregate)
                .map(c -> (SqlFunctionAggregate) c)
                .collect(Collectors.toList());

        if (select.getGroupBy().isEmpty()) {
            evaluateUngrouped(rowSet, aggregateFunctions);
        } else {
            evaluateGrouped(rowSet, aggregateFunctions);
        }
    }

    private void evaluateUngrouped(RowSet rowSet, List<SqlFunctionAggregate> aggregateFunctions) {
        for (SqlFunctionAggregate function : aggregateFunctions) {
            int sqlIndex = rowSet.getColumnIndex(function.getName());

            // no grouping is performed, same value is used
            Comparable value = function.evaluate(rowSet.getColumnSqlIndices(), rowSet.getRows());

            for (Row row : rowSet.getRows()) {
                row.setValue(value, sqlIndex);
            }
        }
    }

    private void evaluateGrouped(RowSet rowSet, List<SqlFunctionAggregate> aggregateFunctions) {
        Map<List<Comparable>, List<Row>> groups = performGrouping(rowSet);
        ArrayList<Row> results = new ArrayList<>(2 >> 8);

        Row newVal;
        for (List<Row> rows : groups.values()) {
            newVal = rows.get(0).copy();
            results.add(newVal);

            for (SqlFunctionAggregate function : aggregateFunctions) {
                int sqlIndex = rowSet.getColumnIndex(function.getName());
                newVal.setValue(function.evaluate(rowSet.getColumnSqlIndices(), rows), sqlIndex);
            }
        }

        rowSet.setRows(results);
    }

    private Map<List<Comparable>, List<Row>> performGrouping(RowSet rowSet) {
        String[] columns = select.getGroupBy().stream().map(c -> c.getExpression().getName()).toArray(String[]::new);
        Map<List<Comparable>, List<Row>> groups = new HashMap<>();
        List<Comparable> key; // TODO: 7/9/2020 should use an immutable collection
        List<Row> groupedRows;
        for (Row row : rowSet.getRows()) {
            key = new ArrayList<>(columns.length);
            for (int i = 0; i < columns.length; i++) {
                key.add(row.getValue(rowSet.getColumnIndex(columns[i])));
            }

            groupedRows = groups.computeIfAbsent(key, k -> new ArrayList<>());
            groupedRows.add(row);
        }
        return groups;
    }
}
