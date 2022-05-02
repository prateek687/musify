package com.plugsurfung.musify.api.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plugsurfung.musify.api.model.MusicBrainzResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;

@Configuration
public class ExceptionHandler extends AbstractErrorWebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public ExceptionHandler(ErrorAttributes errorAttributes, ApplicationContext applicationContext,
                            ServerCodecConfigurer configurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(configurer.getWriters());
        super.setMessageReaders(configurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(
            RequestPredicates.all() , request -> {
            var error = errorAttributes.getError(request);
            log.error("Error Message: {}", error.getMessage());

            if (error instanceof WebClientResponseException.NotFound) {
                return ServerResponse.status(NOT_FOUND).body(Mono.just(new ErrorResponse(NOT_FOUND.toString())), ErrorResponse.class);
            }

            if (error instanceof WebClientResponseException.BadRequest badRequest) {
                try {
                    MusicBrainzResponse response = OBJECT_MAPPER.readValue(badRequest.getResponseBodyAsString(), MusicBrainzResponse.class);
                    return badRequest().body(Mono.just(new ErrorResponse(response.error())), ErrorResponse.class);
                } catch (JsonProcessingException e) {
                    log.error("Unable to parse error response!");
                }
            }

            if (error instanceof WebClientResponseException.ServiceUnavailable || error instanceof RequestNotPermitted ||
                error instanceof TimeoutException || error instanceof CallNotPermittedException) {
                return ServerResponse.status(SERVICE_UNAVAILABLE).body(Mono.just(new ErrorResponse(BAD_GATEWAY.toString())), ErrorResponse.class);
            }

            return ServerResponse.status(INTERNAL_SERVER_ERROR).body(Mono.just(new ErrorResponse(INTERNAL_SERVER_ERROR.toString())), ErrorResponse.class);
        });
    }
}
