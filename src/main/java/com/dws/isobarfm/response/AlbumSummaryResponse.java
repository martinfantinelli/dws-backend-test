package com.dws.isobarfm.response;

import com.dws.isobarfm.model.Album;

public record AlbumSummaryResponse(String id, String name, String image, String releasedDate) {

    public static AlbumSummaryResponse from(Album album) {
        return new AlbumSummaryResponse(album.id(), album.name(), album.image(), album.releasedDate());
    }
}
