package com.plugsurfung.musify.api.wikidata.service;

import com.plugsurfung.musify.api.wikidata.client.WikiDataClient;
import com.plugsurfung.musify.api.wikipedia.service.WikipediaService;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Service
public class WikiDataService {

    private static final Logger log = LoggerFactory.getLogger(WikiDataService.class);
    private static final String ENWIKI = "enwiki";
    private final WikipediaService wikipediaService;
    private final WikiDataClient wikiDataClient;

    public WikiDataService(WikipediaService wikipediaService, WikiDataClient wikiDataClient) {
        this.wikiDataClient = wikiDataClient;
        this.wikipediaService = wikipediaService;
    }

    public Publisher<String> fetchWikipageTitleFromWikidata(String resourceId) {
        log.info("Fetching wikipedia title from wikidata using resource: {}", resourceId);
        var wikiDataMono = wikiDataClient.getWikiData(resourceId)
                .map(response -> extractWikiPageTitle(response.entities().get(resourceId).sitelinks().get(ENWIKI).url()));

        return wikiDataMono
                .flatMap(title -> StringUtils.hasText(title) ? Mono.from(wikipediaService.fetchWikipediaExtract(title)) : Mono.just(title));
    }

    private String extractWikiPageTitle(String url) {
        String[] tokens = url.split("/");// splitting the wikipageurl to extract the title
        if (tokens.length != 0) {
            return tokens[tokens.length - 1];
        }
        return "";
    }
}
