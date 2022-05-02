package com.plugsurfung.musify;

import com.plugsurfung.musify.api.exceptions.ErrorResponse;
import com.plugsurfung.musify.api.model.ArtistDetailsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "500000")
public class MusifyApplicationTest {

    @Autowired
    public WebTestClient webTestClient;

    @Test
    public void contextLoads() {
    }

    @Test
    public void fetchArtistAlbums() {
        String mbid = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";

        var response = webTestClient.get()
            .uri("http://localhost:8080/musify/music-artist/details/" + mbid)
            .exchange()
            .expectStatus()
            .isOk() // should return ok
            .expectBody(ArtistDetailsResponse.class)
            .returnResult()
            .getResponseBody();

        assertNotNull(response);
        var artistAlbums = response.albums();

        assertEquals("Michael Jackson", response.name());
        artistAlbums.forEach(album -> assertNotNull(album.imageUrl()));
    }

    @Test
    public void fetchArtistAlbums_InvalidMBID() {
        String invalidMusicBrainzId = "Invalid_music_brainz_id";

        var response = webTestClient.get()
                .uri("http://localhost:8080/musify/music-artist/details/" + invalidMusicBrainzId)
                .exchange()
                .expectStatus()
                .isBadRequest() // should return ok
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(response);
        assertEquals("Invalid mbid.", response.errorMessage());
    }

    @Test
    public void fetchArtistAlbums_NonExistentMbid() {
        String nonExistentMBID = "8b8a38a9-a290-4560-84f6-3d4466e8d792";

        var response = webTestClient.get()
                .uri("http://localhost:8080/musify/music-artist/details/" + nonExistentMBID)
                .exchange()
                .expectStatus()
                .isNotFound() // should return ok
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(response);
        assertEquals("404 NOT_FOUND", response.errorMessage());
    }

    @Test
    public void fetchArtistAlbums_WhenCoverArtNotFound() {
        String mbid = "8b8a38a9-a290-4560-84f6-3d4466e8d791";

        var response = webTestClient.get()
                .uri("http://localhost:8080/musify/music-artist/details/" + mbid)
                .exchange()
                .expectStatus()
                .isOk() // should return ok
                .expectBody(ArtistDetailsResponse.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(response);
        assertNotNull(response.name());
        assertEquals("John Williams", response.name());

        var albums = response.albums();

        assertTrue(albums.stream().anyMatch(album -> album.imageUrl() == null));
    }
}
