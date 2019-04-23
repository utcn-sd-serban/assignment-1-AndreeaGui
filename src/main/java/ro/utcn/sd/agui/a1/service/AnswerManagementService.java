package ro.utcn.sd.agui.a1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.agui.a1.entity.Answer;
import ro.utcn.sd.agui.a1.entity.Question;
import ro.utcn.sd.agui.a1.persistence.AnswerRepository;
import ro.utcn.sd.agui.a1.persistence.QuestionRepository;
import ro.utcn.sd.agui.a1.persistence.RepositoryFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerManagementService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Answer> listAllAnswers() {
        return repositoryFactory.createAnswerRepository().findAll();
    }

    @Transactional
    public int addAnswer(int questionId, int userId, String text) {

        QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();
        AnswerRepository answerRepository = repositoryFactory.createAnswerRepository();


        if (questionRepository.findById(questionId).isPresent()) {
            Timestamp dateTime = new Timestamp(System.currentTimeMillis());
            Answer answer = new Answer(null, questionId, userId, text, dateTime);
            return answerRepository.save(answer).getAnswerId();
        } else {
            return -1;
        }
    }

    @Transactional
    public int deleteAnswer(int answerId, int userId) {

        AnswerRepository answerRepository = repositoryFactory.createAnswerRepository();
        Optional<Answer> foundAnswer = answerRepository.findById(answerId);

        if (foundAnswer.isPresent()) {
            if (foundAnswer.get().getUserId() == userId) {
                answerRepository.remove(foundAnswer.get());
                return answerId;
            } else {
                return -2; //the answer is not of the current user
            }
        } else {
            return -1; //answer not found
        }
    }

    @Transactional
    public int updateAnswer(int answerId, int userId, String text) {

        AnswerRepository answerRepository = repositoryFactory.createAnswerRepository();
        Optional<Answer> foundAnswer = answerRepository.findById(answerId);

        if (foundAnswer.isPresent()) {
            if (foundAnswer.get().getUserId() == userId) {
                Answer updatedAnswer = foundAnswer.get();
                updatedAnswer.setText(text);
                answerRepository.save(updatedAnswer);
                return answerId;
            } else {
                return -2; //the answer is not of the current user
            }
        } else {
            return -1; //answer not found
        }
    }

    @Transactional
    public List<Answer> listAllAnswersPerQuestion(int questionId) {

        AnswerRepository answerRepository = repositoryFactory.createAnswerRepository();
        QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();
        Optional<Question> foundQuestion = questionRepository.findById(questionId);

        if (foundQuestion.isPresent())
            return answerRepository.findAllByQuestion(foundQuestion.get());
        else
            return null;
    }


}
