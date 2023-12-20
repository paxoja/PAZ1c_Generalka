package org.generalka;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.generalka.storage.*;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionFxModel {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty question = new SimpleStringProperty();
    private ObjectProperty<Test> test = new SimpleObjectProperty<>();
    private ListProperty<AnswerFxModel> answers = new SimpleListProperty<>(FXCollections.observableArrayList());

    public TestQuestionFxModel() {
    }

    public TestQuestionFxModel(TestQuestion testQuestion) {
        setId(testQuestion.getId());
        setQuestion(testQuestion.getQuestion());
        setTest(testQuestion.getTest());

        ObservableList<AnswerFxModel> answerModels = FXCollections.observableArrayList();
        for (Answer answer : testQuestion.getAnswers()) {
            answerModels.add(new AnswerFxModel(answer));
        }
        setAnswers(answerModels);
    }


    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getQuestion() {
        return question.get();
    }

    public StringProperty questionProperty() {
        return question;
    }

    public void setQuestion(String question) {
        this.question.set(question);
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

    public ObservableList<AnswerFxModel> getAnswers() {
        return answers.get();
    }

    public ListProperty<AnswerFxModel> answersProperty() {
        return answers;
    }

    public void setAnswers(ObservableList<AnswerFxModel> answers) {
        this.answers.set(answers);
    }

    public TestQuestion getTestQuestion() {
        return new TestQuestion(getId(), getQuestion(), getTest(), getAnswersAsList());
    }

    // method to get answers as a List of Answer objects
    public List<Answer> getAnswersAsList() {
        List<Answer> answers = new ArrayList<>();
        for (AnswerFxModel answerModel : getAnswers()) {
            answers.add(answerModel.getAnswers());
        }
        return answers;
    }
}
