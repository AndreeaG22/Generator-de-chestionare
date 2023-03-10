package com.example.project;

import java.io.*;
import java.util.ArrayList;

import static com.example.project.TestValidity.getWord;

public class Quizz extends Questions{
    public static int noQuizzes = 1;
    public String name;
    public int id_qzz;
    public ArrayList<String> IDs;

    public Quizz(String name, int id_qzz) {
        this.name = name;
        this.id_qzz = id_qzz;
        IDs = new ArrayList<>();

    }

    public Quizz() {
    }

    public void implemetQuizz(String[] args) {
        getQuestionsId(args);
        addQzzInFile(args);
        System.out.println("{ 'status' : 'ok', 'message' : 'Quizz added succesfully'}");
    }

    /* metoda adauga, in lista de id-uri, id-urile intrebarilor pe care utilizatorul doreste sa le adauge in quizz */
    public void getQuestionsId(String[] args) {
        for(int i = 4; i < args.length; i++) {
            if(args[i].startsWith("-question-")) {
                String s = args[i];
                String[] words = s.split("'");
                String id = words[1].trim();
                IDs.add(id);
            }
        }
    }

    /* metoda adauga in fisierul in care sunt retinute quiz-urile create, datele despre quiz-ul creat */
    public void addQzzInFile(String[] args) {
        String user = getWord(args, 1);
        String toWrite = user + "," + this.name + "," + this.id_qzz;
        StringBuilder idString = new StringBuilder();
        for(String id: IDs) {
            idString.append(id).append("/");
        }
        String idToWrite = idString.substring(0, idString.length() - 1);
        try(FileWriter fw = new FileWriter("quizzes.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(toWrite + "," + idToWrite + "," + "False");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* metoda returneaza un string cu detaliile esentiale ale tuturor quiz-urilor unui utilizator, care va fi afisat
    atunci cand utilizatorul doreste sa afiseze toate quiz-urile create */
    public String getAllQzz() {
        try {
            File file = new File("quizzes.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder result = new StringBuilder("{ 'status' : 'ok', 'message' : '[{\"quizz_id\" : ");
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                result.append("\"").append(words[2]).append("\"").append(", \"quizz_name\" : ").append("\"").append(words[1]).append("\", ").append("\"is_completed\" : \"").append(words[4]).append("\"}, {\"quizz_id\" : ");
            }
            return result.substring(0, result.length() - 16) + "]'}";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* metoda returneaza un string cu detaliile explicite ale unui quiz, care va fi afisat atunci cand utilizatorul
    doreste sa afiseze un quiz in functie de id-ul acestuia */
    public String getQuizzDetails(String id) {
        try {
            File file = new File("quizzes.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int index = 1;
            StringBuilder result = new StringBuilder("{ 'status' : 'ok', 'message' : '[{\"question-name\":");
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                if(words[2].equals(id)) {
                    String[] ids = words[3].split("/");
                    for(String idd: ids) {
                        Questions q = new Questions();
                        q = q.findQuestionById(idd);
                        ArrayList<Answers> answers;
                        Answers getAnwers = new Answers();
                        answers = getAnwers.getAnswersByQuestionName(q.question);
                        result.append("\"").append(q.question).append("\"").append(", \"question_index\":").append("\"").append(idd).append("\", ").append("\"question_type\":").append("\"").append(q.type).append("\", ").append("\"answers\":\"[");
                        for(Answers a: answers) {
                            result.append("{\"answer_name\":" + "\"").append(a.getAnswer()).append("\", ").append("\"answer_id\":").append("\"").append(index).append("\"}, ");
                            index++;
                        }
                        result = new StringBuilder(result.substring(0, result.length() - 2));
                        result.append("]\"}, {\"question-name\":");
                        //index++;
                    }
                    result = new StringBuilder(result.substring(0, result.length() - 19));
                    result.append("]'}");
                    return result.toString();
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* metoda pune intre-un array de stringuri numele intrebarilor dintr-un quiz, in functie de id-ul acestuia */
    public String[] getQuestionsByQuizzId(String id) {
        String[] questions = new String[5];
        try {
            File file = new File("quizzes.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                if(words[2].equals(id)) {
                    String[] ids = words[3].split("/");
                    for(String idd: ids) {
                        Questions q = new Questions();
                        q = q.findQuestionById(idd);
                        result.append(q.question).append("/");
                    }
                    result = new StringBuilder(result.substring(0, result.length() - 1));
                    questions = result.toString().split("/");
                    return questions;
                }
            }
            return questions;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //* metoda sterge un quiz din fisierul in care sunt retinute quiz-urile create, in functie de id-ul acestuia */
    public void deleteQuizzById(String id) {
        try {
            File file = new File("quizzes.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                if(!words[2].equals(id)) {
                    result.append(line).append("\n");
                }
            }
            try(FileWriter fw = new FileWriter("quizzes.csv", false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(result);
                deleteSolutionOfQuizz(id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* metoda adauga raspunsurile unui utilizator in fisier */
    public void writeInFile(String line) {
        try(FileWriter fw = new FileWriter("quizzesSol.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* metoda afiseaza toate solutiile submise de un utilizator, in functie de username-ul acestuia */
    public void printSol(String username) {
        try {
            File file = new File("quizzesSol.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder result = new StringBuilder("{ 'status' : 'ok', 'message' : '[{\"quiz-id\" : ");
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                if(words[0].equals(username)) {
                    result.append("\"").append(words[1]).append("\"").append(", \"quiz-name\" : ").append("\"").append(words[2]).append("\", ").append("\"score\" : ").append("\"").append(words[3]).append("\", \"index_in_list\" : ").append("\"").append(words[4]).append("\"}, {\"quiz-id\" : ");
                }
            }
            System.out.println(result.substring(0, result.length() - 16) + "}]'}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    /* functia intoarce numele quiz-ului in functie de id-ul acestuia */
    public String getQuizzName(String id) {
        try {
            File file = new File("quizzes.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String result = "";
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                if(words[2].equals(id)) {
                    result = words[1];
                    return result;
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* metoda verifica daca utilizatorul a mai submis raspunsuri pt quiz-ul curent*/
    public boolean checkIfAlreadyAnsweredQuiz(String username, String quizId) {
        try {
            File file = new File("quizzesSol.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                if(words[0].equals(username) && words[1].equals(quizId)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* metoda verifica daca utilizatorul incearca sa raspunda la propriul quiz */
    public boolean checkIfUserHasQuizz(String username, String quizId) {
        try {
            File file = new File("quizzes.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                if(words[0].equals(username) && words[2].equals(quizId)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* metoda sterge solutiile unui quiz, in functie de id-ul acestuia */
    public void deleteSolutionOfQuizz(String id) {
        try {
            File file = new File("quizzesSol.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                if(!words[1].equals(id)) {
                    result.append(line).append("\n");
                }
            }
            try(FileWriter fw = new FileWriter("quizzesSol.csv", false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* daca un utilizator a raspuns la un quiz, modificam caracteristica isCompleted in "True" */
    void modifyTrueAsCompleted(String id){
        String quiz = "";
        try {
            File file = new File("quizzes.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                if(!words[2].equals(id)) {
                    result.append(line).append("\n");
                } else {
                    quiz = line;
                }
            }
            String[] words = quiz.split(",");
            words[4] = "True";
            String newQuizz = words[0] + "," + words[1] + "," + words[2] + "," + words[3] + "," + words[4];
            result.append(newQuizz).append("\n");
            try(FileWriter fw = new FileWriter("quizzes.csv", false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(result);
                deleteSolutionOfQuizz(id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
