package com.jdbcly.engine;

import com.jdbcly.core.SqlColumn;
import com.jdbcly.exceptions.NotSupportedException;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;

/**
 * Date: 6/28/2020
 */
public class CriteriaUtils {

    public static Criteria mapToCriteria(Expression e) {
        if (e instanceof Column) {
            return new Criteria.Column(new SqlColumn(((Column) e).getColumnName()));
        }
        if (e instanceof StringValue) {
            return new Criteria.Value(((StringValue) e).getValue());
        }
        if (e instanceof LongValue) {
            return new Criteria.Value(((LongValue) e).getValue());
        }
        if (e instanceof DoubleValue) {
            return new Criteria.Value(((DoubleValue) e).getValue());
        }
        throw new NotSupportedException("Type is not yet supported in criteria: " + e.toString());
    }

    public static CriteriaEvaluator mapOperatorType(ComparisonOperator e) {
        if (e instanceof GreaterThan) {
            return CriteriaEvaluator.BIGGER;
        }
        if (e instanceof GreaterThanEquals) {
            return CriteriaEvaluator.BIGGER_EQUAL;
        }
        if (e instanceof MinorThan) {
            return CriteriaEvaluator.SMALLER;
        }
        if (e instanceof MinorThanEquals) {
            return CriteriaEvaluator.SMALLER_EQUAL;
        }
        if (e instanceof EqualsTo) {
            return CriteriaEvaluator.EQUAL;
        }
        if (e instanceof NotEqualsTo) {
            return CriteriaEvaluator.NOT_EQUAL;
        }
        throw new NotSupportedException("Operator not supported in criteria: " + e.getStringExpression());
    }

    public static void evaluateColumnValues(RowSet rowSet, Row row, Criteria criteria) {
        if (criteria instanceof Criteria.Comparison) {
            Criteria.Comparison c = (Criteria.Comparison) criteria;
            if (c.left instanceof Criteria.Column) {
                SqlColumn column = ((Criteria.Column) c.left).getColumn();
                Comparable value = row.getValue(rowSet.getColumnIndex(column.getNameAlias()));
                column.setValue(value);
            }
            if (c.right instanceof Criteria.Column) {
                SqlColumn column = ((Criteria.Column) c.right).getColumn();
                Comparable value = row.getValue(rowSet.getColumnIndex(column.getNameAlias()));
                column.setValue(value);
            }

        } else if (criteria instanceof Criteria.And) {
            Criteria.And c = (Criteria.And) criteria;
            evaluateColumnValues(rowSet, row, c.left);
            evaluateColumnValues(rowSet, row, c.right);

        } else if (criteria instanceof Criteria.Or) {
            Criteria.Or c = (Criteria.Or) criteria;
            evaluateColumnValues(rowSet, row, c.left);
            evaluateColumnValues(rowSet, row, c.right);

        }
    }
}
