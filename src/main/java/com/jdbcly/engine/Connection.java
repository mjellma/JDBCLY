package com.jdbcly.engine;

import com.jdbcly.core.Properties;
import com.jdbcly.core.Property;
import com.jdbcly.core.Utils;
import com.jdbcly.jdbc.JdbclyConnection;
import com.jdbcly.jdbc.JdbclyDriver;

/**
 * Date: 6/27/2020
 */
public class Connection {

    private final JdbclyConnection jdbcConnection;

    private String scheme;
    private final ResultItem<String> properties = new ResultItem<>();

    public Connection(JdbclyConnection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
        parseUri(jdbcConnection.url());
    }

    private void parseUri(String uriString) {
        uriString = uriString.replace(JdbclyDriver.DRIVER_PREFIX, "");
        uriString = uriString.replace("\\", "/");

        scheme = uriString.substring(0, uriString.indexOf("://"));
        String filePath = uriString.substring(scheme.length() + "://".length(), uriString.indexOf("?"));
        String params = uriString.substring(uriString.indexOf("?"));

        if (!Utils.isNullOrEmpty(params)) {
            String[] kv;
            for (String part : params.split("&")) {
                kv = part.split("=");
                properties.put(kv[0], kv[1]);
            }
        }

        properties.put(Properties.URI.getKey(), filePath);
    }

    public String getScheme() {
        return scheme;
    }

    public <T> T getProperty(Property prop) {
        String value = properties.getOrDefault(prop.getKey(), prop.getDefValue());
        return (T) prop.convert(value);
    }

    public JdbclyConnection getJdbcConnection() {
        return jdbcConnection;
    }
}
