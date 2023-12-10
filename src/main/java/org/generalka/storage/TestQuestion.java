package org.generalka.storage;

import java.util.List;
import java.util.Objects;

public class TestQuestion {

    private Long id;

    private String question;

    private Test test;

    private List<Answer> answers;

    public TestQuestion(Long id, String question, Test test, List<Answer> answers) {
        this.id = id;
        this.question = question;
        this.test = test;
        this.answers = answers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "TestQuestion{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", test=" + test +
                ", answers=" + answers +
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
        TestQuestion other = (TestQuestion) obj;
        return Objects.equals(id, other.id);
    }
}
