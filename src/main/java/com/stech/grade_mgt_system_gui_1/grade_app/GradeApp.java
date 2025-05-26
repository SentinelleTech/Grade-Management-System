package com.stech.grade_mgt_system_gui_1.grade_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class GradeApp extends Application {

    private final Map<String, Integer> studentData = new HashMap<>();
    private final TextArea outputArea = new TextArea();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Student Grade Management System");

        //UI Components
        TextField nameField = new TextField();
        nameField.setPromptText("Enter student Name");

        TextField scoreField = new TextField();
        scoreField.setPromptText("Enter score (0 - 100)");

        Button addButton = new Button("Add Student");
        Button gradeButton = new Button("View Student");
        Button savButton = new Button("Save to File");

        outputArea.setEditable(false);
        outputArea.setPrefHeight(200);

        VBox root = new VBox(10,
                new Label("Student Name: "), nameField,
                new Label("Score: "), scoreField,
                new HBox(10, addButton, gradeButton, savButton),
                new Label("Output: "), outputArea
                );

        root.setPadding(new Insets(15));

        //Event handlers
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String scoreStr = scoreField.getText();

            try {
                int score = Integer.parseInt(scoreStr);

                if(score < 0 || score > 100) {
                        throw new InvalidScoreException("Score must be between 0 and 100");
                }

                //store in the map
                studentData.put(name, score);

                //Invoke the output method which appends text to the output area
                output("Added Student: " + name);
            } catch (InvalidScoreException ex) {
                output("Error: " + ex.getMessage());
            } catch (NumberFormatException ex)  {
                output("Please enter a valid number for the score");
            } finally {
                nameField.clear();
                scoreField.clear();
            }
        });


        //Grade the student based on the score they got
        gradeButton.setOnAction(e -> {
            String name = nameField.getText();

            try {

                if (!studentData.containsKey(name)) {
                        throw new StudentNotFoundException("Student Not Found!");
                }

                //read the score using student name as the key
                int score = studentData.get(name);
                output("Grade for " + name + " : " + calculateGrade(score));

            } catch (StudentNotFoundException ex) {
                output("Error: " + ex.getMessage());
            } finally {
                nameField.clear();
            }
        });


        //Save all the data collected so far to a file
        savButton.setOnAction(e -> {

            PrintWriter writer = null;

            try {

                writer = new PrintWriter(new FileWriter("students_fx.txt"));

                //Loop through the map
                for (String student : studentData.keySet()) {
                    writer.println(student + " : " + studentData.get(student));

                }

                output("Saved the data to students_fx.txt");

            } catch (IOException ex) {
                output("Error while saving the file: " + ex.getMessage());
            } finally {

                try {

                    if(writer != null) {
                        writer.close();
                    }

                } catch (Exception ex) {
                    IOException suppressed = new IOException("Error while closing the writer.");
                    suppressed.addSuppressed(ex);
                    output("Suppressed exception thrown: " + suppressed.getSuppressed()[0]);
                }

            }


        });




        //Setup the Scene
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();

    }

    private void output(String msg) {
        outputArea.appendText(msg + "\n");
    }

    private String calculateGrade(int score) {

        if(score >= 80) return "A";
        else if(score >= 70) return "B";
        else if(score >= 60) return "C";
        else if(score >= 50) return "D";
        else return "F";

    }

    public static void main(String[] args) {
        launch();
    }
}