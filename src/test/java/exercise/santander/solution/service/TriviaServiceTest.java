package exercise.santander.solution.service;

import exercise.santander.solution.config.TriviaQuestionClient;
import exercise.santander.solution.domain.CreateTriviaResponse;
import exercise.santander.solution.domain.ClientQuestion;
import exercise.santander.solution.domain.ClientQuestionResponse;
import exercise.santander.solution.domain.TriviaResponseStatus;
import exercise.santander.solution.entity.Trivia;
import exercise.santander.solution.repository.TriviaRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TriviaServiceTest {

    @Mock
    private TriviaRepository triviaRepository;
    @Mock
    private TriviaQuestionClient triviaQuestionClient;

    private TriviaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TriviaService(triviaRepository,triviaQuestionClient);
    }

    @Test
    void createTriviaQuestion() {
        //Arrange
        ClientQuestion triviaQuestion = new ClientQuestion("Sports",
                "multiple", "medium", "Which soccer team won the Copa America 2015 Championship?",
                "Chile", List.of("Argentina", "Brazil", "Paraguay"));
        ClientQuestionResponse triviaQuestionResponse = new ClientQuestionResponse(0, List.of(triviaQuestion));
        when(triviaQuestionClient.getTriviaQuestion(1)).thenReturn(triviaQuestionResponse);
        when(triviaRepository.save(any(Trivia.class))).thenReturn(createTrivia());

        // Act
        Optional<CreateTriviaResponse> responseEntity = service.createTriviaQuestion();

        // Assert
        assertEquals(0, responseEntity.get().triviaId());
        verify(triviaRepository, times(1)).save(any(Trivia.class));

    }

    @Test
    void createTriviaQuestion_Failure() {
        // Arrange
        ClientQuestionResponse triviaQuestionResponse = new ClientQuestionResponse(1, List.of());
        when(triviaQuestionClient.getTriviaQuestion(1)).thenReturn(triviaQuestionResponse);

        // Act
        Optional<CreateTriviaResponse> response = service.createTriviaQuestion();

        // Assert
        assertFalse(response.isPresent());
        verify(triviaRepository, never()).save(any(Trivia.class));
    }

    @Test
    void verifyAnswer_CorrectAnswer() {
        // Arrange
        Trivia trivia = createTrivia();
        when(triviaRepository.findById(0)).thenReturn(Optional.of(trivia));

        // Act
        Optional<TriviaResponseStatus> responseStatus = service.verifyAnswer("0", "Chile");

        // Assert
        assertEquals(TriviaResponseStatus.RIGHT, responseStatus.get());
        verify(triviaRepository, times(1)).delete(trivia);
    }

    @Test
    void verifyAnswer_IncorrectAnswer() {
        // Arrange
        Trivia trivia = createTrivia();
        when(triviaRepository.findById(0)).thenReturn(Optional.of(trivia));

        // Act
        Optional<TriviaResponseStatus> responseStatus = service.verifyAnswer("0", "Argentina");

        // Assert
        assertEquals(TriviaResponseStatus.WRONG, responseStatus.get());
        verify(triviaRepository, times(1)).save(any(Trivia.class));
    }

    @Test
    void verifyAnswer_MaxAttemptsReached() {
        // Arrange
        Trivia trivia = createTrivia();
        trivia.setAnswer_attempts(3);
        when(triviaRepository.findById(0)).thenReturn(Optional.of(trivia));

        // Act
        Optional<TriviaResponseStatus> responseStatus = service.verifyAnswer("0", "Argentina");

        // Assert
        assertEquals(TriviaResponseStatus.MAX_ATTEMPTS_REACHED, responseStatus.get());
        verify(triviaRepository, never()).save(any(Trivia.class));
    }

    @Test
    void verifyAnswer_NoSuchQuestion() {
        // Arrange
        when(triviaRepository.findById(0)).thenReturn(Optional.empty());

        // Act
        Optional<TriviaResponseStatus> responseStatus = service.verifyAnswer("0", "Chile");

        // Assert
        assertEquals(TriviaResponseStatus.NO_SUCH_QUESTION, responseStatus.get());
    }

    @Test
    void createTriviaQuestion_ThrowsFeignException() {
        // Arrange
        FeignException feignException = mock(FeignException.class);
        when(triviaQuestionClient.getTriviaQuestion(1)).thenThrow(feignException);

        // Act & Assert
        assertThrows(FeignException.class, () -> service.createTriviaQuestion());
    }

    private static Trivia createTrivia(){
        return Trivia.builder().triviaId(0).correct_answer("Chile").answer_attempts(0)
                .question("Which soccer team won the Copa America 2015 Championship?").build();
    }
}