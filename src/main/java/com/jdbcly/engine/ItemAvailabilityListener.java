package com.jdbcly.engine;

/**
 * Date: 6/30/2020
 */
public interface ItemAvailabilityListener {
    /**
     * @param item the available item.
     * @return whether to proceed with the next item.
     */
    boolean onItemAvailable(ResultItem<?> item);
}
