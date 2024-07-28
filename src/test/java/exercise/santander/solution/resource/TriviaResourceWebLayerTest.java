package exercise.santander.solution.resource;

import exercise.santander.solution.domain.CreateQuestionResponse;
import exercise.santander.solution.domain.TriviaResponseStatus;
import exercise.santander.solution.repository.TriviaRepository;
import exercise.santander.solution.service.TriviaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TriviaResource.class)
class TriviaResourceWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TriviaService triviaService;

    @MockBean
    private TriviaRepository triviaRepository;

    @Test
    void createTriviaQuestion() throws Exception {
        // Mock the service call
        given(triviaService.createTriviaQuestion()).willReturn(Optional.of(List.of(new CreateQuestionResponse(1,List.of()))));

        // Perform the request and assert the response
        mockMvc.perform(post("/trivia/start")
                        .param("amount", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void replyAnswer() throws Exception {
        // Mock the service call for validation
        given(triviaService.verifyAnswer(any(), eq("answer")))
                .willReturn(TriviaResponseStatus.RIGHT);


        String jsonPayload = "{\"answer\":\"answer\"}";


        // Perform the request and assert the response
        mockMvc.perform(put("/trivia/reply/1")
                        .content(jsonPayload)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("right!"));
    }

    @Test
    void replyAnswerSaysWrong() throws Exception {
        // Mock the service call for validation
        given(triviaService.verifyAnswer(any(), eq("wrong")))
                .willReturn(TriviaResponseStatus.WRONG);


        String jsonPayload = "{\"answer\":\"wrong\"}";


        // Perform the request and assert the response
        mockMvc.perform(put("/trivia/reply/1")
                        .content(jsonPayload)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("wrong!"));
    }
}