package org.generalka.storage;

import java.util.List;
import java.util.Objects;
import java.util.Date;

public class Test {
    private  Long id;
    private String topic;
    private boolean isWholeSemester;
    private Date date;
    private String subject;
    private String semester;
    private int yearOfStudy;
    private User user; // User object representing the foreign key relationship

    public Test(){

    }

    public Test(Long id, String topic, boolean isWholeSemester, Date date, String subject, String semester, int yearOfStudy, User user) {
        this.id = id;
        this.topic = topic;
        this.isWholeSemester = isWholeSemester;
        this.date = date;
        this.subject = subject;
        this.semester = semester;
        this.yearOfStudy = yearOfStudy;
        this.user = user;
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
