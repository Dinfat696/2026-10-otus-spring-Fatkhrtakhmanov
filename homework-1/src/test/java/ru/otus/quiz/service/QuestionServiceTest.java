package ru.otus.quiz.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.domain.Answer;
import ru.otus.quiz.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionDao questionDao;

    @Test
    void returnsQuestionsFromDao() {
        List<Question> questions = List.of(new Question("Is the Sun a star?", List.of(new Answer("Yes", true))));
        when(questionDao.getAllQuestions()).thenReturn(questions);
        QuestionService questionService = new QuestionServiceImpl(questionDao);

        List<Question> result = questionService.getAllQuestions();

        assertThat(result).isEqualTo(questions);
        verify(questionDao).getAllQuestions();
    }
}
