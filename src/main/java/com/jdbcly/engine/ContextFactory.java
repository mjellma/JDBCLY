package com.jdbcly.engine;

import com.jdbcly.files.FileContext;

/**
 * Date: 6/27/2020
 */
public class ContextFactory {

    public static Context getContext(Connection connection) {
        return new FileContext(connection);
    }
}
