package ru.otus.quiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.support.TestMessages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private IOService ioService;

    private final MessageService messageService = TestMessages.messageService();
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentServiceImpl(ioService, messageService);
    }

    @Test
    void readsFirstAndLastName() {
        when(ioService.readLine()).thenReturn("John", "Doe");

        Student student = studentService.determineStudent();

        assertThat(student).isEqualTo(new Student("John", "Doe"));
        verify(ioService).printLine("Please enter your first name:");
        verify(ioService).printLine("Please enter your last name:");
    }

    @Test
    void asksAgainWhenInputIsEmpty() {
        when(ioService.readLine()).thenReturn("", "   ", "John", "Doe");

        Student student = studentService.determineStudent();

        assertThat(student).isEqualTo(new Student("John", "Doe"));
        verify(ioService, times(2)).printLine("Input must not be empty. Please try again.");
    }

    @Test
    void trimsInput() {
        when(ioService.readLine()).thenReturn("  John  ", "  Doe  ");

        Student student = studentService.determineStudent();

        assertThat(student).isEqualTo(new Student("John", "Doe"));
    }
}
