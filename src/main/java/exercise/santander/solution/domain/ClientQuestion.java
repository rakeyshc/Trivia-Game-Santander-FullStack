package exercise.santander.solution.domain;

import java.util.List;

public record ClientQuestion(
        String category,
        String type,
        String difficulty,
        String question,
        String correct_answer,
        List<String> incorrect_answers
) {}