package com.jdbcly.files;

import com.jdbcly.core.*;
import com.jdbcly.engine.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 6/27/2020
 */
public class FileSelect extends SelectOperation implements ItemAvailabilityListener {

    private QueryEngine engine;

    private List<ResultItem<?>> results = new LinkedList<>();

    public FileSelect(Context context) {
        super(context);
    }

    @Override
    public void execute(QueryEngine engine) {
        this.engine = engine;

        String uri = context.getConnection().getProperty(Properties.URI);
        context.getParser().parse(new FileReader(uri), this);
    }

    @Override
    public boolean onItemAvailable(ResultItem<?> item) {
        engine.processItem(item);
        return true;
    }
}
