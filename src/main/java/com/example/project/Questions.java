package com.example.project;

import java.io.*;
import java.util.ArrayList;

import static com.example.project.TestValidity.*;

public class Questions {
    public String question;
    public String type;
    public int id_q;
    public ArrayList<Answers> answers;

    public Questions() {
    }

    public Questions(String question, String type, int id_q) {
        this.question = question;
        this.type = type;
        this.id_q = id_q;
        answers = new ArrayList<>();

    }

    public void writeQuestion(String[] args) {
        setAnswers(answers, args);
        addInfoInFile(args);
        System.out.println("{ 'status' : 'ok', 'message' : 'Question added successfully'}");
    }

    /* metoda adauga raspunsurile in lista de raspunsuri */
    public void setAnswers(ArrayList<Answers> answers, String[] args) {
        for (int i = 5; i < args.length - 1; i = i + 2) {
            if (args[i].startsWith("-answer")) {
                String s1 = args[i];
                String s2 = args[i + 1];
                String[] words1 = s1.split("'");
                String[] words2 = s2.split("'");
                String answer = words1[1].trim();
                String isCorrect = words2[1].trim();
                Answers a = new Answers(answer, isCorrect);
                answers.add(a);
            }
        }
    }

    /* metoda numara raspunsurile corecte */
    public String countAnswers(ArrayList<Answers> answers) {
        int count = 0;
        for (Answers a : answers) {
            if (a.isCorrect.equals("1")) {
                count++;
            }
        }
        return Integer.toString(count);

    }

    /* metoda adauga informatiile despre intrebari in fisierul specific */
    public void addInfoInFile(String[] args) {
        String user = getWord(args, 1);
        String toWrite = "-question-info" + "," + user + "," + this.question + "," + id_q + "," + type + "," + countAnswers(answers) + "," + answers.size();
        StringBuilder answString = new StringBuilder();
        for (Answers answ : answers) {
            answString.append(answ.id_answer).append("/").append(answ.getAnswer()).append("/").append(answ.isCorrect).append(",");
        }
        String answToWrite = answString.substring(0, answString.length() - 1);
        try (FileWriter fw = new FileWriter("questions.csv", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(toWrite);
            out.println(answToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* metoda cauta o intrebare dupa nume si intoarce id-ul acesteia daca intrebarea a fost gasita*/
    public String findQuestionInFile(String question) {
        try {
            File file = new File("questions.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.startsWith("-question-info")) {
                    String[] words = line.split(",");
                    if (words[2].equals(question)) {
                        return words[3];
                    }
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* metoda cauta intrebarea in functie de id-ul ei si intoarce un obiect de tip Questions, daca aceasta a fost
    gasita*/
    public Questions findQuestionById(String id) {
        try {
            File file = new File("questions.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                if (line.startsWith("-question-info")) {
                    if (words[3].equals(id)) {
                        String question = words[2];
                        String type = words[4];
                        int id_q = Integer.parseInt(words[3]);
                        return new Questions(question, type, id_q);
                    }
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* metoda intoarce un string ce contine detaliile tuturor intrebarilor din sistem */
    public String getAllQ() {
        try {
            File file = new File("questions.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder result = new StringBuilder("{ 'status' : 'ok', 'message' : '[{\"question_id\" : ");
            while ((line = br.readLine()) != null) {
                if (line.startsWith("-question-info")) {
                    String[] words = line.split(",");
                    result.append("\"").append(words[3]).append("\"").append(", \"question_name\" : ").append("\"").append(words[2]).append("\"}, {\"question_id\" : ");
                }

            }
            return result.substring(0, result.length() - 20) + "}]'}";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* metoda verifica daca intrebarea cu id-ul dat exista in fisier */
    public boolean verifyIfIdExists(String id) {
        try {
            File file = new File("questions.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("-question-info")) {
                    String[] words = line.split(",");
                    if (words[3].equals(id)) {
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
