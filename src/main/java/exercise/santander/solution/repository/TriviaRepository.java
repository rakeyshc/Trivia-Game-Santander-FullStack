package exercise.santander.solution.repository;

import exercise.santander.solution.entity.Trivia;
import org.springframework.data.repository.CrudRepository;

public interface TriviaRepository extends CrudRepository<Trivia, Integer> {
}
