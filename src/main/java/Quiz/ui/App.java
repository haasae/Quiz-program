package Quiz.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public void start(Stage primaryStage) throws Exception {

        // Laster opp fxml filen
        Parent root = FXMLLoader.load(getClass().getResource("resources/App.fxml"));

        Scene scene = new Scene(root, 350, 400);

        // Laster opp css for knapper & bakgrunn
        scene.getStylesheets().addAll(this.getClass().getResource("resources/styles.css").toExternalForm());

        primaryStage.setTitle("Quiz Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
