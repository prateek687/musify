package com.plugsurfung.musify.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MusicBrainzResponse(String id,
                                  String name,
                                  String country,
                                  String gender,
                                  String disambiguation,
                                  List<Relation> relations,
                                  String error,
                                  @JsonProperty("release-groups") List<ReleaseGroup> releaseGroups) {
}
