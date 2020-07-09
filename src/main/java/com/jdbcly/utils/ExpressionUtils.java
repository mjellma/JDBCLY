package com.jdbcly.utils;

import com.jdbcly.core.SqlColumn;
import com.jdbcly.core.SqlExpression;
import com.jdbcly.core.SqlFunctionAggregate;
import com.jdbcly.engine.AggregateFunctionEvaluator;
import com.jdbcly.exceptions.NotSupportedException;
import net.sf.jsqlparser.expression.Function;

/**
 * Date: 7/9/2020
 */
public class ExpressionUtils {
    public static SqlExpression mapJsqlExpression(net.sf.jsqlparser.expression.Expression exp) {
        if (exp instanceof net.sf.jsqlparser.schema.Column) {
            return new SqlColumn(((net.sf.jsqlparser.schema.Column) exp).getColumnName());
        } else if (exp instanceof Function) {
            return mapJsqlFunction((Function) exp);
        } else {
            throw new NotSupportedException("Only columns are supported in projection. " + exp);
        }
    }

    public static SqlExpression mapJsqlFunction(net.sf.jsqlparser.expression.Function fun) {
        String name = fun.getName().toLowerCase();

        String[] parameters = null;
        if (fun.getParameters() != null) {
            parameters = fun.getParameters().getExpressions().stream().map(f -> ((net.sf.jsqlparser.schema.Column) f).getColumnName()).toArray(String[]::new);
        }

        // aggregate functions
        switch (name) {
            case "count":
                SqlColumn column = parameters == null ? null : new SqlColumn(parameters[0]);
                return new SqlFunctionAggregate(name, new AggregateFunctionEvaluator.Count(column));

            case "min":
                return new SqlFunctionAggregate(name, new AggregateFunctionEvaluator.Min(new SqlColumn(parameters[0])));

            case "max":
                return new SqlFunctionAggregate(name, new AggregateFunctionEvaluator.Max(new SqlColumn(parameters[0])));

            case "sum":
                return new SqlFunctionAggregate(name, new AggregateFunctionEvaluator.Sum(new SqlColumn(parameters[0])));
        }

        throw new RuntimeException("Function not yet supported: " + name);
    }
}
