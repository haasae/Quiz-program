package Quiz.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;

import Quiz.ui.AppController;

public class QuizImplementasjon implements Quiz {

    private List<Question> questions;

    public QuizImplementasjon(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuizQuestions() {
        try {
            File myObj = new File("src/main/java/Quiz/QuizQuestions.txt");
            Scanner reader = new Scanner(myObj);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] parts = data.split(";");
                if (parts.length >= 3 && parts.length < 7) {
                    String questionText = parts[0];
                    List<String> options = new ArrayList<>();

                    for (int i = 1; i < parts.length; i++) {
                        options.add(parts[i]);
                    }

                    // Siste alternativ i tekstfil er riktig
                    String correctAnswer = options.get(options.size() - 1);

                    // Stokker om alternativer slik at ikke siste alternativ som alltid er riktig
                    Collections.shuffle(options);

                    // Gjør om filtekst til Question type
                    Question question = new Question(questionText, options, options.indexOf(correctAnswer));
                    questions.add(question);
                }

            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

        return questions;
    }

    public static int getScore() {
        return AppController.correctCounter;
    }

    public static void saveScore() throws IOException {
        int i = getScore();
        String score = Integer.toString(i);

        BufferedWriter write = new BufferedWriter(new FileWriter("src/main/java/Quiz/Score.txt", true));
        write.append('\n');
        write.append(score + "/" + AppController.questions.size());

        write.close();
    }

    public static void main(String[] args) {

        QuizImplementasjon quiz = new QuizImplementasjon(new ArrayList<>());

        List<Question> questions = quiz.getQuizQuestions();

        System.out.println(questions);

    }

}