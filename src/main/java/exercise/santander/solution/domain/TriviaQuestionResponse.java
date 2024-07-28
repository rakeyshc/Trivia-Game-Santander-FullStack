package exercise.santander.solution.domain;

import java.util.List;

public record TriviaQuestionResponse(
        int response_code,
        List<TriviaQuestion> results
) {}