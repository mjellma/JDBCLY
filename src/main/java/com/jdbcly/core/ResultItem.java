package com.jdbcly.core;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Date: 6/27/2020
 * <p>
 * ResultItem<V> serves as a case-insensitive map, with keys of type String and values of type V.
 * The core behavior is delegated to an underlying TreeMap.
 */
public class ResultItem<V> {

    private final Map<String, V> delegate = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public V get(String key) {
        return delegate.get(key);
    }

    public V getOrDefault(String key, V def) {
        V val = delegate.get(key);
        if (val == null) {
            val = def;
        }
        return val;
    }

    public V put(String key, V value) {
        return delegate.put(key, value);
    }

    public void remove(String key) {
        delegate.remove(key);
    }

    public void clear() {
        delegate.clear();
    }

    public int size() {
        return delegate.size();
    }

    public Set<Map.Entry<String, V>> entrySet() {
        return delegate.entrySet();
    }

    public Set<String> keySet() {
        return delegate.keySet();
    }

    public String[] keysArray() {
        return keySet().toArray(new String[0]);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
