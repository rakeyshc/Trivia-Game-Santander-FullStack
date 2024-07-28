package exercise.santander.solution.exception;


import exercise.santander.solution.domain.ApiError;
import feign.FeignException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleFeignException(FeignException exception) {
        return ResponseEntity.status(HttpStatusCode.valueOf(exception.status())).body(new ApiError("Something went wrong whilst calling 3rd party"));
    }
}
