package ru.otus.quiz.service;

import ru.otus.quiz.domain.Answer;
import ru.otus.quiz.domain.Question;

import java.util.List;

public class ConsoleService {

    public void printQuestions(List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            System.out.println("No questions to show.");
            return;
        }

        System.out.println("Quiz. Please answer the following questions:");
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println((i + 1) + ". " + question.getText());
            List<Answer> answers = question.getAnswers();
            if (answers != null) {
                for (int j = 0; j < answers.size(); j++) {
                    System.out.println("   " + (j + 1) + ") " + answers.get(j).getText());
                }
            }
        }
    }
}
