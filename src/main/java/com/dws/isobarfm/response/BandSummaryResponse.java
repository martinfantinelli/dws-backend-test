package com.dws.isobarfm.response;

import com.dws.isobarfm.model.Band;

public record BandSummaryResponse(String id, String name, String image, String genre, long numPlays) {

    public static BandSummaryResponse from(Band band) {
        return new BandSummaryResponse(band.id(), band.name(), band.image(), band.genre(), band.numPlays());
    }
}
