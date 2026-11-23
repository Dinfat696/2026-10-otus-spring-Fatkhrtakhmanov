package ru.otus.quiz.service;

import ru.otus.quiz.domain.Student;
import ru.otus.quiz.domain.TestResult;

public interface TestingService {

    TestResult test(Student student);
}
