//package com.company.files;
//
//import com.company.engine.ResultItem;
//import com.company.files.IParser;
//import com.squareup.moshi.JsonAdapter;
//import com.squareup.moshi.Moshi;
//import com.squareup.moshi.Types;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * Date: 6/27/2020
// */
//
//public class JsonParser implements IParser {
//
//    private static final String SEPARATOR = "_";
//
//    @Override
//    public List<ResultItem<?>> parseRows(String content) {
//        try {
//            Moshi moshi = new Moshi.Builder().build();
//            JsonAdapter<List<Map<String, Object>>> jsonAdapter = moshi.adapter(Types.newParameterizedType(List.class, Map.class));
//            List<Map<String, Object>> list = jsonAdapter.fromJson(content);
//
//            ArrayList<ResultItem<?>> results = new ArrayList<>(list.size());
//            for (Map<String, Object> map : list) {
//                results.add(convert(map));
//            }
//
//            return results;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public String[] parseColumns(String content) {
//        return parseRows(content).get(0).keysArray();
//    }
//
//    private ResultItem<Comparable> convert(Map<String, Object> map) {
//        ResultItem<Comparable> item = new ResultItem<>();
//
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            if (entry.getValue() instanceof Map) {
//                traverseMapValues(item, entry.getKey(), (Map<String, Object>) entry.getValue());
//            } else {
//                item.put(entry.getKey(), (Comparable) entry.getValue());
//            }
//        }
//        return item;
//    }
//
//    /**
//     * Flattens the hierarchy.
//     * Nested object keys are calculated as: {@code parentKey} + {@code SEPARATOR} + {@code entry.getKey()}
//     */
//    private void traverseMapValues(ResultItem<Comparable> item, String parentKey, Map<String, Object> map) {
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            if (entry.getValue() instanceof Map) {
//                traverseMapValues(item, parentKey + SEPARATOR + entry.getKey(), (Map<String, Object>) entry.getValue());
//            } else {
//                item.put(parentKey + SEPARATOR + entry.getKey(), (Comparable) entry.getValue());
//            }
//        }
//    }
//}
