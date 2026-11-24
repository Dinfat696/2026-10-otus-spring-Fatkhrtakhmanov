package ru.otus.quiz.service;

import org.springframework.stereotype.Component;
import ru.otus.quiz.domain.Student;

import java.util.Optional;

/**
 * Holds the student of the current shell session.
 */
@Component
public class QuizSession {

    private Student student;

    public void login(Student student) {
        this.student = student;
    }

    public void logout() {
        this.student = null;
    }

    public Optional<Student> getStudent() {
        return Optional.ofNullable(student);
    }
}
