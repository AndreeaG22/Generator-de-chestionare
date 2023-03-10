package com.example.project;

import java.io.*;
import java.util.ArrayList;


public class TestValidity extends UserToAdd{

    TestValidity() {
    }

    /* metodele din aceasta clasa au rolul de a apela metodele corespunzatoare pentru a verifica daca utilizatorul a
    * introdus corect datele de la tastatura, iar in cazul in care regulile au fost respectate, sa se reallizeze
    * actiunea ceruta de utilizator */

    public void verifyUserFormat(String[] arrayOfArgs) {
        if(checkCredentials(arrayOfArgs) == 1) {
            return;
        }
        String username = getWord(arrayOfArgs, 1);
        String password = getWord(arrayOfArgs, 2);

        String userToAdd = username + "," + password;
        boolean verifyUser = verificationUser(userToAdd);

        if(verifyUser) {
            System.out.println("{ 'status' : 'error', 'message' : 'User already exists'}");
            return;
        }
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);
        writeUserInFile(user.getUsername(), user.getPassword());
    }
    public void verifyQuestionFormat(String[] arrayOfArgs) {
        if(checkAuthentication(arrayOfArgs) == 1)
            return;
        QuestionToAdd questionToAdd = new QuestionToAdd();
        questionToAdd.verifyFormat(arrayOfArgs);
    }

    public void verifyGetQuestionFormat(String[] arrayOfArgs) {
        if(checkAuthentication(arrayOfArgs) == 1)
            return;
        String question = getWord(arrayOfArgs, 3);
        Questions questionObj = new Questions();
        String qId = questionObj.findQuestionInFile(question);
        if(qId == null) {
            System.out.println("{ 'status' : 'error', 'message' : 'Question does not exist'}");
        } else {
            System.out.println("{ 'status' : 'ok', 'message' : '" + qId + "' }");
        }

    }

    public void verifyGetAllQuestionsFormat(String[] arrayOfArgs) {
        if(checkAuthentication(arrayOfArgs) == 1)
            return;
        Questions questionObj = new Questions();
        System.out.println(questionObj.getAllQ());
    }

    public void verifyCreateQuizzFormat(String[] arrayOfArgs, ArrayList<Users> users) {
        if(checkAuthentication(arrayOfArgs) == 1)
            return;

        QuizzToAdd quizzToAdd = new QuizzToAdd();
        if(quizzToAdd.checkQuizzFormat(arrayOfArgs) == 1)
            return;
        Users userObj = new Users();
        String quizz = arrayOfArgs[3].split("'")[1];
        int noQzz;
        String username = arrayOfArgs[1].split("'")[1];
        int userIndexQz = userObj.findUserIndex(users, username);
        if(userIndexQz == -1) {
            noQzz = 1;
        } else {
            noQzz = users.get(userIndexQz).noQuizzes + 1;
        }
        userObj.setUserNoQzz(users, username, noQzz);
        Quizz qzz = new Quizz(quizz, noQzz);
        qzz.implemetQuizz(arrayOfArgs);
    }


    public void verifyGetQuizzFormat(String[] arrayOfArgs) {
        if(checkAuthentication(arrayOfArgs) == 1)
            return;
        QuizzToAdd quizzToAdd = new QuizzToAdd();
        String quizz = arrayOfArgs[3].split("'")[1];
        String verifyQuizz = quizzToAdd.verificationQuizz(quizz);
        if(verifyQuizz == null) {
            System.out.println("{ 'status' : 'error', 'message' : 'Quizz does not exist'}");
        } else {

            System.out.println("{ 'status' : 'ok', 'message' : '" + verifyQuizz + "' }");
        }
    }

    public void verifyGetAllQuizzesFormat(String[] arrayOfArgs) {
        if(checkAuthentication(arrayOfArgs) == 1)
            return;
        Quizz quizzObj = new Quizz();
        System.out.println(quizzObj.getAllQzz());
    }

    public void verifyGetQuizzDetailsFormat(String[] arrayOfArgs) {
        if(checkAuthentication(arrayOfArgs) == 1)
            return;
        String quizz = arrayOfArgs[3].split("'")[1];
        Quizz quizzObj = new Quizz();
        System.out.println(quizzObj.getQuizzDetails(quizz));
    }

    public void verifySubmitQuizzFormat(String[] arrayOfArgs) {
        if(checkAuthentication(arrayOfArgs) == 1)
            return;
        if(arrayOfArgs.length < 4 || arrayOfArgs[3].equals("-name")) {
            System.out.println("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
            return;
        }
        String quizzId = arrayOfArgs[3].split("'")[1];
        boolean quizzExistence = verifyQuizzById(quizzId);
        if(!quizzExistence) {
            System.out.println("{ 'status' : 'error', 'message' : 'No quiz was found'}");
            return;
        }
        Answers answerObj = new Answers();
        int descId = answerObj.verifyIfIdExists(arrayOfArgs);
        if(descId != -1){
            System.out.println("{ 'status' : 'error', 'message' : 'Answer ID for answer " + descId + " does not exists'}");
            return;
        }
        Quizz quizzObj = new Quizz();
        String username = arrayOfArgs[1].split("'")[1];
        if(quizzObj.checkIfAlreadyAnsweredQuiz(username, quizzId)) {
            System.out.println("{ 'status' : 'error', 'message' : 'You already submitted this quiz'}");
            return;
        }
        if(quizzObj.checkIfUserHasQuizz(username, quizzId)) {
            System.out.println("{ 'status' : 'error', 'message' : 'You cannot answer your own quiz'}");
            return;
        }
        quizzObj.modifyTrueAsCompleted(quizzId);
        QuizzToAdd quizzToAdd = new QuizzToAdd();
        quizzToAdd.calculateQuizPoints(arrayOfArgs);

    }


    public void verifyDeleteQuizzFormat(String[] arrayOfArgs) {
        if(checkAuthentication(arrayOfArgs) == 1)
            return;
        if(arrayOfArgs.length < 4 || arrayOfArgs[3].equals("-name")) {
            System.out.println("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
            return;
        }
        String quizz = arrayOfArgs[3].split("'")[1];
        boolean quizzExistence = verifyQuizzById(quizz);
        if(!quizzExistence) {
            System.out.println("{ 'status' : 'error', 'message' : 'No quiz was found'}");
            return;
        }
        Quizz quizzObj = new Quizz();
        quizzObj.deleteQuizzById(quizz);
        System.out.println("{ 'status' : 'ok', 'message' : 'Quizz deleted successfully'}");
    }

    public void verifyGetSolutionFormat(String[] arrayOfArgs) {
        if(checkAuthentication(arrayOfArgs) == 1)
            return;
        Quizz quizzObj = new Quizz();
        quizzObj.printSol(arrayOfArgs[1].split("'")[1]);
    }

    public static String getWord(String[] array, int index) {
        String s = array[index];
        String[] words = s.split("'");
        return words[1];
    }

    public static boolean verifyQuizzById(String id) {
        try {

            File file = new File("quizzes.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains(id)) {
                    String[] words = line.split(",");
                    if(words[2].equals(id)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
