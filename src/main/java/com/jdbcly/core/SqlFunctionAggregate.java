package com.jdbcly.core;

import com.jdbcly.engine.AggregateFunctionEvaluator;
import com.jdbcly.engine.Row;

import java.util.List;

/**
 * Date: 7/9/2020
 */
public class SqlFunctionAggregate extends SqlExpression {
    private AggregateFunctionEvaluator evaluator;

    public SqlFunctionAggregate(String name, AggregateFunctionEvaluator evaluator) {
        super(name);
        this.evaluator = evaluator;
    }

    public Comparable evaluate(ResultItem<Integer> columnSqlIndices, List<Row> rows) {
        return evaluator.evaluate(columnSqlIndices, rows);
    }
}
