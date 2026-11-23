package ru.otus.quiz.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import ru.otus.quiz.domain.Answer;
import ru.otus.quiz.domain.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestionDaoCsv implements QuestionDao {

    private static final String FIELD_SEPARATOR = ";";

    private final Resource resource;

    public QuestionDaoCsv(@Value("${quiz.csv-path}") String csvPath) {
        this.resource = new ClassPathResource(csvPath);
    }

    @Override
    public List<Question> getAllQuestions() {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            List<Question> questions = new ArrayList<>();
            String line;
            int lineNumber = 0;
            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                questions.add(parseLine(line, lineNumber));
            }
            return questions;

        } catch (IOException e) {
            throw new QuizDataException(
                    "Failed to read questions from CSV resource: " + resource.getDescription(), e);
        }
    }

    private Question parseLine(String line, int lineNumber) {
        String[] fields = line.split(FIELD_SEPARATOR, -1);
        if (fields.length < 3) {
            throw new QuizDataException(
                    "Invalid CSV line " + lineNumber + ": question, correct answer and at least one option are required");
        }

        String text = fields[0].trim();
        int correctAnswerIndex = parseCorrectAnswerIndex(fields[1], lineNumber);

        List<Answer> answers = new ArrayList<>();
        for (int i = 2; i < fields.length; i++) {
            String answerText = fields[i].trim();
            if (!answerText.isEmpty()) {
                int answerIndex = answers.size();
                answers.add(new Answer(answerText, answerIndex == correctAnswerIndex));
            }
        }

        if (answers.isEmpty() || correctAnswerIndex >= answers.size()) {
            throw new QuizDataException("Invalid CSV line " + lineNumber + ": correct answer index is out of range");
        }
        return new Question(text, answers);
    }

    private int parseCorrectAnswerIndex(String field, int lineNumber) {
        try {
            int correctAnswerNumber = Integer.parseInt(field.trim());
            if (correctAnswerNumber < 1) {
                throw new NumberFormatException();
            }
            return correctAnswerNumber - 1;
        } catch (NumberFormatException e) {
            throw new QuizDataException("Invalid CSV line " + lineNumber + ": correct answer index must be a positive number");
        }
    }
}
