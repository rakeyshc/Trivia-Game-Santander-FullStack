package exercise.santander.solution.repository;

import exercise.santander.solution.entity.Trivia;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


@DataJpaTest
class TriviaRepositoryTest {

    @Autowired
    private TriviaRepository triviaRepository;

    private Trivia trivia;

    @BeforeEach
    void setUp() {
        trivia = new Trivia(1,"dummy questions ?", "answer",0);
        triviaRepository.save(trivia);

    }

    @AfterEach
    void tearDown() {
        triviaRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFetchTrivia() throws Exception {
        var findTriviaById = triviaRepository.findById(1);
        assertThat(findTriviaById,is(Optional.of(trivia)));
    }

    @Test
    void shouldDeleteTheValueFetch(){
        triviaRepository.delete(trivia);
        var findTriviaById = triviaRepository.findById(1);
        assertThat(findTriviaById, is(Optional.empty()));
    }


}