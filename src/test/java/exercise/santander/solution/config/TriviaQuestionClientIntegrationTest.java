package exercise.santander.solution.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import exercise.santander.solution.domain.TriviaQuestion;
import exercise.santander.solution.domain.TriviaQuestionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@SpringBootTest
@WireMockTest(httpPort = 8089)
public class TriviaQuestionClientIntegrationTest {

    @Autowired
    TriviaQuestionClient triviaQuestionClient;

    @Value("${trivia.question.url}")
    private String ipApiUrl;

    @Test
    public void shouldCallWeatherService() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        WireMock.stubFor(WireMock.post(WireMock.urlPathMatching("/trivia/start/1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(TriviaQuestionResponse.class))
                        .withStatus(200)));


        var triviaQuestionResponse = triviaQuestionClient.getTriviaQuestion(1);
        assertNotNull(triviaQuestionResponse);
    }
}
