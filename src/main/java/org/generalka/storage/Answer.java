package org.generalka.storage;

import java.util.Objects;

public class Answer {
    private Long id;
    private String answer;
    private boolean isCorrect;
    private TestQuestion testQuestion;

    public Answer(Long id, String answer, boolean isCorrect, TestQuestion testQuestion) {
        this.id = id;
        this.answer = answer;
        this.isCorrect = isCorrect;
        this.testQuestion = testQuestion;
    }

    public Answer() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public TestQuestion getTestQuestion() {
        return testQuestion;
    }

    public void setTestQuestion(TestQuestion testQuestion) {
        this.testQuestion = testQuestion;
    }


}
