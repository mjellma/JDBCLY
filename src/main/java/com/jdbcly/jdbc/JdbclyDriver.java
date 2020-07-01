package com.jdbcly.jdbc;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Date: 6/27/2020
 */
public class JdbclyDriver implements Driver {
    public static final String DRIVER_PREFIX = "jdbcly-";

    static {
        try {
            JdbclyDriver driverInst = new JdbclyDriver();
            DriverManager.registerDriver(driverInst);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return new JdbclyConnection(url, info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if (url.startsWith(DRIVER_PREFIX + "csv")) {
            return true;
        }
        return false;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 1;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
