package ru.otus.quiz.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Question {

    private final String text;
    private final List<Answer> answers;

    public Question(String text, List<Answer> answers) {
        this.text = text;
        this.answers = new ArrayList<>(answers);
    }

    public String getText() {
        return text;
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

    @Override
    public String toString() {
        return "Question{text='" + text + "', answers=" + answers + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(text, question.text) && Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, answers);
    }
}
