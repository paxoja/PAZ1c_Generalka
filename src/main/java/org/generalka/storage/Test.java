package org.generalka.storage;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Test {

    private int id;

    private String topic;

    private int isWholeSemester;

    private Date date;

    private Subject subject;

    private User user;

    private List<TestQuestion> testQuestions;

    public Test(int id, String topic, int isWholeSemester, Date date, Subject subject, User user, List<TestQuestion> testQuestions) {
        this.id = id;
        this.topic = topic;
        this.isWholeSemester = isWholeSemester;
        this.date = date;
        this.subject = subject;
        this.user = user;
        this.testQuestions = testQuestions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getIsWholeSemester() {
        return isWholeSemester;
    }

    public void setIsWholeSemester(int isWholeSemester) {
        this.isWholeSemester = isWholeSemester;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TestQuestion> getTestQuestions() {
        return testQuestions;
    }

    public void setTestQuestions(List<TestQuestion> testQuestions) {
        this.testQuestions = testQuestions;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", isWholeSemester=" + isWholeSemester +
                ", date=" + date +
                ", subject=" + subject +
                ", user=" + user +
                ", testQuestions=" + testQuestions +
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
        Test other = (Test) obj;
        return Objects.equals(id, other.id);
    }
}
