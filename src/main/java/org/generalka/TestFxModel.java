package org.generalka;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.generalka.storage.*;

import java.sql.Timestamp;
import java.util.Date;

public class TestFxModel {
    long id;
    private StringProperty topic = new SimpleStringProperty();
    private BooleanProperty isWholeSemester = new SimpleBooleanProperty();
    private ObjectProperty<Timestamp> date = new SimpleObjectProperty<>();
    private StringProperty subject = new SimpleStringProperty();
    private StringProperty semester = new SimpleStringProperty();
    private IntegerProperty yearOfStudy = new SimpleIntegerProperty();
    private ObjectProperty<User> user = new SimpleObjectProperty<>();

    public TestFxModel() {

    }

    public TestFxModel(Test test) {
        setId(test.getId());
        setTopic(test.getTopic());
        setIsWholeSemester(test.getIsWholeSemester());
        setDate(test.getDate());
        setSubject(test.getSubject());
        setSemester(test.getSemester());
        setYearOfStudy(test.getYearOfStudy());
        setUser(test.getUser());
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

    public Boolean getIsWholeSemester() {
        return isWholeSemester.get();
    }

    public BooleanProperty isWholeSemesterProperty() {
        return isWholeSemester;
    }

    public void setIsWholeSemester(Boolean isWholeSemester) {
        this.isWholeSemester.set(isWholeSemester);
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

    public String getSubject() {
        return subject.get();
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getSemester() {
        return semester.get();
    }

    public StringProperty semesterProperty() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester.set(semester);
    }

    public Integer getYearOfStudy() {
        return yearOfStudy.get();
    }

    public IntegerProperty yearOfStudyProperty() {
        return yearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy.set(yearOfStudy);
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

    public Test getTest() {
        return new Test(getId(), getTopic(), getIsWholeSemester(), getDate(), getSubject(), getSemester(), getYearOfStudy(), getUser());
    }
}

