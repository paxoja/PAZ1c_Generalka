package org.generalka.storage;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

public class TestHistory {
    private Long id;
    private int score;
    private String report;
    private Timestamp date;
    private Test test;
    private User user;

    public TestHistory() {

    }

    public TestHistory(Long id, int score, String report, Timestamp date, Test test, User user) {
        this.id = id;
        this.score = score;
        this.report = report;
        this.date = date;
        this.test = test;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
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


}
