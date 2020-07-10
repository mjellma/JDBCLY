package com.jdbcly.utils;

import com.jdbcly.core.SqlColumn;
import com.jdbcly.core.SqlExpression;
import com.jdbcly.core.SqlFunctionAggregate;
import com.jdbcly.engine.AggregateFunctionEvaluator;
import com.jdbcly.exceptions.NotSupportedException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;

/**
 * Date: 7/9/2020
 */
public class ExpressionUtils {
    public static SqlExpression mapJsqlExpression(net.sf.jsqlparser.expression.Expression exp, Alias alias) {
        if (exp instanceof Column) {
            return new SqlColumn(((Column) exp).getColumnName(), getAlias(alias));
        } else if (exp instanceof Function) {
            return mapJsqlFunction((Function) exp, alias);
        } else {
            throw new NotSupportedException("Only columns are supported in projection. " + exp);
        }
    }

    public static SqlExpression mapJsqlFunction(net.sf.jsqlparser.expression.Function fun, Alias alias) {
        String name = fun.getName().toLowerCase();

        String[] parameters = null;
        if (fun.getParameters() != null) {
            parameters = fun.getParameters().getExpressions().stream().map(f -> ((net.sf.jsqlparser.schema.Column) f).getColumnName()).toArray(String[]::new);
        }

        String aliasName = getAlias(alias);

        // aggregate functions
        switch (name) {
            case "count":
                SqlColumn column = parameters == null ? null : new SqlColumn(parameters[0]);
                return new SqlFunctionAggregate(name, aliasName, new AggregateFunctionEvaluator.Count(column));

            case "min":
                return new SqlFunctionAggregate(name, aliasName, new AggregateFunctionEvaluator.Min(new SqlColumn(parameters[0])));

            case "max":
                return new SqlFunctionAggregate(name, aliasName, new AggregateFunctionEvaluator.Max(new SqlColumn(parameters[0])));

            case "sum":
                return new SqlFunctionAggregate(name, aliasName, new AggregateFunctionEvaluator.Sum(new SqlColumn(parameters[0])));
        }

        throw new RuntimeException("Function not yet supported: " + name);
    }

    private static String getAlias(Alias alias) {
        if (alias != null) return alias.getName();
        return null;
    }
}
