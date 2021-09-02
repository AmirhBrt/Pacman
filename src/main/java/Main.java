
import control.SaverAndLoader;
import control.SceneController;
import control.SoundController;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.User;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        User.setUsers(SaverAndLoader.loadAllUsers());
        SoundController.playStartGameSound();
        Font.loadFont(getClass().getResource("/fonts/PacFont.TTF").toExternalForm(), 60);
        primaryStage.setTitle("Pacman");
        SceneController.startWelcomeScene(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
