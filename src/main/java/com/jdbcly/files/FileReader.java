package com.jdbcly.files;

import com.jdbcly.exceptions.JdbclyException;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Date: 6/27/2020
 */
public class FileReader implements IReader {

    private String uri;

    public FileReader(String uri) {
        this.uri = uri;
    }

    @Override
    public void read(Listener listener) {
        try {
            Path path = Paths.get(uri);

            BufferedReader reader = Files.newBufferedReader(path);
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                if (!listener.onLineRead(line, index++)) {
                    break;
                }
            }
        } catch (Exception e) {
            throw new JdbclyException(e);
        }
    }
}
