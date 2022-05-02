package com.plugsurfung.musify.api.routes;

import com.plugsurfung.musify.api.artist.service.ArtistService;
import com.plugsurfung.musify.api.model.ArtistDetailsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RoutesConfiguration {
    private static final Logger log = LoggerFactory.getLogger(RoutesConfiguration.class);
    private final ArtistService artistService;

    public RoutesConfiguration(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Bean
    public RouterFunction<ServerResponse> getArtistDetailsRoute() {
        return route(GET("/musify/music-artist/details/{mbid}"),
            this::getArtistDetailsHandler);
    }

    Mono<ServerResponse> getArtistDetailsHandler(ServerRequest request) {
        var mbid = request.pathVariable("mbid");
        log.info("Received GET request: {}", request.path());

        return artistService.getArtistDetails(mbid)
            .flatMap(artist -> ok().body(Mono.just(artist), ArtistDetailsResponse.class));
    }
}
