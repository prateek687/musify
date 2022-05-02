package com.plugsurfung.musify.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WikiDataEntity(String id,
                             Map<String, SiteLinkDetail> sitelinks) {
}
