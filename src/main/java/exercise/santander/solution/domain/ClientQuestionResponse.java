package exercise.santander.solution.domain;

import java.util.List;

public record ClientQuestionResponse(
        int response_code,
        List<ClientQuestion> results
) {}