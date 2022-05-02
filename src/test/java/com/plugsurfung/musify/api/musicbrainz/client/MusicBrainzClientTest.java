package com.plugsurfung.musify.api.musicbrainz.client;

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

class MusicBrainzClientTest extends TestBase {
    private static final String MBID = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";
    public static final String MUSIC_BRAINZ_URL =
            "/ws/2/artist/f27ec8db-af05-4f36-916e-3d57f91ecf5e?fmt=json&inc=url-rels+release-groups";
    public static final String MUICS_BRAINZ_RESPONSE =
            FileReaderUtil.responseAsStringFromFile("/musicbrainzresponse.json");

    @Autowired
    MusicBrainzClient musicBrainzClient;

    @Test
    void testFetchMusifyArtist() {
        musicBrainzClient.fetchMusifyArtist(MBID).block();
        MOCK_SERVER.verify(WireMock.getRequestedFor(WireMock.urlEqualTo(MUSIC_BRAINZ_URL)));
    }

    @BeforeEach
    void init() {
        MOCK_SERVER.stubFor(get(urlEqualTo(MUSIC_BRAINZ_URL)).willReturn(aResponse().withHeader(CONTENT_TYPE, JSON).withBody(MUICS_BRAINZ_RESPONSE)));
        MOCK_SERVER.start();
    }

    @AfterAll
    static void afterAll() {
        MOCK_SERVER.stop();
    }
}
