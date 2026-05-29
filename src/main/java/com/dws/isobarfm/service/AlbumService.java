package com.dws.isobarfm.service;

import com.dws.isobarfm.exception.ResourceNotFoundException;
import com.dws.isobarfm.response.AlbumDetailResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    private final CatalogService catalogService;

    public AlbumService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public List<AlbumDetailResponse> getAllAlbums() {
        return catalogService.getCatalog().albums().stream()
            .map(AlbumDetailResponse::from)
            .toList();
    }

    public AlbumDetailResponse getAlbumById(String id) {
        return catalogService.getCatalog().findAlbum(id)
            .map(AlbumDetailResponse::from)
            .orElseThrow(() -> new ResourceNotFoundException("Album", id));
    }
}
