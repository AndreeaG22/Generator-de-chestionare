package com.example.project;

import java.io.*;

import static com.example.project.Tema1.users;
import static com.example.project.TestValidity.getWord;

public class UserToAdd extends Users {
    public UserToAdd() {
    }

    /* metoda verifica daca parametrul -p sau -u este furnizat de catre utilizator, daca username-ul exista sau
    daca username-ul si parola sunt corecte; in caz contrar se afiseaza mesaje de eroare
     */
    public int checkAuthentication(String[] arrayOfArgs) {
        if(arrayOfArgs.length < 2 || arrayOfArgs[1].equals("-u")) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return 1;
        }
        if(arrayOfArgs.length < 3 || arrayOfArgs[2].equals("-q")) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return 1;
        }
        String username = getWord(arrayOfArgs, 1);
        String password = getWord(arrayOfArgs, 2);

        boolean verifyUser = verificationUser(username + "," + password);

        if (!verifyUser) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return 1;
        }
        return 0;
    }

    /* metoda verifica daca parametrul -u sau -p este furnizat de catre utilizator */
    public int checkCredentials(String[] arrayOfArgs) {
        if (arrayOfArgs.length < 2 || arrayOfArgs[1].equals("-u")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Please provide username'}");
            return 1;
        } else if (arrayOfArgs.length < 3 || arrayOfArgs[2].equals("-p")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Please provide password'}");
            return 1;
        }
        return 0;
    }

    /* metoda verifica daca utilizatorul exista deja  in sistem; Se citeste linie cu linie fisierul in care sunt stocate
    * numele userilor si parolele si se verifica daca username-ul introdus de catre utilizator exista deja in fisier */
    public boolean verificationUser(String userToAdd) {
        try {
            File file = new File("users.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(userToAdd)) return true;
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* metoda adauga un nou utilizator in sistem; adauga o linie, in fisierul in care retinem utilizatorii si parolele,
    * care contine: user,password */
    public void writeUserInFile(String username, String password) {
        try(FileWriter fw = new FileWriter("users.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            String userToAdd = username + "," + password;
            Users user = new Users();
            user.setUsername(username);
            users.add(user);
            out.println(userToAdd);
            System.out.println("{ 'status' : 'ok', 'message' : 'User created successfully'}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
