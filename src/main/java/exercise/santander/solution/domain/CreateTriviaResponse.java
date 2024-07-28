package exercise.santander.solution.domain;

import java.util.List;

public record CreateTriviaResponse(Integer triviaId, List<String> possibleAnswers) {
}
