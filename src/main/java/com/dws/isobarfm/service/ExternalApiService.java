package com.dws.isobarfm.service;

import com.dws.isobarfm.exception.ExternalApiException;
import com.dws.isobarfm.model.Album;
import com.dws.isobarfm.model.Band;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;

/**
 * Thin I/O boundary over the upstream bands API. Holds no cache itself — caching
 * and indexing are the responsibility of {@link CatalogService}. Its single job is
 * to perform the HTTP call and translate transport failures into a domain exception.
 */
@Service
public class ExternalApiService {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiService.class);

    private final RestClient restClient;

    public ExternalApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Band> fetchAllBands() {
        log.info("Fetching bands from upstream API");
        try {
            Band[] bands = restClient.get()
                .uri("/bands")
                .retrieve()
                .body(Band[].class);
            return bands != null ? List.copyOf(Arrays.asList(bands)) : List.of();
        } catch (RestClientException e) {
            log.error("Failed to fetch bands from upstream API", e);
            throw new ExternalApiException("Unable to retrieve bands data", e);
        }
    }

    public List<Album> fetchAllAlbums() {
        log.info("Fetching albums from upstream API");
        try {
            Album[] albums = restClient.get()
                .uri("/albums")
                .retrieve()
                .body(Album[].class);
            return albums != null ? List.copyOf(Arrays.asList(albums)) : List.of();
        } catch (RestClientException e) {
            log.error("Failed to fetch albums from upstream API", e);
            throw new ExternalApiException("Unable to retrieve albums data", e);
        }
    }
}
