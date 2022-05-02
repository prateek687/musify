package com.plugsurfung.musify;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.eclipse.jetty.http.HttpHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {MusifyApplication.class})
@AutoConfigureWebTestClient(timeout = "500000")
@ActiveProfiles("test")
public class TestBase {
  public static final String JSON = MediaType.APPLICATION_JSON.toString();
  public static final String CONTENT_TYPE = HttpHeader.CONTENT_TYPE.asString();

  public static final WireMockServer MOCK_SERVER = new WireMockServer(wireMockConfig().port(8080));

  @Autowired public WebTestClient webTestClient;
}
