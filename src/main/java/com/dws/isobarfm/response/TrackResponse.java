package com.dws.isobarfm.response;

import com.dws.isobarfm.model.Track;

public record TrackResponse(String id, String name, String duration) {

    public static TrackResponse from(Track track) {
        return new TrackResponse(track.id(), track.name(), track.duration());
    }
}
