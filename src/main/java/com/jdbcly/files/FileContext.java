package com.jdbcly.files;

import com.jdbcly.core.*;
import com.jdbcly.engine.*;
import com.jdbcly.files.csv.CsvParser;
import com.jdbcly.jdbc.JdbclyColumn;
import com.jdbcly.jdbc.JdbclyTable;

/**
 * Date: 6/27/2020
 */
public class FileContext extends Context {

    private FileTablesDefinition tablesDefinition = new FileTablesDefinition(this);
    private FileColumnsDefinition columnsDefinition = new FileColumnsDefinition(this);

    public FileContext(Connection connection) {
        super(connection);
    }

    @Override
    public JdbclyTable[] getTables(String schema, String catalog) {
        return tablesDefinition.getTables(schema, catalog);
    }

    @Override
    public JdbclyColumn[] getColumns(JdbclyTable table) {
        return columnsDefinition.getColumns(table);
    }

    @Override
    public IParser getParser() {
        String scheme = connection.getScheme();
        if ("csv".equals(scheme)) {
            return new CsvParser(connection.getProperty(Properties.Csv.DELIMITER));
        }
        throw new RuntimeException("Unsupported scheme: " + scheme);
    }

    @Override
    public SelectOperation getSelectOperation() {
        return new FileSelect(this);
    }
}
