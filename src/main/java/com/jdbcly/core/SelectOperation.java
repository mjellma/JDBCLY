package com.jdbcly.core;

import com.jdbcly.engine.Context;
import com.jdbcly.engine.QueryEngine;

/**
 * Date: 6/30/2020
 */
public abstract class SelectOperation {

    protected Context context;

    public SelectOperation(Context context) {
        this.context = context;
    }

    public abstract void execute(QueryEngine engine);
}
