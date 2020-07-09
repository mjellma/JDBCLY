package com.jdbcly.files;

import com.jdbcly.core.ResultItem;
import com.jdbcly.engine.ItemAvailabilityListener;

import java.util.List;

/**
 * Date: 6/27/2020
 */
public interface IParser {

    /**
     * @param reader
     * @param items  the maximum number of items to parse.
     * @return a list of up to {@code items} parsed items.
     */
    List<ResultItem<?>> parse(IReader reader, int items);

    /**
     * @param reader
     * @param listener the item availability listener.
     */
    void parse(IReader reader, ItemAvailabilityListener listener);
}
