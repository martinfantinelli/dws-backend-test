package com.dws.isobarfm.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogTest {

    private Band band(String id, String name, long plays) {
        return new Band(id, name, "img", "rock", "bio", plays, List.of());
    }

    private Album album(String id, Band band) {
        return new Album(id, "album-" + id, "2000-01-01", "img", band, List.of());
    }

    @Test
    void preSortsBandsByPopularityDescending() {
        Band a = band("1", "Alpha", 100);
        Band b = band("2", "Bravo", 300);
        Band c = band("3", "Charlie", 200);

        Catalog catalog = Catalog.build(List.of(a, b, c), List.of());

        assertThat(catalog.bandsSortedBy(BandSort.POPULARITY))
            .extracting(Band::name)
            .containsExactly("Bravo", "Charlie", "Alpha");
    }

    @Test
    void preSortsBandsAlphabeticallyCaseInsensitive() {
        Band a = band("1", "zebra", 1);
        Band b = band("2", "Apple", 2);

        Catalog catalog = Catalog.build(List.of(a, b), List.of());

        assertThat(catalog.bandsSortedBy(BandSort.ALPHABETICAL))
            .extracting(Band::name)
            .containsExactly("Apple", "zebra");
    }

    @Test
    void indexesAlbumsByBandIdForConstantTimeLookup() {
        Band nickelback = band("b1", "Nickelback", 100);
        Band u2 = band("b2", "U2", 200);
        Album silverSideUp = album("a1", nickelback);
        Album theLongRoad = album("a2", nickelback);
        Album joshuaTree = album("a3", u2);

        Catalog catalog = Catalog.build(
            List.of(nickelback, u2),
            List.of(silverSideUp, theLongRoad, joshuaTree)
        );

        assertThat(catalog.albumsForBand("b1")).extracting(Album::id)
            .containsExactlyInAnyOrder("a1", "a2");
        assertThat(catalog.albumsForBand("b2")).extracting(Album::id)
            .containsExactly("a3");
        assertThat(catalog.albumsForBand("unknown")).isEmpty();
    }

    @Test
    void findsBandAndAlbumById() {
        Band b = band("b1", "Nickelback", 100);
        Album a = album("a1", b);

        Catalog catalog = Catalog.build(List.of(b), List.of(a));

        assertThat(catalog.findBand("b1")).isPresent();
        assertThat(catalog.findBand("nope")).isEmpty();
        assertThat(catalog.findAlbum("a1")).isPresent();
        assertThat(catalog.findAlbum("nope")).isEmpty();
    }

    @Test
    void returnedCollectionsAreImmutable() {
        Catalog catalog = Catalog.build(List.of(band("1", "A", 1)), List.of());

        assertThat(catalog.bandsSortedBy(BandSort.POPULARITY)).isUnmodifiable();
    }
}
