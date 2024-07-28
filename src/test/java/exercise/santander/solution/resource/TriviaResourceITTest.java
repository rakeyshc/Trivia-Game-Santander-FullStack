package exercise.santander.solution.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.santander.solution.domain.CreateTriviaResponse;
import exercise.santander.solution.domain.TriviaResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.net.URL;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TriviaResourceITTest {
    @LocalServerPort
    private int port;
    private URL base;
    @Autowired
    private TestRestTemplate template;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/trivia");
    }

    @Test
    public void createQuestionShouldWork(){

        ResponseEntity<CreateTriviaResponse> response = template.postForEntity(base.toString()+"/start", null, CreateTriviaResponse.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().triviaId()).isEqualTo(1);

    }

    @Test
    @Sql(statements = "INSERT INTO TRIVIA(trivia_id,answer_attempts,correct_answer,question) VALUES(1,1,'answer','dummy question')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void replyAnswerShouldReturnCorrect() throws Exception {

        //Arrange
        JSONObject requestObject = new JSONObject();
        requestObject.put("answer","answer");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(requestObject.toString(), headers);

        //Act
        ResponseEntity<TriviaResponse> response = template.exchange(base.toString()+"/reply/1", HttpMethod.PUT, entity, TriviaResponse.class);

        //Assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().result()).isEqualTo("right!");
    }

    @Test
    @Sql(statements = "INSERT INTO TRIVIA(trivia_id,answer_attempts,correct_answer,question) " +
            "VALUES(2,1,'answer','dummy question')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM TRIVIA WHERE trivia_id = 1", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void replyAnswerShouldReturnWrong() throws Exception {

        //Arrange
        JSONObject requestObject = new JSONObject();
        requestObject.put("answer","wrong");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(requestObject.toString(), headers);

        //Act
        ResponseEntity<TriviaResponse> response = template.exchange(base.toString()+"/reply/2", HttpMethod.PUT, entity, TriviaResponse.class);

        //Assert
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(response.getBody().result()).isEqualTo("wrong!");
    }
}
