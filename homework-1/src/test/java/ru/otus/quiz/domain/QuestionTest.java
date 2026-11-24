package ru.otus.quiz.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionTest {

    @Test
    void identifiesCorrectAnswer() {
        Question question = new Question("What is 2 + 2?", List.of(
                new Answer("4", true),
                new Answer("5", false)));

        assertThat(question.isCorrectAnswer(0)).isTrue();
        assertThat(question.isCorrectAnswer(1)).isFalse();
    }

    @Test
    void answersAreNotModifiable() {
        Question question = new Question("What is 2 + 2?", List.of(new Answer("4", true)));

        assertThatThrownBy(() -> question.getAnswers().add(new Answer("5", false)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void copiesAnswersFromSourceList() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("4", true));
        Question question = new Question("What is 2 + 2?", answers);

        answers.add(new Answer("5", false));

        assertThat(question.getAnswers()).hasSize(1);
    }
}
