package com.example.w0276812.quizzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Created by Chris on 2015-10-19.
 * Created and used by the Quiz Activity to maintain the questions,
 * lists of possible answers, etc.
 */
public class QuizLogic {
    // Constants and Member Variables
    private final int MAX_QUESTIONS = 10;


    // A map from term to definition, encompasing all from the file.
    private HashMap<String, String> items;

    // A generated array of terms (ie correct answers) in the order they
    // will be asked in the quiz.
    private String[] questions;

    // The index of the question currently showing.
    private int currentQuestion;

    // A list of the questions gotten right or wrong
    private boolean[] results;

    // The array of terms currently shown as possible answers (1 correct)
    private String[] options;

    // Constructor
    public QuizLogic(HashMap<String,String> itms) {
        // set the variables to new-quiz values
        items = itms;
        currentQuestion = 0;
        options = new String[4];
        int numQuestions = Math.min(items.size(), MAX_QUESTIONS);
        results = new boolean[numQuestions];
        Arrays.fill(results, false);

        // build list of questions.
        ArrayList<String> allTerms = new ArrayList<String>(items.keySet());
        Collections.shuffle(allTerms);
        questions = new String[numQuestions];
        for (int idx = 0; idx < numQuestions; idx++) {
            questions[idx] = allTerms.get(idx);
        }

        // reset list of answer terms
        resetOptions();
    }

    private void resetOptions() {
        if (currentQuestion != -1) {
            // make sure correct answer is included.
            ArrayList<String> tempOptions = new ArrayList(4);
            tempOptions.add(questions[currentQuestion]);

            ArrayList<String> allTerms = new ArrayList<String>(items.keySet());
            allTerms.remove(questions[currentQuestion]);
            Collections.shuffle(allTerms);

            for (int idx = 0; idx < 3; idx++) {
                tempOptions.add(allTerms.get(idx));
            }
            Collections.shuffle(tempOptions);
            options = tempOptions.toArray(new String[4]);
        }
    }

    // public methods
    public String getDefinition() {
        if (currentQuestion != -1) {
            return items.get(questions[currentQuestion]);
        } else {
            return "Done Quiz";
        }
    }

    public String getAnswer() {
        if (currentQuestion != -1) {
            return questions[currentQuestion];
        } else {
            return "done quiz";
        }
    }

    public String[] getOptions() {
        return options;
    }

    public int getCurrentNum() { return currentQuestion + 1; }

    public int getNumQuestions() { return questions.length; }

    public String[] getQuestions() { return questions; }

    public boolean[] getResults() { return results; }

    public int getCorrect() {
        int numCorrect = 0;
        for (int idx = 0; idx < results.length; idx++) {
            if (results[idx]) { numCorrect++; }
        }
        return numCorrect;
    }

    public boolean isDone() { return currentQuestion == -1; }

    public boolean isCorrect(String term) {
        if (currentQuestion >= questions.length || currentQuestion < 0) {
            return false;
        } else {
            return questions[currentQuestion].contentEquals(term);
        }
    }

    public void nextQuestion() {
        if (currentQuestion >= questions.length - 1 || currentQuestion < 0) {
            currentQuestion = -1;
        } else {
            currentQuestion++;
            resetOptions();
        }
    }

    public void answer(String term) {
        if (isCorrect(term)) {
            results[currentQuestion] = true;
        }
        nextQuestion();
    }
}
