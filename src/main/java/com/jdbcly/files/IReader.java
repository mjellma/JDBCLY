package com.jdbcly.files;

/**
 * Date: 6/30/2020
 */
public interface IReader {
    void read(Listener listener);

    interface Listener {
        /**
         * @return whether to continue reading.
         */
        boolean onLineRead(String line, int lineNumber);
    }
}
