package Quiz.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Quiz.game.Question;
import Quiz.game.QuizImplementasjon;

public class AppController {

    // Legger til knapper og tekst fra fxml-fil

    @FXML
    private Button startButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button saveAnswerButton;

    @FXML
    private Button nextQuestionButton;

    @FXML
    private Label labelFirst;

    @FXML
    private Label labelVerifyAnswer;

    @FXML
    private VBox questionsOptionsBox;

    @FXML
    private RadioButton radio1;

    @FXML
    private RadioButton radio2;

    @FXML
    private RadioButton radio3;

    @FXML
    private RadioButton radio4;

    @FXML
    private RadioButton radio5;

    @FXML
    private Label scoreLabel;

    @FXML
    private VBox BottomBox;

    @FXML
    private Button restartButton;

    private ToggleGroup toggleGroup;

    private int currentQuestionIndex;
    public static List<Question> questions;
    private QuizImplementasjon quiz;
    public static int correctCounter = 0;

    @FXML
    public void initialize() {

        // Legger svalarternativer til i gruppe
        toggleGroup = new ToggleGroup();
        radio1.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);
        radio3.setToggleGroup(toggleGroup);
        radio4.setToggleGroup(toggleGroup);
        radio5.setToggleGroup(toggleGroup);

        // Viser knapper og tekst
        saveAnswerButton.setVisible(false);
        nextQuestionButton.setVisible(false);
        questionsOptionsBox.setVisible(false);
        scoreLabel.setVisible(false);
        restartButton.setVisible(false);
        labelFirst.setDisable(false);
        currentQuestionIndex = 0;
    }

    @FXML
    private void handleStartButtonAction(ActionEvent event) {

        // Henter spm og alternativ
        quiz = new QuizImplementasjon(new ArrayList<>());
        questions = quiz.getQuizQuestions();

        // Rydder opp og legger til nødvendige knapper & tekst
        currentQuestionIndex = 0;
        saveAnswerButton.setDisable(false);
        toggleGroup.selectToggle(null);
        startButton.setVisible(false);
        exitButton.setVisible(false);
        questionsOptionsBox.setVisible(true);
        saveAnswerButton.setVisible(true);
        nextQuestionButton.setVisible(true);
        nextQuestionButton.setDisable(true);
        nextQuestionButton.setText("Neste spørsmål");

        displayQuestion();
    }

    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        exitButton.getScene().getWindow().hide();
    }

    @FXML
    private void handleSaveAnswerButtonAction(ActionEvent event) {
        // Her finner vi ut hvilket alternativ som er valgt
        RadioButton selectedRadioButton = (RadioButton) questionsOptionsBox.getChildren().stream() // Alle noder i boxen
                                                                                                   // blir streamet
                .filter(node -> node instanceof RadioButton && ((RadioButton) node).isSelected()) // Filtrerer at noden
                                                                                                  // er av typen
                                                                                                  // RadioButton og at
                                                                                                  // den er valgt
                .findFirst().orElse(null); // Dersom ingen alternativ er valgt, return null for å unngå feilmelding

        // Sjekker at et alternativ er valgt
        if (selectedRadioButton != null) {
            saveAnswerButton.setDisable(true);
            labelFirst.setDisable(true);

            // Definerer indexen av valgt alternativ
            int selectedOptionIndex = questionsOptionsBox.getChildren().indexOf(selectedRadioButton) - 1;

            // Sjekker om riktig svar
            if (selectedOptionIndex == questions.get(currentQuestionIndex).getCorrectAnswerIndex()) {
                this.correctCounter++;
                labelVerifyAnswer.setText("Riktig!");
            } else {
                labelVerifyAnswer.setText("Feil!");
            }

            nextQuestionButton.setDisable(false);

            // Gjør at man ikke kan velge nytt alternativ etter lagret svar
            questionsOptionsBox.getChildren().stream()
                    .filter(node -> node instanceof RadioButton)
                    .forEach(node -> ((RadioButton) node).setDisable(true));

        } else {
            // Feilmelding
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Feil");
            alert.setHeaderText("Ingen svaralternativ valgt");
            alert.setContentText("Du må VELGE 1!");
            alert.showAndWait();
        }

    }

    @FXML
    private void handleNextQuestionButtonAction(ActionEvent event) throws IOException {

        // Rydder opp
        saveAnswerButton.setDisable(false);
        labelFirst.setDisable(false);
        toggleGroup.selectToggle(null); // Resetter valgt alternativ
        currentQuestionIndex++;

        // Sjekker om vi er på siste spørsmål
        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
            if (currentQuestionIndex == questions.size() - 1) {
                nextQuestionButton.setText("Se resultat");
            } else {
                nextQuestionButton.setText("Neste spørsmål");
            }

            // Rydder opp
            nextQuestionButton.setDisable(true);
            labelVerifyAnswer.setText("");

            // Resetter alternativer
            questionsOptionsBox.getChildren().stream()
                    .filter(node -> node instanceof RadioButton)
                    .forEach(node -> ((RadioButton) node).setDisable(false));

        } else {

            // Dersom vi er forbi siste spørsmål skal resultatet vises
            scoreLabel.setText("Resultat: " + correctCounter + "/" + questions.size());
            QuizImplementasjon.saveScore();

            // Rydder opp
            scoreLabel.setVisible(true);
            restartButton.setVisible(true);
            questionsOptionsBox.setVisible(false);
            nextQuestionButton.setVisible(false);
            labelVerifyAnswer.setText("");

            // Resetter alternativer
            questionsOptionsBox.getChildren().stream()
                    .filter(node -> node instanceof RadioButton)
                    .forEach(node -> ((RadioButton) node).setDisable(false));
        }
    }

    private void displayQuestion() {

        // Henter spørsmål korresponderende til spmindex
        Question question = questions.get(currentQuestionIndex);
        labelFirst.setText(question.getQuestion());

        // Displayer alternativene
        List<RadioButton> radioButtons = new ArrayList<>();
        radioButtons.add((RadioButton) questionsOptionsBox.lookup("#radio1"));
        radioButtons.add((RadioButton) questionsOptionsBox.lookup("#radio2"));
        radioButtons.add((RadioButton) questionsOptionsBox.lookup("#radio3"));
        radioButtons.add((RadioButton) questionsOptionsBox.lookup("#radio4"));
        radioButtons.add((RadioButton) questionsOptionsBox.lookup("#radio5"));

        // Lagrer hvert alternativ
        List<String> options = question.getOptions();
        for (int i = 0; i < radioButtons.size(); i++) {
            RadioButton radioButton = radioButtons.get(i);
            if (i < options.size()) {
                radioButton.setText(options.get(i));
                radioButton.setVisible(true);
            } else {
                radioButton.setVisible(false);
            }
        }

    }

    @FXML
    private void handleRestartButtonAction(ActionEvent event) {
        // Rydder opp
        scoreLabel.setVisible(false);
        restartButton.setVisible(false);
        startButton.setVisible(true);
        exitButton.setVisible(true);
        scoreLabel.setText("");
        correctCounter = 0;
        currentQuestionIndex = 0;
    }
}
