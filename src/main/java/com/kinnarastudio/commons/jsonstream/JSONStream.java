package com.kinnarastudio.commons.jsonstream;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface JSONStream {
    static <V> Stream<JSONObjectEntry<V>> of(JSONObject jsonObject, BiFunction<JSONObject, String, V> valueExtractor) {
        Objects.requireNonNull(valueExtractor);

        return Optional.ofNullable(jsonObject)
                .map(json -> StreamSupport.stream(Spliterators.spliteratorUnknownSize((Iterator<String>) json.keys(), 0), false))
                .orElseGet(Stream::empty)
                .map(key -> {
                    V value = valueExtractor.apply(jsonObject, key);
                    if(value != null) {
                        return new JSONObjectEntry<>(key, valueExtractor.apply(jsonObject, key));
                    }
                    return null;
                })
                .filter(Objects::nonNull);
    }

    static <V> Stream<V> of(JSONArray jsonArray, BiFunction<JSONArray, Integer, V> valueExtractor) {
        Objects.requireNonNull(valueExtractor);

        int length = Optional.ofNullable(jsonArray)
                .map(JSONArray::length)
                .orElse(0);

        return IntStream.iterate(0, i -> i + 1).limit(length)
                .boxed()
                .map(integer -> valueExtractor.apply(jsonArray, integer))
                .filter(Objects::nonNull);
    }
}
