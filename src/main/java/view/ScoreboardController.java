package view;

import control.SceneController;
import control.SoundController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.Comparator;

public class ScoreboardController {

    @FXML
    private VBox vBox;
    @FXML
    private Label noUser;

    @FXML
    private void initialize() {
        if (User.getUsers().size() != 0) {
            noUser.setText("Ranking\t\tUsername\t\tMax Point");
            vBox.getChildren().clear();
            User.getUsers().sort(Comparator.comparingInt(User::getMaxPoint).reversed());
            int counter = 1;
            for (int i = 0; i < User.getUsers().size(); i++) {
                if (i < 10) {
                    User user = User.getUsers().get(i);
                    Label label = new Label();
                    label.setText(counter + "\t\t\t" + user.getUsername() + "\t\t\t" + user.getMaxPoint());
                    label.setAlignment(Pos.CENTER);
                    vBox.getChildren().add(label);
                    vBox.setSpacing(10);
                    if (i < User.getUsers().size() - 1) {
                        if (user.getMaxPoint() > User.getUsers().get(i + 1).getMaxPoint()) {
                            counter++;
                        }
                    }
                } else {
                    break;
                }
            }
        }
    }

    public void backToWelcome(InputEvent event) throws IOException {
        SoundController.playButtonSound();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneController.startWelcomeScene(stage);
    }
}
