package exercise.santander.solution;

import exercise.santander.solution.config.TriviaQuestionClient;
import exercise.santander.solution.repository.TriviaRepository;
import exercise.santander.solution.resource.TriviaResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SmokeTest {

	@Autowired
	private TriviaResource triviaResource;

	@Autowired
	private TriviaRepository triviaRepository;

	@Autowired
	private TriviaQuestionClient triviaQuestionClient;

	@Test
	void smokeTest() {
		assertThat(triviaResource).isNotNull();
		assertThat(triviaRepository).isNotNull();
		assertThat(triviaQuestionClient).isNotNull();
	}

}
