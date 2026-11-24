package ru.otus.quiz.service;

import org.junit.jupiter.api.Test;
import ru.otus.quiz.domain.Student;

import static org.assertj.core.api.Assertions.assertThat;

class QuizSessionTest {

    private static final Student STUDENT = new Student("John", "Doe");

    private final QuizSession quizSession = new QuizSession();

    @Test
    void isEmptyInitially() {
        assertThat(quizSession.getStudent()).isEmpty();
    }

    @Test
    void storesStudentAfterLogin() {
        quizSession.login(STUDENT);

        assertThat(quizSession.getStudent()).contains(STUDENT);
    }

    @Test
    void clearsStudentAfterLogout() {
        quizSession.login(STUDENT);

        quizSession.logout();

        assertThat(quizSession.getStudent()).isEmpty();
    }
}
