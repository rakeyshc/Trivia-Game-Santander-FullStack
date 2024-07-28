package exercise.santander.solution.service;


import exercise.santander.solution.config.TriviaQuestionClient;
import exercise.santander.solution.domain.TriviaQuestion;
import exercise.santander.solution.domain.TriviaQuestionResponse;
import exercise.santander.solution.domain.CreateQuestionResponse;
import exercise.santander.solution.domain.TriviaResponseStatus;
import exercise.santander.solution.entity.Trivia;
import exercise.santander.solution.repository.TriviaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class TriviaService {

    private final TriviaRepository triviaRepository;

    private final TriviaQuestionClient triviaQuestionClient;

    public Optional<List<CreateQuestionResponse>> createTriviaQuestion() {

        //Hardcoded to amount = 1 but can be configured...
        TriviaQuestionResponse triviaQuestionResponse = triviaQuestionClient.getTriviaQuestion(1);
        if (triviaQuestionResponse.response_code() == 0 && !triviaQuestionResponse.results().isEmpty()) {
            List<CreateQuestionResponse> responseItems = triviaQuestionResponse.results().stream()
                    .map(this::combineAnswers)
                    .map(this::saveTriviaQuestion)
                    .collect(Collectors.toList());

            return Optional.ofNullable(responseItems);
        }
        return Optional.empty();
    }

    public TriviaResponseStatus verifyAnswer(String triviaId, String answer) {
        Optional<Trivia> recordExists = triviaRepository.findById(Integer.parseInt(triviaId));
        return recordExists.map(record -> validate(record,answer)).orElse(TriviaResponseStatus.NO_SUCH_QUESTION);
    }

    private CreateQuestionResponse saveTriviaQuestion(CombinedResult combinedResult){
       Trivia persistedRecord =  triviaRepository.save(
                Trivia.builder()
                        .question(combinedResult.result().question())
                        .answer_attempts(0)
                        .correct_answer(combinedResult.result().correct_answer())
                        .build());
       return new CreateQuestionResponse(persistedRecord.getTriviaId(), combinedResult.combinedAnswers);

    }

    private CombinedResult combineAnswers(TriviaQuestion result) {
        List<String> combinedAnswers = Stream.of(
                        result.incorrect_answers(),
                        Arrays.asList(result.correct_answer()))
                .flatMap(l -> l.stream()).collect(Collectors.toList());

        return new CombinedResult(result, combinedAnswers);
    }

    private  TriviaResponseStatus validate(Trivia record, String answer){
        if(record.getAnswer_attempts() > 2){
            return TriviaResponseStatus.MAX_ATTEMPTS_REACHED;
        }else if(record.getCorrect_answer().equalsIgnoreCase(answer)){
            triviaRepository.delete(record);
            return TriviaResponseStatus.RIGHT;
        }else{
            record.setAnswer_attempts(record.getAnswer_attempts() + 1);
            triviaRepository.save(record);
            return TriviaResponseStatus.WRONG;
        }
    }

    private record CombinedResult(TriviaQuestion result, List<String> combinedAnswers) {
    }

}
