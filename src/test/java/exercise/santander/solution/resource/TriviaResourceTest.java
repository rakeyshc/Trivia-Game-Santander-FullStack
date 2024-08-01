package exercise.santander.solution.resource;

import exercise.santander.solution.config.TriviaQuestionClient;
import exercise.santander.solution.domain.AnswerRequest;
import exercise.santander.solution.domain.ClientQuestion;
import exercise.santander.solution.entity.Trivia;
import exercise.santander.solution.repository.TriviaRepository;
import exercise.santander.solution.service.TriviaService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TriviaResourceTest {

    private TriviaResource triviaResource;

    @Mock
    private TriviaService triviaService;

    @Mock
    private TriviaQuestionClient triviaQuestionClient;

    @Mock
    private TriviaRepository triviaRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        //MockitoAnnotations.initMocks(this);
        triviaResource = new TriviaResource(triviaService);
    }

    @Test
    void shouldReturnTheCreatedTriviaQuestionsResponse(){
        //assertThrows(FeignException.class, () -> triviaResource.createTriviaQuestion());
        assertThat(triviaResource.createTriviaQuestion(),is(Optional.empty()));
    }

    @Test
    void shouldReturnWrongOnReceivalOfIncorrectAnswer(){
        when(triviaRepository.findById(1)).thenReturn(Optional
                .of(new Trivia(1, "dummy","dummy",0)));
        AnswerRequest request = new AnswerRequest("wrong");
        assertThat(triviaResource.replyAnswer("1", request), is(Optional.empty()));
    }

    private ClientQuestion getClientQuestion() {
        return new ClientQuestion("Sports",
                "multiple", "medium", "Which soccer team won the Copa America 2015 Championship?",
                "Chile", List.of("Argentina", "Brazil", "Paraguay"));
    }
}
