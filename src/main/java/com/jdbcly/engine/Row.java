package com.jdbcly.engine;

import java.util.Arrays;

/**
 * Date: 7/8/2020
 */
public class Row {
    private Comparable[] values;

    public Row(Comparable[] values) {
        this.values = values;
    }

    public Comparable getValue(int sqlIndex) {
        int i = sqlIndex - 1;
        Comparable value = null;
        if (i >= 0 && i < values.length) {
            value = values[i];
        }
        return value;
    }

    void setValues(Comparable[] values) {
        this.values = values;
    }

    void setValue(Comparable value, int sqlIndex) {
        values[sqlIndex - 1] = value;
    }

    public Row copy() {
        return new Row(Arrays.copyOf(values, values.length));
    }
}
