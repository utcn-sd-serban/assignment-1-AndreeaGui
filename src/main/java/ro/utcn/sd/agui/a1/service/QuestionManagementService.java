package ro.utcn.sd.agui.a1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.agui.a1.entity.Question;
import ro.utcn.sd.agui.a1.entity.Tag;
import ro.utcn.sd.agui.a1.persistence.QuestionRepository;
import ro.utcn.sd.agui.a1.persistence.RepositoryFactory;
import ro.utcn.sd.agui.a1.persistence.TagRepository;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionManagementService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Question> listAllQuestions() {
        return repositoryFactory.createQuestionRepository().findAll();
    }

    @Transactional
    public int addQuestion(int userId, String title, String text, String tags) {

        TagRepository tagRepository = repositoryFactory.createTagRepository();
        QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();

        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        //create the object
        Question question = new Question(null, userId, title, text, dateTime);
        //save the object in the persistence
        questionRepository.save(question);

        String[] tagNames = tags.split("\\s");
        List<Tag> tagsOfQuestion = new ArrayList<>(); //the tags of the question as objects
        for (String iterationTagName : tagNames) {
            Optional<Tag> foundTag = tagRepository.findByName(iterationTagName);
            if (foundTag.isPresent()) {
                tagRepository.addTagToQuestion(foundTag.get(), question);
            } else {
                Tag newTag = new Tag(null, iterationTagName); //create tag object
                newTag = tagRepository.save(newTag); //add the tag object in the persistence
                tagRepository.addTagToQuestion(newTag, question); //link the tag to the question in persistence
            }
        }
        return question.getQuestionId();

    }


    @Transactional
    public List<Question> filterQuestionsByTag(Tag tag) {
        List<Question> questions = repositoryFactory.createQuestionRepository().filterByTag(tag).stream()
                .sorted(Comparator.comparing(Question::getDateTime))
                .collect(Collectors.toList());
        Collections.reverse(questions);
        return questions;
    }

    @Transactional
    public List<Question> filterQuestionsByTitle(String title) {

        List<Question> questions = repositoryFactory.createQuestionRepository().findAll().stream().filter(x -> x.getTitle().toLowerCase()
                .contains(title.toLowerCase())).sorted(Comparator.comparing(Question::getDateTime))
                .collect(Collectors.toList());
        Collections.reverse(questions);
        return questions;
    }
}
