package ro.utcn.sd.agui.a1.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.utcn.sd.agui.a1.entity.Answer;
import ro.utcn.sd.agui.a1.entity.Tag;
import ro.utcn.sd.agui.a1.entity.User;
import ro.utcn.sd.agui.a1.exception.TagNotFoundException;
import ro.utcn.sd.agui.a1.service.*;
import ro.utcn.sd.agui.a1.exception.UserNotFoundException;
import ro.utcn.sd.agui.a1.exception.QuestionNotFoundException;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ConsoleController implements CommandLineRunner {

    private final Scanner scanner = new Scanner(System.in);
    private final UserManagementService userManagementService;
    private final QuestionManagementService questionManagementService;
    private final TagManagementService tagManagementService;
    private final AnswerManagementService answerManagementService;
    private final QuestionVoteManagementService questionVoteManagementService;
    private final AnswerVoteManagementService answerVoteManagementService;

    private User currentUser;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Welcome to Stack Overflow by Andreea Gui!");

        boolean done = false;

        while (!done) {

            System.out.println("Enter a command: ");
            String command = scanner.nextLine().trim();
            try {
                done = handleCommand(command);

            } catch (UserNotFoundException e) {
                System.out.println("User was not found!");
            } catch (QuestionNotFoundException e) {
                System.out.println("Question was not found!");
            } catch (TagNotFoundException e) {
                System.out.println("Tag was not found.");
            }

        }

    }

    private boolean handleCommand(String command) {

        switch (command) {
            case "login":
                handleLogin();
                return false;
            case "logout":
                handleLogout();
                return false;
            case "register as user":
                handleRegisterAsUser();
                return false;
            case "exit":
                return true;
        }

        if (currentUser == null) {
            System.out.println("You need to be logged in to issue commands! ");
            return false;
        }

        System.out.println("Your command is: " + command);
        switch (command) {
            case "list users":
                handleListUsers();
                return false;
            case "list questions":
                handleListQuestions();
                return false;
            case "logout":
                handleLogout();
                return false;
            case "add question":
                handleAddQuestion();
                return false;
            case "filter questions by tag":
                handleFilterQuestionsByTag();
                return false;
            case "filter questions by title":
                handleFilterQuestionsByTitle();
                return false;
            case "add answer":
                handleAddAnswer();
                return false;
            case "list all answers":
                handleListAllAnswers();
                return false;
            case "delete answer":
                handleDeleteAnswer();
                return false;
            case "update answer":
                handleUpdateAnswer();
                return false;
            case "list answers":
                handleListAnswersByQuestion();
                return false;
            case "list all question votes":
                handleListAllQuestionVotes();
                return false;
            case "add question vote":
                handleAddQuestionVote();
                return false;
            case "delete question vote":
                handleDeleteQuestionVote();
                return false;
            case "change question vote":
                handleChangeQuestionVote();
                return false;
            case "list all answer votes":
                handleListAllAnswerVotes();
                return false;
            case "add answer vote":
                handleAddAnswerVote();
                return false;
            case "delete answer vote":
                handleDeleteAnswerVote();
                return false;
            case "change answer vote":
                handleChangeAnswerVote();
                return false;
            case "list answers by score":
                handleListAnswersByScore();
                return false;
            case "exit":
                return true;
            default:
                System.out.println("Unknown command. Please try again! ");
                return false;
        }
    }

    private void handleListUsers() {
        userManagementService.listAllUsers().forEach(s -> System.out.println(s.toString()));
    }

    private void handleListQuestions() {
        questionManagementService.listAllQuestions().forEach(s -> System.out.println(s.toString()));
    }

    private void handleRegisterAsUser() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        boolean registrationHappened = userManagementService.registerAsUser(username, password);
        if (registrationHappened) {
            System.out.println("New user " + username + " was registered.");
        } else {
            System.out.println("The username " + username + " already exists. Please try another one!");

        }
    }

    private void handleLogin() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        Optional<User> logged = userManagementService.login(username, password);
        if (logged.isPresent()) {
            System.out.println(username + " is logged in.");
            currentUser = logged.get();
        } else {
            System.out.println("Login failed.");
        }
    }

    private void handleLogout() {
        System.out.println("You have logged out.");
        currentUser = null;
    }

    private void handleAddQuestion() {


        System.out.println("Title: ");
        String title = scanner.nextLine();
        System.out.println("Text: ");
        String text = scanner.nextLine();
        System.out.println("Write your tags separated by blank space: ");
        String tagNames = scanner.nextLine();
        questionManagementService.addQuestion(currentUser.getUserId(), title, text, tagNames);


    }

    private void handleFilterQuestionsByTag() {


        System.out.println("Write a tag: ");
        String tagName = scanner.nextLine();

        Tag foundTag = tagManagementService.findTagByName(tagName);
        questionManagementService.filterQuestionsByTag(foundTag).forEach(s -> System.out.println(s.toString()));


    }

    private void handleFilterQuestionsByTitle() {


        System.out.println("Write the text: ");
        String text = scanner.nextLine();
        questionManagementService.filterQuestionsByTitle(text).forEach(s -> System.out.println(s.toString()));

    }

    private void handleAddAnswer() {
        System.out.println("Write the answered question: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Write the text: ");
        String text = scanner.nextLine();
        int checkNumber = answerManagementService.addAnswer(questionId, currentUser.getUserId(), text);
        if (checkNumber == -1) {
            System.out.println("The selected question does not exist!");
        } else {
            System.out.println("The answer was added with id: " + checkNumber);
        }

    }

    private void handleListAllAnswers() {
        answerManagementService.listAllAnswers().forEach(s -> System.out.println(s.toString()));
    }

    private void handleDeleteAnswer() {
        System.out.println("Give the answer id you want to delete: ");
        int answerId = scanner.nextInt();
        scanner.nextLine();
        int checkNumber = answerManagementService.deleteAnswer(answerId, currentUser.getUserId());
        if (checkNumber == -1) {
            System.out.println("The answer does not exist.");
        } else {
            if (checkNumber == -2) {
                System.out.println("The answer is not written by you. Cannot be deleted.");
            } else {
                System.out.println("Your answer " + answerId + " was deleted.");
            }
        }

    }

    private void handleUpdateAnswer() {
        System.out.println("Give the answer id you want to update: ");
        int answerId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Write the updated text: ");
        String text = scanner.nextLine();
        int checkNumber = answerManagementService.updateAnswer(answerId, currentUser.getUserId(), text);
        if (checkNumber == -1) {
            System.out.println("The answer does not exist.");
        } else {
            if (checkNumber == -2) {
                System.out.println("Cannot update this answer.");
            } else {
                System.out.println("Your answer " + answerId + " was updated.");
            }
        }
    }

    private void handleListAnswersByQuestion() {

        System.out.println("Write the question id: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();
        List<Answer> answers = answerManagementService.listAllAnswersPerQuestion(questionId);
        if (answers == null) {
            System.out.println("The question does not exist");
        } else {
            if (answers.isEmpty()) {
                System.out.println("There are no answers yet.");
            } else {
                answers.forEach(s -> System.out.println(s.toString()));
            }
        }

    }

    private void handleListAllQuestionVotes() {
        questionVoteManagementService.listAllQuestionVotes().forEach(s -> System.out.println(s.toString()));
    }

    private void handleAddQuestionVote() {
        System.out.println("Write the question id: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Your vote is (true/false): ");
        boolean type = scanner.nextBoolean();
        scanner.nextLine();
        int checkNumber = questionVoteManagementService.addQuestionVote(questionId, currentUser.getUserId(), type);
        if (checkNumber == -1) {
            System.out.println("The question does not exist.");
        } else {
            if (checkNumber == -2) {
                System.out.println("You cannot vote your own question.");
            } else {
                if (checkNumber == -3) {
                    System.out.println("You can vote only once a question.");
                }
                System.out.println("You voted with " + type + " question " + questionId);
            }
        }

    }

    private void handleDeleteQuestionVote() {
        System.out.println("Give the vote id you want to delete: ");
        int voteId = scanner.nextInt();
        scanner.nextLine();
        int checkNumber = questionVoteManagementService.deleteQuestionVote(voteId, currentUser.getUserId());
        if (checkNumber == -1) {
            System.out.println("The vote does not exist.");
        } else {
            if (checkNumber == -2) {
                System.out.println("It is not your vote.");
            } else {
                System.out.println("Your vote " + voteId + " was deleted.");
            }
        }
    }

    private void handleChangeQuestionVote() {
        System.out.println("Give the vote id you want to update: ");
        int voteId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Give the new type: ");
        Boolean type = scanner.nextBoolean();
        scanner.nextLine();
        int checkNumber = questionVoteManagementService.updateQuestionVote(voteId, currentUser.getUserId(), type);
        if (checkNumber == -1) {
            System.out.println("The vote does not exist.");
        } else {
            if (checkNumber == -2) {
                System.out.println("It is not your vote.");
            } else {
                System.out.println("Your vote " + voteId + " was changed.");
            }
        }
    }

    private void handleListAllAnswerVotes() {
        answerVoteManagementService.listAllAnswerVotes().forEach(s -> System.out.println(s.toString()));
    }

    private void handleAddAnswerVote() {
        System.out.println("Write the answer id: ");
        int answerId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Your vote is (true/false): ");
        boolean type = scanner.nextBoolean();
        scanner.nextLine();
        int checkNumber = answerVoteManagementService.addAnswerVote(answerId, currentUser.getUserId(), type);
        if (checkNumber == -1) {
            System.out.println("The answer does not exist.");
        } else {
            if (checkNumber == -2) {
                System.out.println("You cannot vote your own answer.");
            } else {
                if (checkNumber == -3) {
                    System.out.println("You can vote only once an answer.");
                } else {
                    System.out.println("You voted with " + type + " question " + answerId);
                }
            }
        }
    }

    private void handleDeleteAnswerVote() {
        System.out.println("Give the vote id you want to delete:");
        int voteId = scanner.nextInt();
        scanner.nextLine();
        int checkNumber = answerVoteManagementService.deleteAnswerVote(voteId, currentUser.getUserId());
        if (checkNumber == -1) {
            System.out.println("The vote does not exist.");
        } else {
            if (checkNumber == -2) {
                System.out.println("It is not your vote.");
            } else {
                System.out.println("Your vote " + voteId + " was deleted.");
            }
        }
    }

    private void handleChangeAnswerVote() {
        System.out.println("Give the vote id you want to update: ");
        int voteId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Give the new type:");
        Boolean type = scanner.nextBoolean();
        scanner.nextLine();
        int checkNumber = answerVoteManagementService.updateAnswerVote(voteId, currentUser.getUserId(), type);
        if (checkNumber == -1) {
            System.out.println("The vote does not exist.");
        } else {
            if (checkNumber == -2) {
                System.out.println("It is not your vote.");
            } else {
                System.out.println("Your vote " + voteId + " was changed.");
            }
        }
    }

    private void handleListAnswersByScore(){
        System.out.println("Write the question id:");
        int questionId = scanner.nextInt();
        scanner.nextLine();
        Map<Answer, Integer> answers = answerVoteManagementService.listAnswersByScore(questionId);
        if(answers!=null){
            answers.entrySet().stream().forEach(e-> System.out.println(e.toString()));
        }else{
            System.out.println("No question was found.");
        }
    }
}
