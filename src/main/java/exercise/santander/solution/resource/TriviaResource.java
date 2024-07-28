package exercise.santander.solution.resource;


import exercise.santander.solution.domain.AnswerRequest;
import exercise.santander.solution.domain.CreateTriviaResponse;
import exercise.santander.solution.domain.TriviaResponse;
import exercise.santander.solution.service.TriviaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/trivia")
public class TriviaResource {

    private final TriviaService triviaService;

    @PostMapping("/start")
    public @ResponseBody ResponseEntity<CreateTriviaResponse> createTriviaQuestion() {
            return triviaService.createTriviaQuestion().map(ResponseEntity::ok)
                    .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/reply/{triviaId}", produces = "application/json")
    public @ResponseBody ResponseEntity<TriviaResponse> replyAnswer(@PathVariable String triviaId,
                                                                    @RequestBody AnswerRequest answerRequest){
        return triviaService
                .verifyAnswer(triviaId, answerRequest.answer())
                .map(triviaResponse -> ResponseEntity.status(triviaResponse.getStatus())
                        .body(new TriviaResponse(triviaResponse.getResponse())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TriviaResponse("No such question!")));
    }
}
