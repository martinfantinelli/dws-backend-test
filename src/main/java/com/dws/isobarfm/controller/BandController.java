package com.dws.isobarfm.controller;

import com.dws.isobarfm.model.BandSort;
import com.dws.isobarfm.response.BandDetailResponse;
import com.dws.isobarfm.response.BandSummaryResponse;
import com.dws.isobarfm.response.ErrorResponse;
import com.dws.isobarfm.service.BandService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bands")
@Tag(name = "Bands", description = "List, search, and retrieve band details")
public class BandController {

    private final BandService bandService;

    public BandController(BandService bandService) {
        this.bandService = bandService;
    }

    @Operation(
        summary = "List all bands",
        description = "Returns all bands, optionally filtered by name and sorted by popularity or alphabetical order."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bands retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BandSummaryResponse.class)))),
        @ApiResponse(responseCode = "400", description = "Invalid sort value",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public List<BandSummaryResponse> getBands(
            @Parameter(description = "Case-insensitive name filter. Matches any substring.")
            @RequestParam(required = false) String search,

            @Parameter(description = "Sort order. `popularity` sorts by numPlays descending; `alphabetical` sorts A–Z.")
            @RequestParam(defaultValue = "popularity") BandSort sort) {

        return bandService.getBands(search, sort);
    }

    @Operation(
        summary = "Get band by ID",
        description = "Returns full band details including biography and the list of associated albums."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Band found",
            content = @Content(schema = @Schema(implementation = BandDetailResponse.class))),
        @ApiResponse(responseCode = "404", description = "Band not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public BandDetailResponse getBandById(
            @Parameter(description = "Band UUID", example = "bc710bcf-8815-42cf-bad2-3f1d12246aeb")
            @PathVariable String id) {
        return bandService.getBandById(id);
    }
}
