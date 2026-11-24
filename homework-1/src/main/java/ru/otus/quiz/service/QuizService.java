package ru.otus.quiz.service;

import ru.otus.quiz.domain.Student;

public interface QuizService {

    void start();

    void run(Student student);
}
