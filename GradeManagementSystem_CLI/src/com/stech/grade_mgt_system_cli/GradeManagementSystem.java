package com.stech.grade_mgt_system_cli;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class GradeManagementSystem {
    static Map<String, Integer> studentScores = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while(running) {

            try {

                System.out.println("--- Student Grade Management System ---");
                System.out.println("1.) Add Student");
                System.out.println("2.) View Grade");
                System.out.println("3.) Save to file");
                System.out.println("4.) Exit");
                System.out.println("Choose an option: ");

                int choice = scanner.nextInt();

                switch(choice) {
                    case 1:
                        addStudent(scanner);
                        break;

                    case 2:
                        viewGrade(scanner);
                        break;

                    case 3:
                        saveToFile();
                        break;

                    case 4:
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid option!");

                }

            } catch (InputMismatchException e) {
                System.out.println(" Please enter valid input (numbers only).");
                scanner.nextLine(); //Clear the buffer
            } finally {
                System.out.println("Operation complete. \n");
            }

        }

    }


    //Method to be used when adding a student
    public static void addStudent(Scanner scanner) {
        try {
           System.out.println("Enter student name: ");
           String name = scanner.next();
           System.out.println("Enter score (0 - 100): ");
           int score = scanner.nextInt();

           if(score < 0 || score > 100) {
               throw  new InvalidScoreException("Score must be between 0 and 100");
           }

            studentScores.put(name, score);
            System.out.println("Student added successfully");

        } catch (InvalidScoreException e) {
            throw new RuntimeException(e);
        }

    }

    //Method for viewing student grade
    public static void viewGrade(Scanner scanner) {

        try {
        System.out.println("Enter student name to view grade: ");
        String name = scanner.next();

        if(!studentScores.containsKey(name)) {
            throw new StudentNotFoundException("Student not found");
        }

        int score = studentScores.get(name);

        //Invoke the method responsible for calculating grades
        String grade = calculateGrade(score);

        System.out.println("Score for: " + name + " : " + grade);

        } catch (StudentNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    //Grade calculation
    public static String calculateGrade(int score) {

        if(score >= 80) return "A";
        else if(score >= 70) return "B";
        else if(score >= 60) return "C";
        else if(score >= 50) return "D";
        else return "F";

    }

    //Save data to file and also simulate suppressed exceptions
    public static void saveToFile() {

        PrintWriter writer = null;

        try {

            writer = new PrintWriter(new FileWriter("students.txt"));

            for(String name : studentScores.keySet()) {
                writer.println(name + " : " + studentScores.get(name));
            }

            System.out.println("Data saved to students.txt");

        } catch (IOException e) {
            System.out.println("Error while saving the file: " + e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception ex) {
                IOException suppressed = new IOException("Error while closing writer.");
                suppressed.addSuppressed(ex);System.out.println("Suppressed: " + suppressed.getSuppressed()[0]);
            }

        }



    }

}
