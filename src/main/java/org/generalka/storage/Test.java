package org.generalka.storage;

import java.util.List;
import java.util.Objects;
import java.util.Date;

public class Test {
    private int id;
    private String name;
    private String topic;
    private boolean isWholeSemester;
    private Date date;
    private String subject;
    private int semester;
    private int yearOfStudy;
    private User user; // User object representing the foreign key relationship

    // Constructors, getters, setters

    public Test(int id, String name, String topic, boolean isWholeSemester, Date date, String subject, int semester, int yearOfStudy, User user) {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.isWholeSemester = isWholeSemester;
        this.date = date;
        this.subject = subject;
        this.semester = semester;
        this.yearOfStudy = yearOfStudy;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean getIsWholeSemester() {
        return isWholeSemester;
    }

    public void setIsWholeSemester(boolean isWholeSemester) {
        this.isWholeSemester = isWholeSemester;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", topic='" + topic + '\'' +
                ", isWholeSemester=" + isWholeSemester +
                ", date=" + date +
                ", subject='" + subject + '\'' +
                ", semester=" + semester +
                ", yearOfStudy=" + yearOfStudy +
                ", user=" + user +
                '}';
    }
}
