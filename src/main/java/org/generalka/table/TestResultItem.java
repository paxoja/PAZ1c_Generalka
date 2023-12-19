package org.generalka.table;

public class TestResultItem {
    private int questionNumber;
    private String userAnswer;
    private String correctAnswer;

    public TestResultItem(int questionNumber, String userAnswer, String correctAnswer) {
        this.questionNumber = questionNumber;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}


