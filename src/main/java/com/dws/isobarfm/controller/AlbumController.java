package com.dws.isobarfm.controller;

import com.dws.isobarfm.response.AlbumDetailResponse;
import com.dws.isobarfm.response.ErrorResponse;
import com.dws.isobarfm.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@Tag(name = "Albums", description = "Retrieve albums and their tracklists")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Operation(
        summary = "List all albums",
        description = "Returns every album in the catalogue with full tracklist and embedded band summary."
    )
    @ApiResponse(responseCode = "200", description = "Albums retrieved successfully",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = AlbumDetailResponse.class))))
    @GetMapping
    public List<AlbumDetailResponse> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    @Operation(
        summary = "Get album by ID",
        description = "Returns a single album including its tracks and the band it belongs to."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Album found",
            content = @Content(schema = @Schema(implementation = AlbumDetailResponse.class))),
        @ApiResponse(responseCode = "404", description = "Album not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public AlbumDetailResponse getAlbumById(
            @Parameter(description = "Album UUID", example = "3c5794a0-d913-390d-ab24-6762af38c112")
            @PathVariable String id) {
        return albumService.getAlbumById(id);
    }
}
