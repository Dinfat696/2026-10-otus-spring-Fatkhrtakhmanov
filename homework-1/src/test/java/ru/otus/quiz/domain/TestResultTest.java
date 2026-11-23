package ru.otus.quiz.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestResultTest {

    private static final Student STUDENT = new Student("John", "Doe");

    @Test
    void passedWhenCorrectAnswersEqualPassingScore() {
        TestResult result = new TestResult(STUDENT, 4, 5, 4);

        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void passedWhenCorrectAnswersAbovePassingScore() {
        TestResult result = new TestResult(STUDENT, 5, 5, 4);

        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void failedWhenCorrectAnswersBelowPassingScore() {
        TestResult result = new TestResult(STUDENT, 3, 5, 4);

        assertThat(result.isPassed()).isFalse();
    }
}
