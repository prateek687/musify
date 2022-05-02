package com.plugsurfung.musify.api.wikipedia.client;

import com.plugsurfung.musify.TestBase;
import com.plugsurfung.musify.api.FileReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

class WikipediaClientTest extends TestBase {

    public static final String WIKIPEDIA_URL = "/api/rest_v1/page/summary/Michael%20Jackson";
    public static final String WIKIPEDIA_RESPONSE = FileReaderUtil.responseAsStringFromFile("/wikipediaresponse.json");
    private static final String WIKIPEDIA_TITLE = "Michael Jackson";

    @Autowired
    WikipediaClient wikipediaClient;

    @Test
    void testGetDescriptionFromWikipedia() {
        wikipediaClient.getDescriptionFromWikipedia(WIKIPEDIA_TITLE).block();
        MOCK_SERVER.verify(getRequestedFor(urlEqualTo(WIKIPEDIA_URL)));
    }

    @BeforeEach
    void setup() {
        MOCK_SERVER.stubFor(get(urlEqualTo(WIKIPEDIA_URL)).willReturn(aResponse().withHeader(CONTENT_TYPE, JSON).withBody(WIKIPEDIA_RESPONSE)));
        MOCK_SERVER.start();
    }

    @AfterAll
    static void tearDown() {
        MOCK_SERVER.stop();
    }
}
