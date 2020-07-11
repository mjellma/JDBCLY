package com.jdbcly.engine;

import com.jdbcly.core.ResultItem;
import com.jdbcly.core.SqlColumn;

import java.util.List;
import java.util.Objects;

/**
 * Date: 7/8/2020
 */
public abstract class AggregateFunctionEvaluator {

    public abstract Comparable evaluate(ResultItem<Integer> columnSqlIndices, List<Row> rows);

    public static class Count extends AggregateFunctionEvaluator {
        // null if COUNT(*)
        private SqlColumn column;

        public Count(SqlColumn column) {
            this.column = column;
        }

        @Override
        public Comparable evaluate(ResultItem<Integer> columnSqlIndices, List<Row> rows) {
            if (column == null) {
                return rows.size();
            }

            int index = columnSqlIndices.get(column.getNameAlias());
            return rows.stream().filter(r -> r.getValue(index) != null).count();
        }
    }

    public static class Min extends AggregateFunctionEvaluator {
        private SqlColumn column;

        public Min(SqlColumn column) {
            this.column = column;
        }

        @Override
        public Comparable evaluate(ResultItem<Integer> columnSqlIndices, List<Row> rows) {
            int index = columnSqlIndices.get(column.getNameAlias());
            return rows.stream().map(r -> r.getValue(index)).filter(Objects::nonNull).min(Comparable::compareTo).orElse(0);
        }
    }

    public static class Max extends AggregateFunctionEvaluator {
        private SqlColumn column;

        public Max(SqlColumn column) {
            this.column = column;
        }

        @Override
        public Comparable evaluate(ResultItem<Integer> columnSqlIndices, List<Row> rows) {
            int index = columnSqlIndices.get(column.getNameAlias());
            return rows.stream().map(r -> r.getValue(index)).filter(Objects::nonNull).max(Comparable::compareTo).orElse(0);
        }
    }

    public static class Sum extends AggregateFunctionEvaluator {
        private SqlColumn column;

        public Sum(SqlColumn column) {
            this.column = column;
        }

        @Override
        public Comparable evaluate(ResultItem<Integer> columnSqlIndices, List<Row> rows) {
            int index = columnSqlIndices.get(column.getNameAlias());
            double sum = 0;
            Comparable val;
            for (Row row : rows) {
                val = row.getValue(index);
                if (val == null) continue;
                if (val instanceof Number) {
                    sum += ((Number) val).doubleValue();
                }
            }
            return sum;
        }
    }

    public static class Average extends AggregateFunctionEvaluator {
        private SqlColumn column;

        public Average(SqlColumn column) {
            this.column = column;
        }

        @Override
        public Comparable evaluate(ResultItem<Integer> columnSqlIndices, List<Row> rows) {
            Sum sumF = new Sum(column);
            Double sum = (Double) sumF.evaluate(columnSqlIndices, rows);
            Count countF = new Count(column);
            Long count = (Long) countF.evaluate(columnSqlIndices, rows);
            return sum / count;
        }
    }
}
