package com.dws.isobarfm.model;

import java.util.Comparator;

/**
 * Supported orderings for the band listing. Each constant owns its comparator,
 * so the sort logic lives in exactly one place and the valid values are exposed
 * to the API contract (Swagger renders this enum as a dropdown).
 */
public enum BandSort {

    POPULARITY(Comparator.comparingLong(Band::numPlays).reversed()),
    ALPHABETICAL(Comparator.comparing(Band::name, String.CASE_INSENSITIVE_ORDER));

    private final Comparator<Band> comparator;

    BandSort(Comparator<Band> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Band> comparator() {
        return comparator;
    }
}
