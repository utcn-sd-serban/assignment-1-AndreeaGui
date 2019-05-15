package ro.utcn.sd.agui.a1.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ro.utcn.sd.agui.a1.entity.Answer;
import ro.utcn.sd.agui.a1.entity.Question;
import ro.utcn.sd.agui.a1.entity.User;
import ro.utcn.sd.agui.a1.persistence.AnswerRepository;
import ro.utcn.sd.agui.a1.persistence.QuestionRepository;
import ro.utcn.sd.agui.a1.persistence.RepositoryFactory;
import ro.utcn.sd.agui.a1.persistence.UserRepository;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)

public class UserSeed implements CommandLineRunner {

    private final RepositoryFactory factory;

    public void run(String... args) throws Exception {
        UserRepository userRepository = factory.createUserRepository();
        if (userRepository.findAll().isEmpty()) {
            userRepository.save(new User(null, "A", "abc1", 0, "USER", false));
            userRepository.save(new User(null, "B", "abc2", 0, "USER", false));
            userRepository.save(new User(null, "C", "abc3", 0, "USER", false));
            userRepository.save(new User(null, "ana", "000", 0, "USER", false));
        }

        QuestionRepository questionRepository = factory.createQuestionRepository();
        if (questionRepository.findAll().isEmpty()) {
            questionRepository.save(new Question(null, 1, "Title One", "Text One", new Timestamp(System.currentTimeMillis())));
            questionRepository.save(new Question(null, 2, "Title Two", "Text Two", new Timestamp(System.currentTimeMillis())));
            questionRepository.save(new Question(null, 2, "Title Three", "Text three", new Timestamp(System.currentTimeMillis())));

        }

        AnswerRepository answerRepository = factory.createAnswerRepository();
        if (answerRepository.findAll().isEmpty()) {
            answerRepository.save(new Answer(null, 1, 1,
                    "Answer 1 for question 1", new Timestamp(System.currentTimeMillis())));
            answerRepository.save(new Answer(null, 1, 2,
                    "Answer 2 for question 1", new Timestamp(System.currentTimeMillis())));
        }

    }
}
