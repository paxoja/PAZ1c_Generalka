package org.generalka.table;

public class TestOverview {
    private Long id;
    private String topic;
    private String subject;
    private String semester;
    private int yearOfStudy;
    private boolean isWholeSemester;

    public TestOverview(Long id, String topic, String subject, String semester, int yearOfStudy, boolean isWholeSemester) {
        this.id = id;
        this.topic = topic;
        this.subject = subject;
        this.semester = semester;
        this.yearOfStudy = yearOfStudy;
        this.isWholeSemester = isWholeSemester;
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

    public boolean isWholeSemester() {
        return isWholeSemester;
    }

    public void setWholeSemester(boolean wholeSemester) {
        isWholeSemester = wholeSemester;
    }
}


