package ru.otus.quiz.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.otus.quiz.domain.Answer;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.domain.TestResult;
import ru.otus.quiz.support.TestMessages;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestingServiceImplTest {

    private static final Student STUDENT = new Student("John", "Doe");

    @Mock
    private QuestionService questionService;

    @Mock
    private IOService ioService;

    private final MessageSource messageSource = TestMessages.messageSource();

    @Test
    void returnsFullScoreWhenAllAnswersCorrect() {
        when(questionService.getAllQuestions()).thenReturn(List.of(
                question("Question 1", "Correct", "Wrong"),
                question("Question 2", "Correct", "Wrong")));
        when(ioService.readLine()).thenReturn("1", "1");
        TestingService testingService = testingService(5, 2);

        TestResult result = testingService.test(STUDENT);

        assertThat(result.getCorrectAnswers()).isEqualTo(2);
        assertThat(result.getTotalQuestions()).isEqualTo(2);
        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void returnsZeroScoreWhenAllAnswersWrong() {
        when(questionService.getAllQuestions()).thenReturn(List.of(
                question("Question 1", "Correct", "Wrong"),
                question("Question 2", "Correct", "Wrong")));
        when(ioService.readLine()).thenReturn("2", "2");
        TestingService testingService = testingService(5, 2);

        TestResult result = testingService.test(STUDENT);

        assertThat(result.getCorrectAnswers()).isZero();
        assertThat(result.getTotalQuestions()).isEqualTo(2);
        assertThat(result.isPassed()).isFalse();
    }

    @Test
    void asksOnlyConfiguredNumberOfQuestions() {
        when(questionService.getAllQuestions()).thenReturn(List.of(
                question("Question 1", "Correct", "Wrong"),
                question("Question 2", "Correct", "Wrong"),
                question("Question 3", "Correct", "Wrong")));
        when(ioService.readLine()).thenReturn("1");
        TestingService testingService = testingService(1, 3);

        TestResult result = testingService.test(STUDENT);

        assertThat(result.getTotalQuestions()).isEqualTo(1);
        assertThat(result.getCorrectAnswers()).isEqualTo(1);
        verify(ioService, times(1)).readLine();
    }

    @Test
    void asksAgainWhenAnswerIsInvalid() {
        when(questionService.getAllQuestions()).thenReturn(List.of(
                question("Question 1", "Correct", "Wrong", "Maybe", "Never")));
        when(ioService.readLine()).thenReturn("abc", "0", "5", "2");
        TestingService testingService = testingService(1, 1);

        TestResult result = testingService.test(STUDENT);

        assertThat(result.getCorrectAnswers()).isZero();
        assertThat(result.getTotalQuestions()).isEqualTo(1);
        verify(ioService, times(4)).readLine();
        verify(ioService, times(3)).printLine("Please enter a number from 1 to 4.");
    }

    @Test
    void returnsEmptyResultWhenThereAreNoQuestions() {
        when(questionService.getAllQuestions()).thenReturn(List.of());
        TestingService testingService = testingService(1, 1);

        TestResult result = testingService.test(STUDENT);

        assertThat(result.getCorrectAnswers()).isZero();
        assertThat(result.getTotalQuestions()).isZero();
        assertThat(result.isPassed()).isFalse();
        verifyNoInteractions(ioService);
    }

    private TestingService testingService(int questionsLimit, int passingScore) {
        return new TestingServiceImpl(questionService, ioService, messageSource, questionsLimit, passingScore);
    }

    private static Question question(String text, String... options) {
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < options.length; i++) {
            answers.add(new Answer(options[i], i == 0));
        }
        return new Question(text, answers);
    }
}
