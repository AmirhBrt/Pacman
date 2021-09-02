package view;

import control.SceneController;
import control.SoundController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Map;
import model.User;

import java.io.IOException;

public class WelcomeController {
    private static User user;
    @FXML
    private Label text;
    @FXML
    private Label loginAndProfile;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        WelcomeController.user = user;
    }

    public Label getText() {
        return text;
    }

    @FXML
    private void initialize(){
        if (user == null) {
            loginAndProfile.setText("Login");
        } else {
            loginAndProfile.setText(user.getUsername() + "'s Profile");
        }
    }

    public void loginOrProfile(MouseEvent mouseEvent) throws IOException {
        if (user == null) {
            openLoginMenu(mouseEvent);
        } else {
            openProfileMenu(mouseEvent);
        }
    }

    public void openRegisterMenu(MouseEvent mouseEvent) throws IOException {
        SoundController.playButtonSound();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        SceneController.startRegisterScene(stage);
    }

    private void openLoginMenu(MouseEvent mouseEvent) throws IOException {
        SoundController.playButtonSound();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        SceneController.startLoginScene(stage);
    }

    private void openProfileMenu(MouseEvent mouseEvent) throws IOException {
        SoundController.playButtonSound();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        SceneController.startProfileScene(stage);
    }

    public void openScoreboard(MouseEvent mouseEvent) throws IOException {
        SoundController.playButtonSound();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        SceneController.startScoreboardScene(stage);
    }

    public void startGame(MouseEvent mouseEvent) throws IOException {
        SoundController.playButtonSound();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        SceneController.startStartGameScene(stage , new Map(17 , 11));
    }

}
