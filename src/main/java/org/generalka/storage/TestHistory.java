package org.generalka.storage;

import java.util.Date;
import java.util.Objects;

public class TestHistory {
    private int id;
    private int score;
    private String report;
    private Date date;
    private Test test; // Test object representing the foreign key relationship
    private User user; // User object representing the foreign key relationship

    public TestHistory(int id, int score, String report, Date date, Test test, User user) {
        this.id = id;
        this.score = score;
        this.report = report;
        this.date = date;
        this.test = test;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Constructors, getters, setters
}
