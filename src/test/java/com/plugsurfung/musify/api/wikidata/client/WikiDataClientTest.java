package com.plugsurfung.musify.api.wikidata.client;

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

class WikiDataClientTest extends TestBase {

    private static final String WIKIDATA_ENTITY_ID = "Q2831";
    public static final String WIKIDATA_RESPONSE = FileReaderUtil.responseAsStringFromFile("/wikidataresponse.json");
    public static final String WIKIDATA_URL = "/wiki/Special:EntityData/Q2831.json";

    @Autowired
    private WikiDataClient wikiDataClient;

    @BeforeEach
    void setUp() {
        MOCK_SERVER.stubFor(get(urlEqualTo(WIKIDATA_URL))
                .willReturn(aResponse().withHeader(CONTENT_TYPE, JSON)
                        .withBody(WIKIDATA_RESPONSE)));
        MOCK_SERVER.start();
    }

    @AfterAll
    static void tearDown() {
        MOCK_SERVER.stop();
    }

    @Test
    void testGetWikiData() {
        wikiDataClient.getWikiData(WIKIDATA_ENTITY_ID).block();
        MOCK_SERVER.verify(getRequestedFor(urlEqualTo(WIKIDATA_URL)));
    }
}
