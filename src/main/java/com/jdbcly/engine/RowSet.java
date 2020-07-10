package com.jdbcly.engine;

import com.jdbcly.core.ResultItem;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Date: 6/27/2020
 */
public class RowSet implements Closeable {

    // zero-indexed
    private int currentRow = -1;

    // indexed values start from 1
    private ResultItem<Integer> columnSqlIndices = new ResultItem<>();
    private String[] labels;
    private ArrayList<Row> rows;

    private RowSet(String[] labels, List<ResultItem<?>> rows) {
        this.labels = labels;

        for (int i = 0; i < labels.length; i++) {
            columnSqlIndices.put(labels[i], i + 1);
        }

        this.rows = rows.stream()
                .map(values -> new Row(Arrays.stream(labels).map(values::get).toArray(Comparable[]::new)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static RowSet create(String[] labels, List<ResultItem<?>> rows) {
        return new RowSet(labels, rows);
    }

    @Override
    public void close() throws IOException {
        columnSqlIndices.clear();
        labels = null;
        rows.clear();
    }

    public void visit(RowSetVisitor visitor) {
        visitor.visit(this);
    }

    ResultItem<Integer> getColumnSqlIndices() {
        return columnSqlIndices;
    }

    ArrayList<Row> getRows() {
        return rows;
    }

    public boolean next() {
        return currentRow++ != rows.size() - 1;
    }

    public int size() {
        return rows.size();
    }

    public <T> T getValue(String label) {
        return getValue(columnSqlIndices.get(label));
    }

    public <T> T getValue(int sqlIndex) {
        return (T) rows.get(currentRow).getValue(sqlIndex);
    }

    public String getString(String label) {
        return getString(columnSqlIndices.get(label));
    }

    public String getString(int sqlIndex) {
        return String.valueOf(rows.get(currentRow).getValue(sqlIndex));
    }

    public int getColumnCount() {
        return labels.length;
    }

    public String getColumnName(int sqlIndex) {
        return labels[sqlIndex - 1];
    }

    public int getColumnIndex(String name) {
        Integer index = columnSqlIndices.get(name);
        if (index == null) {
            throw new RuntimeException("Column does not exist: " + name);
        }
        return index;
    }

    public int getCurrentRowSqlIndexed() {
        return currentRow + 1;
    }

    // External manipulation methods
    void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

    void reorderRows(Comparator<Row> comparator) {
        rows.sort(comparator);
    }

    void limitOffset(int limit, int offset) {
        rows = rows.stream().skip(offset).limit(limit).collect(Collectors.toCollection(ArrayList::new));
    }

    void filter(Predicate<Row> filter) {
        rows = rows.stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
    }

    void narrowProjection(String[] labels) {
        Comparable[] values;
        for (Row row : rows) {
            values = new Comparable[labels.length];
            for (int i = 0; i < labels.length; i++) {
                int index = getColumnIndex(labels[i]);
                values[i] = row.getValue(index);
            }
            row.setValues(values);
        }

        this.labels = labels;
        columnSqlIndices.clear();
        for (int i = 0; i < labels.length; i++) {
            columnSqlIndices.put(labels[i], i + 1);
        }
    }
}