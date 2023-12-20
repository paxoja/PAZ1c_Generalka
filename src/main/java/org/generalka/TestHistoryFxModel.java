package org.generalka;

import javafx.beans.property.*;
import org.generalka.storage.Test;
import org.generalka.storage.TestHistory;
import org.generalka.storage.User;

import java.sql.Timestamp;

public class TestHistoryFxModel {

    long id;
    private StringProperty topic = new SimpleStringProperty();
    private IntegerProperty score = new SimpleIntegerProperty();
    private StringProperty report = new SimpleStringProperty();
    private ObjectProperty<Timestamp> date = new SimpleObjectProperty<>();
    private ObjectProperty<Test> test = new SimpleObjectProperty<>();
    private ObjectProperty<User> user = new SimpleObjectProperty<>();

    public TestHistoryFxModel() {

    }


    public TestHistoryFxModel(TestHistory testHistory) {
        setId(testHistory.getId());
        setScore(testHistory.getScore());
        setReport(testHistory.getReport());
        setDate(testHistory.getDate());
        setTest(testHistory.getTest());
        setUser(testHistory.getUser());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic.get();
    }

    public StringProperty topicProperty() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic.set(topic);
    }

    public int getScore() {
        return score.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public String getReport() {
        return report.get();
    }

    public StringProperty reportProperty() {
        return report;
    }

    public void setReport(String report) {
        this.report.set(report);
    }

    public Timestamp getDate() {
        return date.get();
    }

    public ObjectProperty<Timestamp> dateProperty() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date.set(date);
    }

    public Test getTest() {
        return test.get();
    }

    public ObjectProperty<Test> testProperty() {
        return test;
    }

    public void setTest(Test test) {
        this.test.set(test);
    }

    public User getUser() {
        return user.get();
    }

    public ObjectProperty<User> userProperty() {
        return user;
    }

    public void setUser(User user) {
        this.user.set(user);
    }

    public TestHistory getTestHistory() {
        return new TestHistory(getId(), getScore(), getReport(), getDate(), getTest(), getUser());
    }
}
