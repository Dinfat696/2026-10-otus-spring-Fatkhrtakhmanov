package ru.otus.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.otus.quiz.config.AppConfig;
import ru.otus.quiz.service.QuizService;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(classes = {AppConfig.class, QuizApplicationTest.StubIoConfig.class})
@TestPropertySource("classpath:application-test.properties")
class QuizApplicationTest {

    @Autowired
    private QuizService quizService;

    @Autowired
    private ScriptedIOService ioService;

    @BeforeEach
    void resetIoService() {
        ioService.reset();
    }

    @Test
    void studentPassesWhenEnoughCorrectAnswers() {
        ioService.provide("Dinar", "Fatkhrtakhmanov", "1", "1", "1", "1", "1");

        quizService.start();

        String output = ioService.getOutput();
        assertTrue(output.contains("Dinar Fatkhrtakhmanov"));
        assertTrue(output.contains("Correct answers: 5 of 5"));
        assertTrue(output.contains("Congratulations! You passed the test."));
    }

    @Test
    void studentFailsWhenNotEnoughCorrectAnswers() {
        ioService.provide("Dinar", "Fatkhrtakhmanov", "2", "2", "2", "2", "2");

        quizService.start();

        String output = ioService.getOutput();
        assertTrue(output.contains("Correct answers: 0 of 5"));
        assertTrue(output.contains("Sorry, you failed the test. Required correct answers: 3"));
    }

    @Test
    void invalidAnswerIsAskedAgain() {
        ioService.provide("Dinar", "Fatkhrtakhmanov", "abc", "9", "1", "1", "1", "1", "1");

        quizService.start();

        String output = ioService.getOutput();
        assertTrue(output.contains("Please enter a number from 1 to 4."));
        assertTrue(output.contains("Correct answers: 5 of 5"));
    }

    @Configuration
    static class StubIoConfig {

        @Bean
        @Primary
        ScriptedIOService scriptedIOService() {
            return new ScriptedIOService();
        }
    }
}
