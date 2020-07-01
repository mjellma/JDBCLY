package com.jdbcly.core;

/**
 * Date: 6/29/2020
 * <p>
 * Property represents a connection property object.
 * A property has a key, a default version, and a converting function (String) -> (ValueType).
 */
public class Property<T> {
    private String key;
    private String defValue;

    private PropertyConversionFunction<T> converter;

    public Property(String key, String defValue) {
        this.key = key;
        this.defValue = defValue;
    }

    public Property(String key, String defValue, PropertyConversionFunction<T> converter) {
        this.key = key;
        this.defValue = defValue;
        this.converter = converter;
    }

    public String getKey() {
        return key;
    }

    public String getDefValue() {
        return defValue;
    }

    public T convert(String value) {
        if (converter != null) {
            return converter.convert(value);
        }
        return (T) value;
    }
}

interface PropertyConversionFunction<T> {
    /**
     * @return {@code value} converted to type T
     */
    T convert(String value);
}
