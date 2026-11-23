package ru.otus.quiz.dao;

import org.junit.jupiter.api.Test;
import ru.otus.quiz.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionDaoCsvTest {

    @Test
    void readsAllQuestionsFromCsv() {
        QuestionDao questionDao = new QuestionDaoCsv("test-questions.csv");

        List<Question> questions = questionDao.getAllQuestions();

        assertEquals(5, questions.size());
        assertEquals("What is 2 + 2?", questions.get(0).getText());
        assertEquals(4, questions.get(0).getAnswers().size());
    }

    @Test
    void marksCorrectAnswerFromCsv() {
        QuestionDao questionDao = new QuestionDaoCsv("test-questions.csv");

        Question question = questionDao.getAllQuestions().get(0);

        assertTrue(question.isCorrectAnswer(0));
        assertFalse(question.isCorrectAnswer(1));
    }

    @Test
    void readsCorrectAnswerThatIsNotTheFirstOption() {
        QuestionDao questionDao = new QuestionDaoCsv("mixed-answers.csv");

        Question question = questionDao.getAllQuestions().get(0);

        assertEquals(3, question.getAnswers().size());
        assertFalse(question.isCorrectAnswer(0));
        assertTrue(question.isCorrectAnswer(1));
        assertFalse(question.isCorrectAnswer(2));
    }

    @Test
    void throwsExceptionForMalformedLine() {
        QuestionDao questionDao = new QuestionDaoCsv("invalid-questions.csv");

        assertThrows(QuizDataException.class, questionDao::getAllQuestions);
    }
}
