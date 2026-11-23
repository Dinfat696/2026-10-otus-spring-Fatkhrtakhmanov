package ru.otus.quiz.service;

import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.domain.Answer;
import ru.otus.quiz.domain.Question;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class QuestionServiceTest {

    @Test
    void getAllQuestionsReturnsQuestionsFromDao() {
        Question question = new Question("Is the Sun a star?", Arrays.asList(new Answer("Yes", true)));
        QuestionDao dao = new StubQuestionDao(Arrays.asList(question));
        QuestionService service = new QuestionServiceImpl(dao);

        List<Question> questions = service.getAllQuestions();

        assertFalse(questions.isEmpty());
        assertEquals(1, questions.size());
        assertEquals("Is the Sun a star?", questions.get(0).getText());
    }

    private static class StubQuestionDao implements QuestionDao {
        private final List<Question> questions;

        StubQuestionDao(List<Question> questions) {
            this.questions = questions;
        }

        @Override
        public List<Question> getAllQuestions() {
            return questions;
        }
    }
}
