package com.example.project;

import java.io.*;
import java.util.ArrayList;

public class Answers {
    static int id = 1;
    public int id_answer;
    private String answer;
    public String isCorrect;

    public Answers() {}
    public Answers(String answer, String isCorrect) {
        //this.answer = answer;
        this.setAnswer(answer);
        this.id_answer = id;
        id++;
        this.isCorrect = isCorrect;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getAnswer() {
        return answer;
    }
    /* metoda inntoarce o lista cu toate raspunsurile unei intrebari */
    public ArrayList<Answers> getAnswersByQuestionName(String name) {
        try {
            File file = new File("questions.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            ArrayList<Answers> answers = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String answers_line = br.readLine();
                String[] words = line.split(",");
                if (words[2].equals(name)) {
                    String[] answers_words = answers_line.split(",");
                    for (String answers_word : answers_words) {
                        String[] answersInfo = answers_word.split("/");
                        Answers a = new Answers(answersInfo[1], answersInfo[2]);
                        a.id_answer = Integer.parseInt(answersInfo[0]);
                        answers.add(a);
                    }
                }
            }
            return answers;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    /* metoda introduce toate raspunsurile unei intrebari intr-o lista si le returneaza doar pe cele care sunt corecte */
    public String getCorrectAnswerInfo(String name) {
        try {
            StringBuilder info = new StringBuilder();
            File file = new File("questions.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            ArrayList<Answers> answers = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String answers_line = br.readLine();
                String[] words = line.split(",");
                if (words[2].equals(name)) {
                    String type = line.split(",")[4];
                    info.append(type).append(",");
                    String noCorrectAnswers = line.split(",")[5];
                    info.append(noCorrectAnswers).append(",");
                    String noAnswers = line.split(",")[6];
                    info.append(noAnswers).append(",");
                    info.append(name).append(",");
                    info.append("/");
                    String[] answers_words = answers_line.split(",");
                    for (String answers_word : answers_words) {
                        String[] answersInfo = answers_word.split("/");
                        Answers a = new Answers(answersInfo[1], answersInfo[2]);
                        a.id_answer = Integer.parseInt(answersInfo[0]);
                        answers.add(a);
                    }
                    break;
                }
            }
            for(Answers a: answers) {
                if(a.isCorrect.equals("1")) {
                    info.append(a.id_answer).append("-");
                }
            }
            return info.substring(0, info.length() - 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    /* metoda intoarce un string cu toate id-urile raspunsurilor unei intrebari */
    public String getAnswersId(String name) {
        try {
            StringBuilder s = new StringBuilder();
            File file = new File("questions.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            ArrayList<Answers> answers = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String answers_line = br.readLine();
                String[] words = line.split(",");
                if (words[2].equals(name)) {
                    String[] answers_words = answers_line.split(",");
                    for (String answers_word : answers_words) {
                        String[] answersInfo = answers_word.split("/");
                        Answers a = new Answers(answersInfo[1], answersInfo[2]);
                        a.id_answer = Integer.parseInt(answersInfo[0]);
                        answers.add(a);
                    }
                    break;
                }
            }
            for(Answers a: answers) {
                s.append(a.id_answer).append("-");
            }
            return s.substring(0, s.length() - 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    /* metoda verifica daca fiecare raspuns dat de utilizator are un id asociat */
    public int verifyIfIdExists(String[] arrayOfArgs) {
        int desc;
        for(int i = 4; i < arrayOfArgs.length; i = i + 2) {
            if(!arrayOfArgs[i].startsWith("-answer-id")) {
                desc = i - 1;
                return desc;
            }
        }
        return -1;
    }
}
