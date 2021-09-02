package view;

import control.SaverAndLoader;
import control.SceneController;
import control.SoundController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class ProfileController {

    @FXML
    private AnchorPane deleteAccountMenu;
    @FXML
    private Label changePassLabel;
    @FXML
    private AnchorPane changePasswordMenu;
    @FXML
    private VBox vBox;
    @FXML
    private PasswordField currentPassword;
    @FXML
    private PasswordField newPassword;

    public void logout(MouseEvent mouseEvent) throws IOException {
        SoundController.playRegisterSound();
        WelcomeController.setUser(null);
        back(mouseEvent);
    }

    public void openRemoveUser() {
        SoundController.playButtonSound();
        vBox.setDisable(true);
        deleteAccountMenu.setVisible(true);
        deleteAccountMenu.setDisable(false);
        vBox.setEffect(new GaussianBlur());
    }


    public void openEditPassword() {
        SoundController.playButtonSound();
        vBox.setDisable(true);
        changePasswordMenu.setVisible(true);
        changePasswordMenu.setDisable(false);
        vBox.setEffect(new GaussianBlur());
    }


    public void removeUser(MouseEvent mouseEvent) throws IOException {
        User user = WelcomeController.getUser();
        for (int i = 0; i < User.getUsers().size(); i++) {
            User user1 = User.getUsers().get(i);
            if (user1.getUsername().equals(user.getUsername())) {
                SoundController.playRegisterSound();
                User.getUsers().remove(i);
                SaverAndLoader.saveAllUsers();
                break;
            }
        }
        WelcomeController.setUser(null);
        closeRemoveUser();
        back(mouseEvent);
    }

    public void closeRemoveUser() {
        SoundController.playButtonSound();
        vBox.setDisable(false);
        deleteAccountMenu.setVisible(false);
        deleteAccountMenu.setDisable(true);
        vBox.setEffect(null);
    }

    public void closeEditPassword() {
        SoundController.playButtonSound();
        vBox.setDisable(false);
        changePasswordMenu.setVisible(false);
        changePasswordMenu.setDisable(true);
        vBox.setEffect(null);
    }

    public void checkKeyboard(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            changePassword(keyEvent);
        } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
            back(keyEvent);
        }
    }

    public void changePassword(InputEvent event) throws IOException {
        String newPass = newPassword.getText();
        String oldPass = currentPassword.getText();
        if (newPass.length() == 0 || oldPass.length() == 0) {
            changePassLabel.setText("No empty fields!");
        } else {
            User user = User.getUserByUsername(WelcomeController.getUser().getUsername());
            if (user != null) {
                if (user.getPassword().equals(oldPass)) {
                    if (newPass.equals(oldPass)) {
                        changePassLabel.setText("Enter a new password!");
                    } else {
                        SoundController.playRegisterSound();
                        user.setPassword(newPass);
                        SaverAndLoader.saveAllUsers();
                        back(event);
                    }
                } else {
                    changePassLabel.setText("Current Password is Wrong!");
                }
            }
        }
    }

    public void back(InputEvent event) throws IOException {
        if (WelcomeController.getUser() != null)
            SoundController.playButtonSound();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneController.startWelcomeScene(stage);
    }
}
