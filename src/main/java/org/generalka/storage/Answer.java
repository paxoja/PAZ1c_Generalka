package org.generalka.storage;

import java.util.Objects;

public class Answer {

    private int id;

    private String answer;

    private Boolean isCorrect;

    private TestQuestion testQuestion;

    public Answer(){

    }

    public Answer(int id, String answer, boolean isCorrect, TestQuestion testQuestion) {
        this.id = id;
        this.answer = answer;
        this.isCorrect = isCorrect;
        this.testQuestion = testQuestion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public TestQuestion getTestQuestion() {
        return testQuestion;
    }

    public void setTestQuestion(TestQuestion testQuestion) {
        this.testQuestion = testQuestion;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", answer='" + answer + '\'' +
                ", isCorrect=" + isCorrect +
                ", testQuestion=" + testQuestion +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Answer other = (Answer) obj;
        return Objects.equals(id, other.id);
    }
}
