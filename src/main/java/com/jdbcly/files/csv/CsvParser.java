package com.jdbcly.files.csv;

import com.jdbcly.core.StringsCache;
import com.jdbcly.core.ValueWrapper;
import com.jdbcly.engine.ItemAvailabilityListener;
import com.jdbcly.engine.ResultItem;
import com.jdbcly.files.IParser;
import com.jdbcly.files.IReader;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 6/28/2020
 */
public class CsvParser implements IParser {
    private static final int LINE_LABELS = 0;

    private final CSVParser csvParser;

    public CsvParser(Character delimiter) {
        this.csvParser = new CSVParserBuilder().withSeparator(delimiter).build();
    }

    @Override
    public void parse(IReader reader, ItemAvailabilityListener listener) {
        ValueWrapper<String[]> columns = new ValueWrapper<>();

        // attempt to minimize memory usage, since we expect that most of the strings are not unique inside the dataset
        StringsCache stringsCache = new StringsCache();

        reader.read((line, lineNumber) -> {
            if (lineNumber == LINE_LABELS) {
                columns.setValue(parseLine(line));
                return true;

            } else {
                ResultItem item = new ResultItem();
                String[] values = parseLine(line);
                String[] columnNames = columns.getValue();

                for (int i = 0; i < values.length; i++) {
                    item.put(stringsCache.getOrPut(columnNames[i]), stringsCache.getOrPut(values[i].trim()));
                }

                return listener.onItemAvailable(item);
            }
        });
    }

    @Override
    public List<ResultItem<?>> parse(IReader reader, int items) {
        List<ResultItem<?>> results = new LinkedList<>();
        parse(reader, item -> {
            results.add(item);
            return results.size() < items;
        });
        return results;
    }

    private String[] parseLine(String line) {
        try {
            return csvParser.parseLine(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
