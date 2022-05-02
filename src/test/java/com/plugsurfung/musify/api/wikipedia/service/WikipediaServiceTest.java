package com.plugsurfung.musify.api.wikipedia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plugsurfung.musify.api.FileReaderUtil;
import com.plugsurfung.musify.api.model.WikipediaResponse;
import com.plugsurfung.musify.api.wikipedia.client.WikipediaClient;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WikipediaServiceTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String WIKIPEDIA_TITLE = "Michael_Jackson";
    private static final String WIKIPEDIA_RESPONSE = FileReaderUtil.responseAsStringFromFile("/wikipediaresponse.json");

    private final WikipediaClient wikipediaClient = mock(WikipediaClient.class);

    @Test
    void testFetchWikipediaExtract() throws JsonProcessingException {
        WikipediaResponse wikipediaResponse = OBJECT_MAPPER.readValue(WIKIPEDIA_RESPONSE, WikipediaResponse.class);
        when(wikipediaClient.getDescriptionFromWikipedia(WIKIPEDIA_TITLE)).thenReturn(Mono.just(wikipediaResponse));
        WikipediaService wikipediaService = new WikipediaService(wikipediaClient);

        Publisher<String> wikipediaPublisher = wikipediaService.fetchWikipediaExtract(WIKIPEDIA_TITLE);
        StepVerifier.create(wikipediaPublisher)
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }
}
