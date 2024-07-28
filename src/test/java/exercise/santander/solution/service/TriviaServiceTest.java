package exercise.santander.solution.service;

import exercise.santander.solution.config.TriviaQuestionClient;
import exercise.santander.solution.domain.CreateQuestionResponse;
import exercise.santander.solution.domain.TriviaQuestion;
import exercise.santander.solution.domain.TriviaQuestionResponse;
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
        TriviaQuestion triviaQuestion = new TriviaQuestion("Sports", "multiple", "medium", "Which soccer team won the Copa America 2015 Championship?", "Chile", List.of("Argentina", "Brazil", "Paraguay"));
        TriviaQuestionResponse triviaQuestionResponse = new TriviaQuestionResponse(0, List.of(triviaQuestion));
        when(triviaQuestionClient.getTriviaQuestion(1)).thenReturn(triviaQuestionResponse);
        when(triviaRepository.save(any(Trivia.class))).thenReturn(createTrivia());

        // Act
        Optional<List<CreateQuestionResponse>> responseEntity = service.createTriviaQuestion();

        // Assert
        assertEquals(0, responseEntity.get().stream().findFirst().get().triviaId());
        assertEquals(4, responseEntity.get().stream().findFirst().get().possibleAnswers().size());
        verify(triviaRepository, times(1)).save(any(Trivia.class));

    }

    @Test
    void createTriviaQuestion_Failure() {
        // Arrange
        TriviaQuestionResponse triviaQuestionResponse = new TriviaQuestionResponse(1, List.of());
        when(triviaQuestionClient.getTriviaQuestion(1)).thenReturn(triviaQuestionResponse);

        // Act
        Optional<List<CreateQuestionResponse>> response = service.createTriviaQuestion();

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
        TriviaResponseStatus responseStatus = service.verifyAnswer("0", "Chile");

        // Assert
        assertEquals(TriviaResponseStatus.RIGHT, responseStatus);
        verify(triviaRepository, times(1)).delete(trivia);
    }

    @Test
    void verifyAnswer_IncorrectAnswer() {
        // Arrange
        Trivia trivia = createTrivia();
        when(triviaRepository.findById(0)).thenReturn(Optional.of(trivia));

        // Act
        TriviaResponseStatus responseStatus = service.verifyAnswer("0", "Argentina");

        // Assert
        assertEquals(TriviaResponseStatus.WRONG, responseStatus);
        verify(triviaRepository, times(1)).save(any(Trivia.class));
    }

    @Test
    void verifyAnswer_MaxAttemptsReached() {
        // Arrange
        Trivia trivia = createTrivia();
        trivia.setAnswer_attempts(3);
        when(triviaRepository.findById(0)).thenReturn(Optional.of(trivia));

        // Act
        TriviaResponseStatus responseStatus = service.verifyAnswer("0", "Argentina");

        // Assert
        assertEquals(TriviaResponseStatus.MAX_ATTEMPTS_REACHED, responseStatus);
        verify(triviaRepository, never()).save(any(Trivia.class));
    }

    @Test
    void verifyAnswer_NoSuchQuestion() {
        // Arrange
        when(triviaRepository.findById(0)).thenReturn(Optional.empty());

        // Act
        TriviaResponseStatus responseStatus = service.verifyAnswer("0", "Chile");

        // Assert
        assertEquals(TriviaResponseStatus.NO_SUCH_QUESTION, responseStatus);
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