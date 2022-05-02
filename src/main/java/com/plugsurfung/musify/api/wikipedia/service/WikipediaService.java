package com.plugsurfung.musify.api.wikipedia.service;

import com.plugsurfung.musify.api.model.WikipediaResponse;
import com.plugsurfung.musify.api.wikipedia.client.WikipediaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WikipediaService {

    private static final Logger log = LoggerFactory.getLogger(WikipediaService.class);
    private final WikipediaClient wikipediaClient;

    public WikipediaService(WikipediaClient wikipediaClient) {
        this.wikipediaClient = wikipediaClient;
    }

    public Mono<String> fetchWikipediaExtract(String title) {
        log.info("Fetching artist description from wikipedia using title: {}", title);
        return wikipediaClient.getDescriptionFromWikipedia(title).map(WikipediaResponse::extractHtml);
    }
}
