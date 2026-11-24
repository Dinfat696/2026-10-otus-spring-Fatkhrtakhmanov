package ru.otus.quiz.service;

import org.springframework.stereotype.Service;
import ru.otus.quiz.config.QuizProperties;
import ru.otus.quiz.domain.Answer;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.domain.TestResult;

import java.util.List;

@Service
public class TestingServiceImpl implements TestingService {

    private final QuestionService questionService;
    private final IOService ioService;
    private final MessageService messageService;
    private final int questionsLimit;
    private final int passingScore;

    public TestingServiceImpl(QuestionService questionService,
                              IOService ioService,
                              MessageService messageService,
                              QuizProperties quizProperties) {
        this.questionService = questionService;
        this.ioService = ioService;
        this.messageService = messageService;
        this.questionsLimit = quizProperties.getQuestionsLimit();
        this.passingScore = quizProperties.getPassingScore();
    }

    @Override
    public TestResult test(Student student) {
        List<Question> questions = questionService.getAllQuestions();
        if (questions.size() > questionsLimit) {
            questions = questions.subList(0, questionsLimit);
        }

        int correctAnswers = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            int chosenAnswer = askAnswer(question, i + 1, questions.size());
            if (question.isCorrectAnswer(chosenAnswer)) {
                correctAnswers++;
            }
        }

        return new TestResult(student, correctAnswers, questions.size(), passingScore);
    }

    private int askAnswer(Question question, int questionNumber, int totalQuestions) {
        List<Answer> answers = question.getAnswers();
        while (true) {
            ioService.printLine(messageService.getMessage("question.header",
                    questionNumber, totalQuestions, question.getText()));
            for (int i = 0; i < answers.size(); i++) {
                ioService.printLine(messageService.getMessage("answer.option", i + 1, answers.get(i).getText()));
            }
            ioService.printLine(messageService.getMessage("ask.answer"));

            String input = ioService.readLine().trim();
            try {
                int answerNumber = Integer.parseInt(input);
                if (answerNumber >= 1 && answerNumber <= answers.size()) {
                    return answerNumber - 1;
                }
            } catch (NumberFormatException ignored) {
                // fall through to the error message below
            }
            ioService.printLine(messageService.getMessage("error.answer.invalid", answers.size()));
        }
    }
}
