package exercise.santander.solution.resource;

import exercise.santander.solution.domain.CreateTriviaResponse;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        given(triviaService.createTriviaQuestion()).willReturn(Optional.of(new CreateTriviaResponse(1,List.of())));

        // Perform the request and assert the response
        mockMvc.perform(post("/trivia/start")
                        .param("amount", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void replyAnswerIsCorrect() throws Exception {
        given(triviaService.verifyAnswer(any(), any()))
                .willReturn(Optional.of(TriviaResponseStatus.RIGHT));

        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"answer\":\"answer\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"result\":\"right!\"}"));
    }

    @Test
    void replyAnswerIsWrong() throws Exception {
        given(triviaService.verifyAnswer(any(), any()))
                .willReturn(Optional.of(TriviaResponseStatus.WRONG));

        mockMvc.perform(put("/trivia/reply/1")
                        .content("{\"answer\":\"answer\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"result\":\"wrong!\"}"));
    }

    @Test
    void replyAnswerSaysMaximumAttemptsReached() throws Exception {
        given(triviaService.verifyAnswer(any(), any()))
                .willReturn(Optional.of(TriviaResponseStatus.MAX_ATTEMPTS_REACHED));

        mockMvc.perform(put("/trivia/reply/1")
                        .content("{\"answer\":\"wrong\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"result\":\"Max attempts reached!\"}"));
    }

    @Test
    void replyAnswerNoSuchQuestion() throws Exception {
        given(triviaService.verifyAnswer(any(), any()))
                .willReturn(Optional.of(TriviaResponseStatus.NO_SUCH_QUESTION));

        mockMvc.perform(put("/trivia/reply/1")
                        .content("{\"answer\":\"wrong\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"result\":\"No such question!\"}"));
    }
}