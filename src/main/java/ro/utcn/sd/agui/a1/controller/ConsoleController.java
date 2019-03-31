package ro.utcn.sd.agui.a1.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.utcn.sd.agui.a1.entity.Tag;
import ro.utcn.sd.agui.a1.entity.User;
import ro.utcn.sd.agui.a1.exception.TagNotFoundException;
import ro.utcn.sd.agui.a1.service.QuestionManagementService;
import ro.utcn.sd.agui.a1.service.TagManagementService;
import ro.utcn.sd.agui.a1.service.UserManagementService;
import ro.utcn.sd.agui.a1.exception.UserNotFoundException;
import ro.utcn.sd.agui.a1.exception.QuestionNotFoundException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ConsoleController implements CommandLineRunner {

    private final Scanner scanner = new Scanner(System.in);
    private final UserManagementService userManagementService;
    private final QuestionManagementService questionManagementService;
    private final TagManagementService tagManagementService;

    private User currentUser;

    @Override
    public void run(String... args) throws Exception{

        System.out.println("Welcome to Stack Overflow by Andreea Gui!");

        boolean done = false;

        while(!done){

            System.out.println("Enter a command: ");
            String command = scanner.nextLine().trim();
            try{
                done = handleCommand(command);

            } catch (UserNotFoundException e){
                System.out.println("User was not found!");
            } catch (QuestionNotFoundException e){
                System.out.println("Question was not found!");
            }catch (TagNotFoundException e){
                System.out.println("Tag was not found.");
            }

        }

    }

    private boolean handleCommand(String command) {

        switch (command){
            case "login":
                handleLogin();
                return false;
            case "logout":
                handleLogout();
                return false;
            case "exit":
                return true;
        }

        if (currentUser == null){
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
            case "register as user":
                handleRegisterAsUser();
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
            case "exit":
                return true;
            default:
                System.out.println("Unknown command. Please try again! ");
                return false;
        }
    }

    private void handleListUsers(){
        userManagementService.listAllUsers().forEach(s->System.out.println(s.toString()));
    }

    private void handleListQuestions(){
        questionManagementService.listAllQuestions().forEach(s->System.out.println(s.toString()));
    }

    private void handleRegisterAsUser(){
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        boolean registrationHappened = userManagementService.registerAsUser(username, password);
        if(registrationHappened){
            System.out.println("New user " + username + " was registered.");
        }
        else
        {
            System.out.println("The username "+ username + " already exists. Please try another one!");

        }
    }

    private void handleLogin(){
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        Optional<User> logged = userManagementService.login(username, password);
        if(logged.isPresent()){
            System.out.println(username+" is logged in.");
            currentUser = logged.get();
        }
        else
        {
            System.out.println("Login failed.");
        }
    }

    private void handleLogout(){
        System.out.println("You have logged out.");
        currentUser = null;
    }

    private void handleAddQuestion(){


        System.out.println("Title: ");
        String title = scanner.nextLine();
        System.out.println("Text: ");
        String text = scanner.nextLine();
        System.out.println("Write your tags separated by blank space: ");
        String tagNames = scanner.nextLine();
        questionManagementService.addQuestion(currentUser.getUserId(), title, text, tagNames);


    }

    private void handleFilterQuestionsByTag(){


        System.out.println("Write a tag: ");
        String tagName = scanner.nextLine();

        Tag foundTag = tagManagementService.findTagByName(tagName);
        questionManagementService.filterQuestionsByTag(foundTag).forEach(s->System.out.println(s.toString()));


    }

    private void handleFilterQuestionsByTitle(){


        System.out.println("Write the text: ");
        String text = scanner.nextLine();
        questionManagementService.filterQuestionsByTitle(text).forEach(s-> System.out.println(s.toString()));

    }

}
