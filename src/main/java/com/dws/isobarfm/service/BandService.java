package com.dws.isobarfm.service;

import com.dws.isobarfm.exception.ResourceNotFoundException;
import com.dws.isobarfm.model.Band;
import com.dws.isobarfm.model.BandSort;
import com.dws.isobarfm.model.Catalog;
import com.dws.isobarfm.response.AlbumSummaryResponse;
import com.dws.isobarfm.response.BandDetailResponse;
import com.dws.isobarfm.response.BandSummaryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class BandService {

    private final CatalogService catalogService;

    public BandService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public List<BandSummaryResponse> getBands(String search, BandSort sort) {
        // Already sorted in the catalog — filtering a sorted list preserves order,
        // so there is no per-request sort.
        Stream<Band> bands = catalogService.getCatalog().bandsSortedBy(sort).stream();

        if (search != null && !search.isBlank()) {
            String query = search.trim().toLowerCase();
            bands = bands.filter(b -> b.name().toLowerCase().contains(query));
        }

        return bands.map(BandSummaryResponse::from).toList();
    }

    public BandDetailResponse getBandById(String id) {
        Catalog catalog = catalogService.getCatalog();

        Band band = catalog.findBand(id)
            .orElseThrow(() -> new ResourceNotFoundException("Band", id));

        List<AlbumSummaryResponse> albums = catalog.albumsForBand(id).stream()
            .map(AlbumSummaryResponse::from)
            .toList();

        return BandDetailResponse.from(band, albums);
    }
}
