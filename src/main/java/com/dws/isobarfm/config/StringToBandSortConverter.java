package com.dws.isobarfm.config;

import com.dws.isobarfm.model.BandSort;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Binds the {@code sort} query parameter to {@link BandSort}, case-insensitively.
 * An unknown value throws {@link IllegalArgumentException}, which Spring surfaces as
 * a 400 via the global handler.
 */
public class StringToBandSortConverter implements Converter<String, BandSort> {

    @Override
    public BandSort convert(String source) {
        try {
            return BandSort.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            String allowed = Arrays.stream(BandSort.values())
                .map(v -> v.name().toLowerCase())
                .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(
                "Invalid sort value '" + source + "'. Allowed values: " + allowed);
        }
    }
}
