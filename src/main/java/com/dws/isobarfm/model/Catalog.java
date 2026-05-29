package com.dws.isobarfm.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Immutable, query-optimised snapshot of the entire catalogue.
 *
 * <p>Built once per cache refresh by {@link #build}. All the per-request work that
 * used to be O(n) linear scans is pre-computed here:
 * <ul>
 *   <li>bands pre-sorted for every {@link BandSort} ordering;</li>
 *   <li>O(1) lookup of a band by id;</li>
 *   <li>O(1) lookup of an album by id;</li>
 *   <li>O(1) lookup of a band's albums.</li>
 * </ul>
 *
 * <p>Because bands and albums are captured in a single snapshot, a band-detail
 * response can never mix data fetched at two different points in time.
 */
public record Catalog(
    Map<BandSort, List<Band>> bandsBySort,
    Map<String, Band> bandsById,
    List<Album> albums,
    Map<String, Album> albumsById,
    Map<String, List<Album>> albumsByBandId
) {

    public static Catalog build(List<Band> bands, List<Album> albums) {
        Map<BandSort, List<Band>> bandsBySort = new EnumMap<>(BandSort.class);
        for (BandSort sort : BandSort.values()) {
            bandsBySort.put(sort, bands.stream().sorted(sort.comparator()).toList());
        }

        Map<String, Band> bandsById = bands.stream()
            .collect(Collectors.toUnmodifiableMap(Band::id, Function.identity(), (first, dup) -> first));

        Map<String, Album> albumsById = albums.stream()
            .collect(Collectors.toUnmodifiableMap(Album::id, Function.identity(), (first, dup) -> first));

        Map<String, List<Album>> albumsByBandId = albums.stream()
            .filter(a -> a.band() != null && a.band().id() != null)
            .collect(Collectors.groupingBy(a -> a.band().id()))
            .entrySet().stream()
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, e -> List.copyOf(e.getValue())));

        return new Catalog(
            Collections.unmodifiableMap(bandsBySort),
            bandsById,
            List.copyOf(albums),
            albumsById,
            albumsByBandId
        );
    }

    public List<Band> bandsSortedBy(BandSort sort) {
        return bandsBySort.get(sort);
    }

    public Optional<Band> findBand(String id) {
        return Optional.ofNullable(bandsById.get(id));
    }

    public Optional<Album> findAlbum(String id) {
        return Optional.ofNullable(albumsById.get(id));
    }

    public List<Album> albumsForBand(String bandId) {
        return albumsByBandId.getOrDefault(bandId, List.of());
    }
}
