package exercise.santander.solution.domain;

import java.util.List;

public record TriviaQuestion(
        String category,
        String type,
        String difficulty,
        String question,
        String correct_answer,
        List<String> incorrect_answers
) {}