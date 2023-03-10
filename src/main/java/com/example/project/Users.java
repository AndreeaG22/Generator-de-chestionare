package com.example.project;

import java.util.ArrayList;

public class Users {
    private String username;
    public int noQuestions = 0;
    public int noQuizzes = 0;
    private String password;



    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    /* metoda intoarce, in functie de numele utilizatorului, indexul acestuia in lista de utilizatori */
    public int findUserIndex(ArrayList<Users> users, String username) {
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }
    /* metoda seteaza numarul de intrebari la care a raspuns utilizatorul */
    public void setUserNoQuestions(ArrayList<Users> users, String username, int index) {
        for (Users user : users) {
            if (user.getUsername().equals(username)) {
                user.noQuestions = index;
            }
        }
    }

    /* metoda seteaza numarul de chestionare create de utilizator */
    public void setUserNoQzz(ArrayList<Users> users, String username, int index) {
        for (Users user : users) {
            if (user.getUsername().equals(username)) {
                user.noQuizzes = index;
            }
        }
    }


}
