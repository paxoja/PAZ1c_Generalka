package org.generalka.table;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class TestHistoryProfile {

    private Long id;
    private String topic;
    private String subject;
    private String semester;
    private int yearOfStudy;
    private int score;
    private int totalQuestions;
    private Timestamp date;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TestHistoryProfile(Long id, String topic, String subject, String semester, int yearOfStudy, int score, int totalQuestions, Timestamp date) {
        this.id = id;
        this.topic = topic;
        this.subject = subject;
        this.semester = semester;
        this.yearOfStudy = yearOfStudy;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getScoreString() {
        return score + "/" + totalQuestions;
    }
    public String getFormattedDate() {
        return date.toLocalDateTime().toLocalDate().format(dateFormatter);
    }
}
