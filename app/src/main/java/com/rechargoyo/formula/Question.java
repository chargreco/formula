package com.rechargoyo.formula;

import android.widget.Toast;

import java.util.ArrayList;

public class Question {
    private String questionText;
    private ArrayList<String> answers;
    private String correctAnswer;
    private int selectedOptionIndex = -1; // Inicialmente ninguna opción seleccionada

    public Question(String questionText, ArrayList<String> answers, String correctAnswer) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setSelectedOptionIndex(int selectedOptionIndex) {
        this.selectedOptionIndex = selectedOptionIndex;
    }

    public boolean isAnswerCorrect() {
        if (selectedOptionIndex >= 0 && selectedOptionIndex < answers.size()) {
            String selectedAnswer = answers.get(selectedOptionIndex);
            return selectedAnswer.equals(correctAnswer);
        }
        return false;
    }

}
