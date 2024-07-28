package exercise.santander.solution.domain;

import java.util.List;

public record CreateQuestionResponse(Integer triviaId, List<String> possibleAnswers) {
}
