package com.example.project;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Tema1{
	public static ArrayList<Users> users = new ArrayList<Users>();

	public static void main(final String[] args)
	{
		TestValidity test = new TestValidity();
		if(args == null) {
			System.out.print("Hello world!");
		} else if(args.length > 0) {
			if(args[0].equals("-create-user")) {
				test.verifyUserFormat(args);
			} else if(args[0].equals("-cleanup-all")) {
				cleanUp();
				users.clear();
				Answers.id = 1;
				Quizz.noQuizzes = 1;
				System.out.println("{ 'status' : 'ok', 'message' : 'Cleanup finished successfully' }");
			} else if(args[0].equals("-create-question")) {
				test.verifyQuestionFormat(args);
			} else if(args[0].equals("-get-question-id-by-text")) {
				test.verifyGetQuestionFormat(args);
			} else if(args[0].equals("-get-all-questions")) {
				test.verifyGetAllQuestionsFormat(args);
			} else if(args[0].equals("-create-quizz")) {
				test.verifyCreateQuizzFormat(args, users);
			} else if(args[0].equals("-get-quizz-by-name")) {
				test.verifyGetQuizzFormat(args);
			} else if(args[0].equals("-get-all-quizzes")) {
				test.verifyGetAllQuizzesFormat(args);
			} else if(args[0].equals("-get-quizz-details-by-id")) {
				test.verifyGetQuizzDetailsFormat(args);
			} else if(args[0].equals("-submit-quizz")) {
				test.verifySubmitQuizzFormat(args);
			} else if(args[0].equals("-delete-quizz-by-id")) {
				test.verifyDeleteQuizzFormat(args);
			} else if(args[0].equals("-get-my-solutions")) {
				test.verifyGetSolutionFormat(args);
			}
		}
	}

	public static void cleanUp()
	{
		try {
			new FileWriter("users.csv", false).close();
			new FileWriter("questions.csv", false).close();
			new FileWriter("quizzes.csv", false).close();
			new FileWriter("quizzesSol.csv", false).close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}