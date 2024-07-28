package exercise.santander.solution.exception;

import exercise.santander.solution.domain.ApiError;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Test
    void handleFeignException() {
        // Arrange
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(500);
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        // Act
        ResponseEntity<ApiError> response = handler.handleFeignException(feignException);

        // Assert
        assertEquals(500, response.getStatusCode().value());
        assertEquals("Something went wrong whilst calling 3rd party", response.getBody().error());
    }
}