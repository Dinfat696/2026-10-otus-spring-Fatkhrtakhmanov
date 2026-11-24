package ru.otus.quiz.domain;

import java.util.Objects;

public class TestResult {

    private final Student student;
    private final int correctAnswers;
    private final int totalQuestions;
    private final int passingScore;

    public TestResult(Student student, int correctAnswers, int totalQuestions, int passingScore) {
        this.student = student;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.passingScore = passingScore;
    }

    public Student getStudent() {
        return student;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public boolean isPassed() {
        return correctAnswers >= passingScore;
    }

    @Override
    public String toString() {
        return "TestResult{student=" + student
                + ", correctAnswers=" + correctAnswers
                + ", totalQuestions=" + totalQuestions
                + ", passingScore=" + passingScore
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult that = (TestResult) o;
        return correctAnswers == that.correctAnswers
                && totalQuestions == that.totalQuestions
                && passingScore == that.passingScore
                && Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, correctAnswers, totalQuestions, passingScore);
    }
}
