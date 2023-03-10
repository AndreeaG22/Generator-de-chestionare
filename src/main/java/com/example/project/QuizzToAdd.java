package com.example.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.example.project.Quizz.*;
import static com.example.project.TestValidity.verifyQuizzById;

public class QuizzToAdd {
    public QuizzToAdd() {
    }

    /* metoda verifica daca datele introduse de utilizator la adaugarea unui quiz sunt corecte si valide */
    public int checkQuizzFormat(String[] arrayOfArgs) {
        String quizz = arrayOfArgs[3].split("'")[1];
        String verifyQuizz = verificationQuizz(quizz);
        Questions questionObj = new Questions();
        if(verifyQuizz != null) {
            System.out.println("{ 'status' : 'error', 'message' : 'Quizz name already exists'}");
            return 1;
        }
        for(int i = 4; i < arrayOfArgs.length; i++) {
            if(arrayOfArgs[i].startsWith("-question-")) {
                String id = arrayOfArgs[i].split("'")[1];
                if(!questionObj.verifyIfIdExists(id)) {
                    System.out.println("{ 'status' : 'error', 'message' : 'Question ID for question " + id + " does not exist'}");
                    return 1;
                }
            }
        }
        if(noOfQuestions(arrayOfArgs) > 10) {
            System.out.println("{ 'status' : 'error', 'message' : 'Quizz has more than 10 questions'}");
            return 1;
        }
        return 0;
    }
    /* metoda verifica daca exista un quiz, cu numele dat ca parametru, in fisierul in care sunt retinute quiz-urile create
    * daca este gasit, metoda intoarce ca rezultat id-ul quiz-ului, altfel intoarce null*/
    public String verificationQuizz(String qzzToAdd) {
        try {

            File file = new File("quizzes.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains(qzzToAdd)) {
                    String[] words = line.split(",");
                    if(words[1].equals(qzzToAdd)) {
                        return words[2];
                    }
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* motoda numara cate intrebari doreste utilizatorul sa introduca intr-un quiz*/
    public int noOfQuestions(String[] arrayOfArgs) {
        int noQuestions = 0;
        for (String arrayOfArg : arrayOfArgs) {
            if (arrayOfArg.startsWith("-question-")) {
                noQuestions++;
            }
        }
        return noQuestions;
    }

    /* metoda calculeaza punctajul obtinut de utilizator pe baza raspunsurilor date la intrebari*/
    public void calculateQuizPoints(String[] arrayOfArgs) {
        String quizzId = arrayOfArgs[3].split("'")[1];
        boolean quizzExistence = verifyQuizzById(quizzId);
        if(!quizzExistence) {
            System.out.println("{ 'status' : 'error', 'message' : 'No quiz was found'}");
            return;
        }
        Quizz quizzObj = new Quizz();
        String toWriteInFile = "";
        String quizzName = quizzObj.getQuizzName(quizzId);
        String username = arrayOfArgs[1].split("'")[1];
        toWriteInFile += username + "," + quizzId + "," + quizzName + ",";
        String IDs = String.valueOf(getAllIds(arrayOfArgs));
        String[] questions = quizzObj.getQuestionsByQuizzId(quizzId);
        float noQuestions = questions.length;
        float totalScore = 0f;
        float percentage = 100f/ noQuestions;
        for (String q : questions) {
            float noCorrectAnswers = 0;
            float noWrongAnswers = 0;
            float noQuizzCorrectAnswers = 0;
            float noQuizzWrongAnswers = 0;
            Answers answersInfo = new Answers();
            String info = answersInfo.getCorrectAnswerInfo(q);
            String[] information = info.split(",");
            information[4] = information[4].substring(1);
            noQuizzCorrectAnswers += Float.parseFloat(information[1]);
            noQuizzWrongAnswers += Float.parseFloat(information[2]) - Float.parseFloat(information[1]);
            if(information[0].equals("single")) {
                if(IDs.contains(information[4])) {
                    noCorrectAnswers++;
                } else {
                    String allAnswers = answersInfo.getAnswersId(information[3]);
                    String[] answers = allAnswers.split("-");
                    boolean contains = false;
                    for (String answer : answers) {
                        if (IDs.contains(answer)) {
                            contains = true;
                            break;
                        }
                    }
                    if(contains) {
                        noWrongAnswers++;
                    }
                }
            } else if(information[0].equals("multiple")) {
                String[] correctAnswersArray = information[4].split("-");
                for (String s : correctAnswersArray) {
                    if (IDs.contains(s)) {
                        noCorrectAnswers++;
                    } else {
                        String allAnswers = answersInfo.getAnswersId(information[3]);
                        String[] answers = allAnswers.split("-");
                        boolean contains = false;
                        for (String answer : answers) {
                            if (IDs.contains(answer)) {
                                contains = true;
                                break;
                            }
                        }
                        if (contains) {
                            noWrongAnswers++;
                        }
                    }
                }

            }
            float correctAnswersPercentage = (noCorrectAnswers / noQuizzCorrectAnswers);
            float wrongAnswersPercentage = (noWrongAnswers / noQuizzWrongAnswers);
            wrongAnswersPercentage = -1.0f * wrongAnswersPercentage;
            if(correctAnswersPercentage != 0) {
                totalScore += correctAnswersPercentage * percentage;
            }
            if(wrongAnswersPercentage != 0) {
                totalScore += wrongAnswersPercentage * percentage;
            }
        }
        int totalScoreQuizz = Math.round(totalScore);
        if(totalScoreQuizz < 0) {
            totalScoreQuizz = 0;
        }
        System.out.println("{ 'status' : 'ok', 'message' : '" + totalScoreQuizz + " points'}");
        toWriteInFile += totalScoreQuizz + ",";
        toWriteInFile += noQuizzes;
        noQuizzes++;
        int quizzIntId = Integer.parseInt(quizzId);
        Quizz quizz = new Quizz(quizzName,quizzIntId);
        quizz.writeInFile(toWriteInFile);
    }

    /* metoda returneaza un string cu toate id-urile raspunsurilor date de utilizator */
    public StringBuilder getAllIds(String[] arrayOfArgs) {
        StringBuilder ids = new StringBuilder();
        for (String arrayOfArg : arrayOfArgs) {
            if (arrayOfArg.startsWith("-answer-id-")) {
                ids.append(arrayOfArg.split("'")[1]);
                ids.append("-");
            }
        }
        return new StringBuilder(ids.substring(0, ids.length() - 1));
    }
}
