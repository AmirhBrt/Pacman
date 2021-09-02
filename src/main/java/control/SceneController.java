package control;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Map;
import model.Pacman;

import java.io.IOException;
import java.net.URL;

public class SceneController {

    public static void startGameController(Stage primaryStage , int lifeCounter , Map map) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/GameView.fxml"));
        Parent root = fxmlLoader.load();
        GameController gameController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        gameController.handleAction(scene);
        gameController.setPacmanLifeCounter(lifeCounter);
        gameController.setPacman(new Pacman(GameController.getCellHeight() / 2.2));
        gameController.setMap(map);
        gameController.init();
        primaryStage.setScene(scene);
    }

    public static void startLoginScene(Stage primaryStage) throws IOException {
        URL loginUrl = SceneController.class.getResource("/fxml/LoginView.fxml");
        Parent root = FXMLLoader.load(loginUrl);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public static void startProfileScene(Stage primaryStage) throws IOException {
        URL profileUrl = SceneController.class.getResource("/fxml/ProfileView.fxml");
        Parent root = FXMLLoader.load(profileUrl);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public static void startLoadMapScene(Stage primaryStage , Map map) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/LoadMapView.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        LoadMapController loadMapController = fxmlLoader.getController();
        loadMapController.setMap(map);
        primaryStage.setScene(scene);
    }

    public static void startRegisterScene(Stage primaryStage) throws IOException {
        URL registerUrl = SceneController.class.getResource("/fxml/RegisterView.fxml");
        Parent root = FXMLLoader.load(registerUrl);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public static void startScoreboardScene(Stage primaryStage) throws IOException {
        URL scoreboardUrl = SceneController.class.getResource("/fxml/ScoreboardView.fxml");
        Parent root = FXMLLoader.load(scoreboardUrl);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public static void startStartGameScene(Stage primaryStage , Map map) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/StartGameView.fxml"));
        Parent root = fxmlLoader.load();
        StartGameController startGameController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        startGameController.setMap(map);
        startGameController.init();
    }

    public static void startWelcomeScene(Stage primaryStage) throws IOException {
        URL fxmlURL = SceneController.class.getResource("/fxml/WelcomeView.fxml");
        Parent root = FXMLLoader.load(fxmlURL);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
}
