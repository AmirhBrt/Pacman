package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Map;
import model.User;
import view.WelcomeController;

import java.io.IOException;

import static control.StartGameController.showMap;

public class LoadMapController {
    private Map map;
    private User user;
    private int i = 0;

    @FXML
    private AnchorPane maze;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;

    public void setMap(Map map) {
        this.map = map;
    }

    @FXML
    private void initialize() {
        i = 0;
        this.user = User.getUserByUsername(WelcomeController.getUser().getUsername());
        assert user != null;
        setMap(user.getSelectedMaps().get(i));
        showMap(user.getSelectedMaps().get(i), maze, GameController.getCellWidth(), GameController.getCellHeight());
        if (user.getSelectedMaps().size() == 1) {
            prevButton.setDisable(true);
            nextButton.setDisable(true);
            prevButton.setVisible(false);
            nextButton.setVisible(false);
        } else {
            prevButton.setDisable(true);
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        SoundController.playRegisterSound();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        SceneController.startStartGameScene(stage , map);
    }

    public void loadMap(ActionEvent actionEvent) throws IOException {
        setMap(user.getSelectedMaps().get(i));
        back(actionEvent);
    }

    public void showNextMap() {
        SoundController.playButtonSound();
        i++;
        if (i == user.getSelectedMaps().size() - 1) {
            nextButton.setDisable(true);
        }
        prevButton.setDisable(false);
        maze.getChildren().clear();
        setMap(user.getSelectedMaps().get(i));
        showMap(map, maze, GameController.getCellWidth(), GameController.getCellWidth());
    }

    public void showPrevMap() {
        SoundController.playButtonSound();
        i--;
        if (i == 0) {
            prevButton.setDisable(true);
        }
        nextButton.setDisable(false);
        maze.getChildren().clear();
        setMap(user.getSelectedMaps().get(i));
        showMap(map, maze, GameController.getCellWidth(), GameController.getCellHeight());
    }
}
