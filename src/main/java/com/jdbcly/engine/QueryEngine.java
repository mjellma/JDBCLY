package com.jdbcly.engine;

import com.jdbcly.core.ResultItem;
import com.jdbcly.core.SelectOperation;
import com.jdbcly.core.SelectStatement;
import com.jdbcly.jdbc.JdbclyColumn;
import com.jdbcly.utils.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 6/27/2020
 * <p>
 * QueryEngine is responsible for processing the results.
 */
public class QueryEngine {

    private final SelectStatement select;
    private final JdbclyColumn[] columns;
    private final List<ResultItem<?>> results = new LinkedList<>();

    public QueryEngine(SelectStatement select, JdbclyColumn[] columns) {
        this.select = select;
        this.columns = columns;
    }

    public void executeQuery(SelectOperation selectOperation) {
        selectOperation.execute(this);
    }

    public void processItem(ResultItem item) {
        // values must first be converted to their declared types
        for (JdbclyColumn column : columns) {
            item.put(column.getName(), Utils.convert(item.get(column.getName()), column.getDataType()));
        }

        results.add(item);
    }

    public void processRowSet(RowSet rowSet) {
        rowSet.visit(new RowSetCriteriaVisitor(select));
        rowSet.visit(new RowSetAggregateVisitor(select));
        rowSet.visit(new RowSetOrderByVisitor(select));
        rowSet.visit(new RowSetLimitOffsetVisitor(select));
        rowSet.visit(new RowSetProjectionVisitor(select));
    }

    public SelectStatement getSelect() {
        return select;
    }

    public List<ResultItem<?>> getResults() {
        return results;
    }
}
