package com.plugsurfung.musify.api.coverart.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.plugsurfung.musify.TestBase;
import com.plugsurfung.musify.api.FileReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

class CoverArtClientTest extends TestBase {

    private static final String RELEASE_GROUP_ID = "97e0014d-a267-33a0-a868-bb4e2552918a";
    private static final String COVER_ART_IMAGE_RESPONSE =
            FileReaderUtil.responseAsStringFromFile("/coverartresponse.json");
    private static final String COVER_ART_URL =
            "/release-group/97e0014d-a267-33a0-a868-bb4e2552918a";

    @Autowired
    CoverArtClient coverArtClient;

    @BeforeEach
    void init() {
        MOCK_SERVER.stubFor(get(urlEqualTo(COVER_ART_URL)).willReturn(aResponse().withHeader(CONTENT_TYPE, JSON).withBody(COVER_ART_IMAGE_RESPONSE)));
        MOCK_SERVER.start();
    }

    @AfterAll
    static void afterAll() {
        MOCK_SERVER.stop();
    }

    @Test
    void testFetchCoverArtImage() {
        coverArtClient.fetchCoverArtImage(RELEASE_GROUP_ID).block();
        MOCK_SERVER.verify(WireMock.getRequestedFor(WireMock.urlEqualTo(COVER_ART_URL)));
    }
}
