package com.jdbcly.exceptions;

/**
 * Date: 7/11/2020
 */
public class JdbclyException extends RuntimeException {
    public JdbclyException() {
    }

    public JdbclyException(String message) {
        super(message);
    }

    public JdbclyException(Throwable cause) {
        super(cause);
    }
}
