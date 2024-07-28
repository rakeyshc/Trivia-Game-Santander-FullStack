package exercise.santander.solution.resource;


import exercise.santander.solution.domain.AnswerRequest;
import exercise.santander.solution.domain.CreateQuestionResponse;
import exercise.santander.solution.domain.TriviaResponseStatus;
import exercise.santander.solution.service.TriviaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/trivia")
public class TriviaResource {

    private final TriviaService triviaService;

    @PostMapping("/start")
    public ResponseEntity<List<CreateQuestionResponse>> createTriviaQuestion() {
            Optional<List<CreateQuestionResponse>> response = triviaService.createTriviaQuestion();
            return response.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @PutMapping("/reply/{triviaId}")
    public ResponseEntity<String> replyAnswer(@PathVariable String triviaId, @RequestBody AnswerRequest answerRequest){
        TriviaResponseStatus response =  triviaService.verifyAnswer(triviaId,answerRequest.answer());
        return ResponseEntity.status(response.getStatus()).body(response.getResponse());
    }
}
