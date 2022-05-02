package com.plugsurfung.musify.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ArtistDetailsResponse(@JsonProperty("mbid") String id,
                                    String name,
                                    String gender,
                                    String country,
                                    String disambiguation,
                                    String description,
                                    List<ArtistAlbum> albums) {
}


