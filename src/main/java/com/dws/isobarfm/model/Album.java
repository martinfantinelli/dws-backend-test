package com.dws.isobarfm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Album(
    String id,
    String name,
    String releasedDate,
    String image,
    Band band,
    List<Track> tracks
) {}
