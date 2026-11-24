package ru.otus.quiz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties(prefix = "quiz")
public class QuizProperties {

    private String locale = "en";
    private String csvPattern = "questions_%s.csv";
    private int questionsLimit = 5;
    private int passingScore = 4;

    public Locale getLocale() {
        return Locale.forLanguageTag(locale);
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCsvPattern() {
        return csvPattern;
    }

    public void setCsvPattern(String csvPattern) {
        this.csvPattern = csvPattern;
    }

    public int getQuestionsLimit() {
        return questionsLimit;
    }

    public void setQuestionsLimit(int questionsLimit) {
        this.questionsLimit = questionsLimit;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }
}
