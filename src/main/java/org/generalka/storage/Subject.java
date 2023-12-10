package org.generalka.storage;

import java.util.Objects;

public class Subject {

    private Long id;

    private String subject;

    private int semester;

    private int yearOfStudy;

    public Subject(Long id, String subject, int semester, int yearOfStudy) {
        this.id = id;
        this.subject = subject;
        this.semester = semester;
        this.yearOfStudy = yearOfStudy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", semester=" + semester +
                ", yearOfStudy=" + yearOfStudy +
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
        Subject other = (Subject) obj;
        return Objects.equals(id, other.id);
    }
}
