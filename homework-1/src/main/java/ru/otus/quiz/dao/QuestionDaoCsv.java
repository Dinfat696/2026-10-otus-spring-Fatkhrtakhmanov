package ru.otus.quiz.dao;

import org.springframework.core.io.Resource;
import ru.otus.quiz.domain.Answer;
import ru.otus.quiz.domain.Question;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoCsv implements QuestionDao {

    private static final String FIELD_SEPARATOR = ",";

    private final Resource resource;

    public QuestionDaoCsv(Resource resource) {
        this.resource = resource;
    }

    @Override
    public List<Question> getAllQuestions() {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {

            List<Question> questions = new ArrayList<>();

            StringBuilder buffer = new StringBuilder();
            int codePoint;
            while ((codePoint = reader.read()) != -1) {
                buffer.append((char) codePoint);
            }

            String[] lines = buffer.toString().split("\\r?\\n");
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                questions.add(parseLine(line));
            }

            return questions;

        } catch (IOException e) {
            throw new QuizDataException(
                    "Failed to read questions from CSV resource: " + resource.getDescription(), e);
        }
    }

    private Question parseLine(String line) {
        String[] fields = line.split(FIELD_SEPARATOR);
        String text = fields[0];
        List<Answer> answers = new ArrayList<>();
        for (int i = 1; i < fields.length; i++) {
            String answerText = fields[i].trim();
            if (!answerText.isEmpty()) {
                answers.add(new Answer(answerText));
            }
        }
        return new Question(text, answers);
    }
}
