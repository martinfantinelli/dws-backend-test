package com.dws.isobarfm.service;

import com.dws.isobarfm.exception.ResourceNotFoundException;
import com.dws.isobarfm.model.Album;
import com.dws.isobarfm.model.Band;
import com.dws.isobarfm.model.BandSort;
import com.dws.isobarfm.model.Catalog;
import com.dws.isobarfm.response.BandSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BandServiceTest {

    private BandService bandService;

    @BeforeEach
    void setUp() {
        Band radiohead = new Band("r", "Radiohead", "img", "alt", "bio", 553_212, List.of());
        Band beatles = new Band("b", "The Beatles", "img", "rock", "bio", 475_270, List.of());
        Album beatlesAlbum = new Album("a1", "Abbey Road", "1969-09-26", "img", beatles, List.of());

        Catalog catalog = Catalog.build(List.of(radiohead, beatles), List.of(beatlesAlbum));
        bandService = new BandService(stubCatalog(catalog));
    }

    @Test
    void returnsBandsSortedByPopularity() {
        List<BandSummaryResponse> result = bandService.getBands(null, BandSort.POPULARITY);

        assertThat(result).extracting(BandSummaryResponse::name)
            .containsExactly("Radiohead", "The Beatles");
    }

    @Test
    void filtersBySearchCaseInsensitively() {
        List<BandSummaryResponse> result = bandService.getBands("beat", BandSort.POPULARITY);

        assertThat(result).extracting(BandSummaryResponse::name)
            .containsExactly("The Beatles");
    }

    @Test
    void getBandByIdReturnsDetailWithAlbums() {
        var detail = bandService.getBandById("b");

        assertThat(detail.name()).isEqualTo("The Beatles");
        assertThat(detail.albums()).extracting("name").containsExactly("Abbey Road");
    }

    @Test
    void getBandByIdThrowsWhenMissing() {
        assertThatThrownBy(() -> bandService.getBandById("missing"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Band not found");
    }

    private CatalogService stubCatalog(Catalog catalog) {
        return new CatalogService(null) {
            @Override
            public Catalog getCatalog() {
                return catalog;
            }
        };
    }
}
