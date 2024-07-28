package exercise.santander.solution.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Trivia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer triviaId;
    String question;
    String correct_answer;

    int answer_attempts;
}
