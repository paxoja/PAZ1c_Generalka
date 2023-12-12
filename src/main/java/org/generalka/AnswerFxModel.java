package org.generalka;

import javafx.beans.property.*;
import org.generalka.storage.*;

public class AnswerFxModel {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty answer = new SimpleStringProperty();
    private BooleanProperty isCorrect = new SimpleBooleanProperty();
    private ObjectProperty<TestQuestionFxModel> testQuestion = new SimpleObjectProperty<>();

    public AnswerFxModel() {
    }

    public AnswerFxModel(Answer answer) {
        setId(answer.getId());
        setAnswer(answer.getAnswer());
        setIsCorrect(answer.getIsCorrect());
        setTestQuestion(new TestQuestionFxModel(answer.getTestQuestion()));
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

    public String getAnswer() {
        return answer.get();
    }

    public StringProperty answerProperty() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    public boolean getIsCorrect() {
        return isCorrect.get();
    }

    public BooleanProperty isCorrectProperty() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect.set(isCorrect);
    }

    public TestQuestionFxModel getTestQuestion() {
        return testQuestion.get();
    }

    public ObjectProperty<TestQuestionFxModel> testQuestionProperty() {
        return testQuestion;
    }

    public void setTestQuestion(TestQuestionFxModel testQuestion) {
        this.testQuestion.set(testQuestion);
    }

    public Answer getAnswers() {
        return new Answer(getId(), getAnswer(), getIsCorrect(), getTestQuestion().getTestQuestion());
    }
}
