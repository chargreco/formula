package com.rechargoyo.formula;

public class Card {
    private String text;
    private boolean isQuestion;
    private boolean revealed;

    public Card(String text, boolean isQuestion) {
        this.text = text;
        this.isQuestion = isQuestion;
        this.revealed = false;
    }

    public String getText() {
        return text;
    }

    public boolean isQuestion() {
        return isQuestion;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
}
