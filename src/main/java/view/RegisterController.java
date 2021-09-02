package view;

import control.SaverAndLoader;
import control.SceneController;
import control.SoundController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class RegisterController {

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Label label;

    public void register(Event event) throws IOException {
        String password = passwordField.getText();
        String username = usernameField.getText();
        if (username.length() == 0 || password.length() == 0) {
            label.setText("please enter both username and password!");
        } else {
            if (User.getUserByUsername(username) == null) {
                SoundController.playRegisterSound();
                new User(username, password);
                SaverAndLoader.saveAllUsers();
                passwordField.setText("");
                usernameField.setText("");
                label.setText("Register successful!");
                back(event);
            } else {
                label.setText("A user exists with this username!");
            }
        }
    }

    public void pressedEnter(KeyEvent keyEvent) throws Exception {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            register(keyEvent);
        } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
            back(keyEvent);
        }
    }

    public void back(Event event) throws IOException {
        SoundController.playButtonSound();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneController.startWelcomeScene(stage);
    }
}
