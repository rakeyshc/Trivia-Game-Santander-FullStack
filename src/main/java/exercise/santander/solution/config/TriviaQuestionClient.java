package exercise.santander.solution.config;

import exercise.santander.solution.domain.ClientQuestionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="trivia-question",  url="${trivia.question.url}")
public interface TriviaQuestionClient {

    @GetMapping
    ClientQuestionResponse getTriviaQuestion(@RequestParam("amount") int amount);
}
