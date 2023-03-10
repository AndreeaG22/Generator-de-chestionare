package com.example.project;

import java.io.*;

import static com.example.project.Tema1.users;
import static com.example.project.TestValidity.getWord;

public class QuestionToAdd extends Questions {
    public QuestionToAdd() {
    }

    /* metoda verifica daca conditiile impuse de cerinta sunt respectate si daca datele introduse de utilizator
    * au format-ul corect; daca da, atunci intrebarea introdusa de utilizator este scrisa in fisier*/
    public void verifyFormat(String[] arrayOfArgs) {
        if(arrayOfArgs.length < 4 || arrayOfArgs[3].equals("-test") || arrayOfArgs[3].startsWith("-type")) {
            System.out.println("{ 'status' : 'error', 'message' : 'No question text provided'}");
            return;
        }
        String questionText = getWord(arrayOfArgs, 3);
        boolean questionExists = verificationQuestion(questionText);
        if(questionExists) {
            System.out.println("{ 'status' : 'error', 'message' : 'Question already exists'}");
            return;
        }
        if(arrayOfArgs.length < 6) {
            System.out.println("{ 'status' : 'error', 'message' : 'No answer provided'}");
            return;
        }
        int noAnswers = countAnswers(arrayOfArgs);
        if(noAnswers == 0) {
            System.out.println("{ 'status' : 'error', 'message' : 'No answer provided'}");
            return;
        }
        if(verifyIfDescriptionExists(arrayOfArgs) == 0 && noAnswers == 1 && verifyIfFlagExists(arrayOfArgs) == 0) {
            System.out.println("{ 'status' : 'error', 'message' : 'Only one answer provided'}");
            return;
        }
        if(noAnswers > 5) {
            System.out.println("{ 'status' : 'error', 'message' : 'More than 5 answers were submitted'}");
            return;
        }
        int countCorrectAnswers = countCorrectAnswers(arrayOfArgs);
        String type = getWord(arrayOfArgs, 4);
        if(type.equals("single") && countCorrectAnswers > 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'Single correct answer question has more than one correct answer'}");
            return;
        }
        boolean sameAnswer = verifyIfSameAnswer(arrayOfArgs);
        if(sameAnswer) {
            System.out.println("{ 'status' : 'error', 'message' : 'Same answer provided more than once'}");
            return;
        }
        int descriptionExists = verifyIfDescriptionExists(arrayOfArgs);
        int flagExists = verifyIfFlagExists(arrayOfArgs);
        int noFlags = countFlags(arrayOfArgs);
        if(descriptionExists != 0 && noAnswers < noFlags) {
            System.out.println("{ 'status' : 'error', 'message' : 'Answer " + descriptionExists + " has no answer description'}");
            return;
        }else if(flagExists != 0) {
            System.out.println("{ 'status' : 'error', 'message' : 'Answer " + flagExists + " has no answer correct flag'}");
            return;
        }
        Users userObj = new Users();
        String username = getWord(arrayOfArgs, 1);
        int id_question;
        int userIndex = userObj.findUserIndex(users, username);
        if(userIndex == -1) {
            id_question = 1;
            Answers.id = 1;
        }else id_question = users.get(userIndex).noQuestions + 1;
        userObj.setUserNoQuestions(users, username, id_question);
        String q = getWord(arrayOfArgs, 3);
        String q_type = getWord(arrayOfArgs, 4);
        Questions question = new Questions(q, q_type, id_question);
        question.writeQuestion(arrayOfArgs);
    }
    /* metoda verifica daca in linia de comanda, utilizatorul a introdus descriere pentru fiecare raspuns */
    public int verifyIfDescriptionExists(String[] arrayOfArgs) {
        int desc = 1;
        for(int i = 5; i < arrayOfArgs.length; i = i + 2) {
            String description = "-answer-" + desc;
            String s = arrayOfArgs[i];
            String[] words = s.split("'");
            String answer = words[0].trim();
            if(!answer.equals(description)) {
                return desc;
            }
            desc++;
        }
        return 0;
    }
    /* metoda verifica daca in linia de comanda, utilizatorul a introdus, pentru fiecare raspuns, parametrul de adevar */
    public int verifyIfFlagExists(String[] arrayOfArgs) {
        int flag_index= 1;
        for(int i = 6; i < arrayOfArgs.length; i = i + 2) {
            String flag = "-answer-" + flag_index + "-is-correct";
            String s = arrayOfArgs[i];
            String[] words = s.split("'");
            String answer = words[0].trim();
            if(!answer.equals(flag)) {
                return flag_index;
            }
            flag_index++;
        }
        return 0;
    }

    /* metoda verifica daca exista 2 raspunsuri identice introduse de utilizator pentru o intrebare */
    public boolean verifyIfSameAnswer(String[] arrayOfArgs) {
        String[] answers = new String[5];
        int n = 0;
        for (String arrayOfArg : arrayOfArgs) {
            if (arrayOfArg.startsWith("-answer")) {
                String[] words = arrayOfArg.split("'");
                String s2 = words[0];
                String[] words2 = s2.split("-");
                if (words2.length < 4) {
                    answers[n] = words[1];
                    n++;
                }
            }
        }
        for(int j = 0; j < n; j++) {
            for(int k = j + 1; k < n; k++) {
                if(answers[j].equals(answers[k])) return true;
            }
        }
        return false;
    }
    /* metoda numara raspunsuri corecte are o intrebare */
    public int countCorrectAnswers(String[] arrayOfArgs) {
        int count = 0;
        for (String arrayOfArg : arrayOfArgs) {
            if (arrayOfArg.startsWith("-answer")) {
                String[] words = arrayOfArg.split("'");
                String s2 = words[0];
                String[] words2 = s2.split("-");
                if (words2.length > 3) {
                    String string = words2[4].trim();
                    if (string.equals("correct") && words[1].equals("1")) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    /* metoda numara cate raspunsuri a inrodus utilizatorul de la tastatura */
    public int countAnswers(String[] arrayOfArgs) {
        int noAnswers = 0;
        for(int i = 4; i < arrayOfArgs.length; i++) {
            if(arrayOfArgs[i].startsWith("-answer")) {
                String a = "correct";
                String s = arrayOfArgs[i];
                String[] words = s.split("'");
                String answer = words[0].trim();
                if(!answer.endsWith(a)) {
                    noAnswers++;
                }
            }
        }
        return noAnswers;
    }
    /* metoda numara cate intrebari au parametru de adevar */
    public int countFlags(String[] arrayOfArgs) {
        int noFlags = 0;
        for(int i = 5; i < arrayOfArgs.length; i++) {
            String flag = "correct";
            String s = arrayOfArgs[i];
            String[] words = s.split("'");
            String answer = words[0].trim();
            if(answer.endsWith(flag)) {
                noFlags++;
            }
        }
        return noFlags;
    }
    /* metoda verifica daca intrebarea exista deja in fisier */
    public boolean verificationQuestion(String questionToAdd) {
        try {

            File file = new File("questions.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("-question-info")) {
                    if(line.contains(questionToAdd)) {
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
