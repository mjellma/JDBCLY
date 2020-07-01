package com.jdbcly.files;

import com.jdbcly.core.*;
import com.jdbcly.engine.Context;
import com.jdbcly.engine.ResultItem;
import com.jdbcly.jdbc.JdbclyColumn;
import com.jdbcly.jdbc.JdbclyTable;

import java.util.List;

/**
 * Date: 6/27/2020
 */
public class FileColumnsDefinition extends ColumnsDefinition {

    public FileColumnsDefinition(Context context) {
        super(context);
    }

    @Override
    protected JdbclyColumn[] getColumnsInternal(JdbclyTable table) {
        String uri = context.getConnection().getProperty(Properties.URI);
        int scanDepth = context.getConnection().getProperty(Properties.SCAN_DEPTH);

        List<ResultItem<?>> rows = context.getParser().parse(new FileReader(uri), scanDepth);
        ResultItem<ESqlDataType> dataTypes = JdbcUtils.determineColumnTypes(rows);

        String[] columnNames = rows.get(0).keysArray();
        JdbclyColumn[] columns = new JdbclyColumn[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) {
            columns[i] = new JdbclyColumn(table, columnNames[i], dataTypes.get(columnNames[i]), i + 1);
        }
        return columns;
    }
}
