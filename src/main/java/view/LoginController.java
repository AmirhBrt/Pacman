package view;

import control.SceneController;
import control.SoundController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label label;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;


    public void enterPressed(KeyEvent keyEvent) throws Exception {
        if (keyEvent.getCode() == KeyCode.ENTER)
            login(keyEvent);
        else if (keyEvent.getCode() == KeyCode.ESCAPE) {
            back(keyEvent);
        }
    }

    public void login(InputEvent event) throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.length() == 0 || password.length() == 0) {
            label.setText("Please enter both username and password!");
        } else {
            User user = User.getUserByUsername(username);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    SoundController.playRegisterSound();
                    WelcomeController.setUser(user);
                    back(event);
                } else {
                    label.setText("Wrong Password!");
                }
            } else {
                label.setText("No user exists with this name!");
            }
        }
    }

    public void back(InputEvent event) throws IOException {
        if (WelcomeController.getUser() == null)
            SoundController.playButtonSound();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneController.startWelcomeScene(stage);
    }
}
