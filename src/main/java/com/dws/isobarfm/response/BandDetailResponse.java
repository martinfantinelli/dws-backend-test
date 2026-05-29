package com.dws.isobarfm.response;

import com.dws.isobarfm.model.Band;

import java.util.List;

public record BandDetailResponse(
    String id,
    String name,
    String image,
    String genre,
    String biography,
    long numPlays,
    List<AlbumSummaryResponse> albums
) {

    public static BandDetailResponse from(Band band, List<AlbumSummaryResponse> albums) {
        return new BandDetailResponse(
            band.id(), band.name(), band.image(), band.genre(),
            band.biography(), band.numPlays(), albums
        );
    }
}
