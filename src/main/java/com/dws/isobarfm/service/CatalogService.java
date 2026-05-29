package com.dws.isobarfm.service;

import com.dws.isobarfm.model.Catalog;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Owns the cached, query-optimised {@link Catalog}.
 *
 * <p>{@code sync = true} collapses a stampede: when the entry expires and many
 * requests arrive at once, only one thread fetches from the upstream and rebuilds
 * the index; the rest block briefly and reuse the result instead of each firing
 * its own upstream call.
 */
@Service
public class CatalogService {

    private final ExternalApiService externalApiService;

    public CatalogService(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @Cacheable(value = "catalog", sync = true)
    public Catalog getCatalog() {
        return Catalog.build(
            externalApiService.fetchAllBands(),
            externalApiService.fetchAllAlbums()
        );
    }
}
