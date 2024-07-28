package exercise.santander.solution.domain;

import org.springframework.http.HttpStatus;
public enum TriviaResponseStatus {
    NO_SUCH_QUESTION(HttpStatus.NOT_FOUND, "No such question!"),
    MAX_ATTEMPTS_REACHED(HttpStatus.FORBIDDEN, "Max attempts reached!"),
    RIGHT(HttpStatus.OK, "right!"),
    WRONG(HttpStatus.BAD_REQUEST, "wrong!");

    private final HttpStatus status;
    private final String response;

    TriviaResponseStatus(HttpStatus status, String response) {
        this.status = status;
        this.response = response;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getResponse() {
        return response;
    }
}
