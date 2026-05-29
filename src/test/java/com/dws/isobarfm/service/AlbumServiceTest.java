package com.dws.isobarfm.service;

import com.dws.isobarfm.exception.ResourceNotFoundException;
import com.dws.isobarfm.model.Album;
import com.dws.isobarfm.model.Band;
import com.dws.isobarfm.model.Catalog;
import com.dws.isobarfm.model.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlbumServiceTest {

    private AlbumService albumService;

    @BeforeEach
    void setUp() {
        Band band = new Band("b", "Nickelback", "img", "rock", "bio", 100, List.of());
        Album album = new Album("a1", "Silver Side Up", "1988-10-07", "img", band,
            List.of(new Track("t1", "Never Again", "260")));

        Catalog catalog = Catalog.build(List.of(band), List.of(album));
        albumService = new AlbumService(stubCatalog(catalog));
    }

    @Test
    void getAlbumByIdReturnsDetailWithTracks() {
        var detail = albumService.getAlbumById("a1");

        assertThat(detail.name()).isEqualTo("Silver Side Up");
        assertThat(detail.band().name()).isEqualTo("Nickelback");
        assertThat(detail.tracks()).extracting("name").containsExactly("Never Again");
    }

    @Test
    void getAlbumByIdThrowsWhenMissing() {
        assertThatThrownBy(() -> albumService.getAlbumById("missing"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Album not found");
    }

    @Test
    void getAllAlbumsReturnsEveryAlbum() {
        assertThat(albumService.getAllAlbums()).hasSize(1);
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
