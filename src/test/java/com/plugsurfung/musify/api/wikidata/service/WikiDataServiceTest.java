package com.plugsurfung.musify.api.wikidata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plugsurfung.musify.api.FileReaderUtil;
import com.plugsurfung.musify.api.model.WikiDataResponse;
import com.plugsurfung.musify.api.wikidata.client.WikiDataClient;
import com.plugsurfung.musify.api.wikipedia.service.WikipediaService;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WikiDataServiceTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String WIKIDATA_RESPONSE = FileReaderUtil.responseAsStringFromFile("/wikidataresponse.json");
    private static final String WIKIPEDIA_EXTRACT = """
            <p><b>Michael Joseph Jackson</b> was an American singer, songwriter, and dancer. Dubbed the "King of Pop", he is regarded as one of the most significant cultural figures of the 20th century. Over a four-decade career, his contributions to music, dance, and fashion, along with his publicized personal life, made him a global figure in popular culture. Jackson influenced artists across many music genres; through stage and video performances, he popularized complicated dance moves such as the moonwalk, to which he gave the name, as well as the robot. He is the most awarded individual music artist in history.</p>
            """;
    private static final String RESOURCE_ID = "Q2831";

    private final WikiDataClient wikiDataClient = mock(WikiDataClient.class);
    private final WikipediaService wikipediaService = mock(WikipediaService.class);

    @Test
    void testFetchWikipageTitleFromWikidata() throws JsonProcessingException {
        WikiDataResponse wikiDataResponse = OBJECT_MAPPER.readValue(WIKIDATA_RESPONSE, WikiDataResponse.class);

        when(wikiDataClient.getWikiData(RESOURCE_ID)).thenReturn(Mono.just(wikiDataResponse));
        WikiDataService wikiDataService = new WikiDataService(wikipediaService, wikiDataClient);
        Publisher<String> wikiDataPublisher = wikiDataService.fetchWikipageTitleFromWikidata("Q2831");
        when(wikipediaService.fetchWikipediaExtract("Michael_Jackson")).thenReturn(Mono.just(WIKIPEDIA_EXTRACT));
        StepVerifier.create(wikiDataPublisher)
                .expectNextMatches(response -> response != null && response.equals(WIKIPEDIA_EXTRACT))
                .verifyComplete();
    }
}
