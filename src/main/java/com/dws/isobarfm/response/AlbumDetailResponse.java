package com.dws.isobarfm.response;

import com.dws.isobarfm.model.Album;

import java.util.List;

public record AlbumDetailResponse(
    String id,
    String name,
    String image,
    String releasedDate,
    BandSummaryResponse band,
    List<TrackResponse> tracks
) {

    public static AlbumDetailResponse from(Album album) {
        List<TrackResponse> tracks = album.tracks() == null
            ? List.of()
            : album.tracks().stream().map(TrackResponse::from).toList();

        return new AlbumDetailResponse(
            album.id(), album.name(), album.image(), album.releasedDate(),
            BandSummaryResponse.from(album.band()),
            tracks
        );
    }
}
