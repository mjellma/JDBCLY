package com.jdbcly.exceptions;

/**
 * Date: 6/27/2020
 */
public class NotSupportedException extends JdbclyException {

    public NotSupportedException() {
        super("Operation not supported.");
    }

    public NotSupportedException(String message) {
        super(message);
    }
}
