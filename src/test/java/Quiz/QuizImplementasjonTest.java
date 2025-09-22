package Quiz;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Quiz.game.QuizImplementasjon;

import java.util.ArrayList;
import java.util.List;

public class QuizImplementasjonTest {

    private QuizImplementasjon quiz;

    @BeforeEach
    void setUp() {
        

        quiz = new QuizImplementasjon(new ArrayList<>());
    }

    @Test
    void testGetQuizQuestions() {
        List<Quiz.game.Question> questions = quiz.getQuizQuestions();
        assertEquals(2, questions.size(), "Should correctly parse two questions.");

        Quiz.game.Question firstQuestion = questions.get(0);
        assertEquals("Hvilken ntnu lokasjon er best?", firstQuestion.getQuestion());
        assertTrue(firstQuestion.getOptions().contains("Trondheim"));
        assertEquals(2, firstQuestion.getOptions().size(), "Should include two options.");

        
    }
}
