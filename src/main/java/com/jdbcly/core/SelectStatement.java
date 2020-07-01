package com.jdbcly.core;

import com.jdbcly.engine.Criteria;
import com.jdbcly.engine.CriteriaUtils;
import com.jdbcly.exceptions.NotSupportedException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 6/27/2020
 */
public class SelectStatement {

    private final PlainSelect select;

    private String table;
    private List<SqlColumn> projection = new ArrayList<>();
    private List<OrderBy> orderBy = new ArrayList<>();
    private Criteria criteria = null;
    private int limit = 0, offset = 0;

    private SelectStatement(PlainSelect select) {
        this.select = select;
    }

    public static SelectStatement from(String sql) throws SQLException {
        PlainSelect select;
        try {
            select = (PlainSelect) ((Select) CCJSqlParserUtil.parse(sql)).getSelectBody();
        } catch (JSQLParserException e) {
            throw new SQLException(e.getCause().getMessage());
        } catch (Exception e) {
            throw new SQLException("Statement could not be parsed.");
        }

        SelectStatement statement = new SelectStatement(select);
        statement.assertSupportedFunctionality();

        statement.determineTable();
        statement.determineProjection();
        statement.determineLimitOffset();
        statement.determineOrderBy();
        statement.criteria = statement.determineCriteria(select.getWhere());

        return statement;
    }

    private void determineTable() {
        table = ((Table) select.getFromItem()).getName();
    }

    private void determineProjection() {
        SelectItemVisitor visitor = new SelectItemVisitor() {
            @Override
            public void visit(AllColumns allColumns) {
            }

            @Override
            public void visit(AllTableColumns allTableColumns) {
            }

            @Override
            public void visit(SelectExpressionItem selectExpressionItem) {
                if (selectExpressionItem.getExpression() instanceof Column) {
                    projection.add(new SqlColumn(((Column) selectExpressionItem.getExpression()).getColumnName()));
                } else {
                    throw new NotSupportedException("Only columns are supported in projection. " + selectExpressionItem);
                }
            }
        };
        select.getSelectItems().forEach(i -> i.accept(visitor));
    }

    private void determineLimitOffset() {
        if (select.getLimit() == null) return;

        limit = (int) ((LongValue) select.getLimit().getRowCount()).getValue();
        if (select.getOffset() != null) {
            offset = (int) select.getOffset().getOffset();
        }
    }

    private void determineOrderBy() {
        for (OrderByElement exp : Utils.getNonNull(select.getOrderByElements())) {
            if (exp.getExpression() instanceof Column) {
                orderBy.add(new OrderBy(((Column) exp.getExpression()).getColumnName(), exp.isAsc()));
            } else {
                throw new NotSupportedException("Only columns are supported in order by. " + exp);
            }
        }
    }

    private Criteria determineCriteria(Expression node) {
        if (node == null) return null;
        if (node instanceof ComparisonOperator) {
            ComparisonOperator c = (ComparisonOperator) node;

            Criteria left = CriteriaUtils.mapToCriteria(c.getLeftExpression());
            Criteria right = CriteriaUtils.mapToCriteria(c.getRightExpression());
            return new Criteria.Comparison(left, right, CriteriaUtils.mapOperatorType(c));
        }

        if (node instanceof AndExpression) {
            AndExpression a = (AndExpression) node;
            return new Criteria.And(determineCriteria(a.getLeftExpression()), determineCriteria(a.getRightExpression()));
        }

        if (node instanceof OrExpression) {
            OrExpression o = (OrExpression) node;
            return new Criteria.Or(determineCriteria(o.getLeftExpression()), determineCriteria(o.getRightExpression()));
        }

        throw new NotSupportedException("Unsupported criteria node: " + node);
    }

    public void assertSupportedFunctionality() {
        if (select.getGroupBy() != null) {
            throw new NotSupportedException("Group by is not supported.");
        }

        if (!Utils.getNonNull(select.getJoins()).isEmpty()) {
            throw new NotSupportedException("Joins are not supported.");
        }
    }

    public String getTable() {
        return table;
    }

    public List<SqlColumn> getProjection() {
        return projection;
    }

    public List<OrderBy> getOrderBy() {
        return orderBy;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public static class OrderBy {
        private SqlColumn column;
        private boolean asc;

        public OrderBy(String column, boolean asc) {
            this.column = new SqlColumn(column);
            this.asc = asc;
        }

        public SqlColumn getColumn() {
            return column;
        }

        public boolean isAsc() {
            return asc;
        }
    }
}
