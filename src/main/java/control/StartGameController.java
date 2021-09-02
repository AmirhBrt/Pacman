package control;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Map;
import model.User;
import view.WelcomeController;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class StartGameController {

    private Map map;

    @FXML
    private Button saveMapButton;
    @FXML
    private Button loadMapButton;
    @FXML
    private VBox vBox;
    @FXML
    private Label textToShowToUser;
    @FXML
    private AnchorPane maze;
    @FXML
    private Spinner<Integer> spinner;

    public static void showMap(Map map, AnchorPane maze, double width, double height) {
        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[0].length; j++) {
                map.getMap()[i][j].setPrefWidth(width);
                map.getMap()[i][j].setPrefHeight(height);
                map.getMap()[i][j].setMinWidth(width);
                map.getMap()[i][j].setMinHeight(height);
                StringBuilder style = new StringBuilder("-fx-border-color: White; -fx-background-color: #232e6d; -fx-border-width: ");
                if (map.getMap()[i][j].isHasTopBorder()) {
                    style.append("2 ");
                } else {
                    style.append("0 ");
                }
                if (map.getMap()[i][j].isHasRightBorder()) {
                    style.append("2 ");
                } else {
                    style.append("0 ");
                }
                if (map.getMap()[i][j].isHasBottomBorder()) {
                    style.append("2 ");
                } else {
                    style.append("0 ");
                }
                if (map.getMap()[i][j].isHasLeftBorder()) {
                    style.append("2;");
                } else {
                    style.append("0;");
                }
                map.getMap()[i][j].setStyle(style.toString());
                maze.getChildren().add(map.getMap()[i][j]);
                map.getMap()[i][j].setLayoutX(j * width);
                map.getMap()[i][j].setLayoutY(i * height);
            }
        }
    }

    public void setMap(Map map){
        this.map = map;
    }

    public void init() {
        maze.getChildren().clear();
        removeIfNoUser();
        showMap(map, maze, GameController.getCellWidth(), GameController.getCellHeight());
    }

    private void removeIfNoUser() {
        if (WelcomeController.getUser() == null) {
            vBox.getChildren().remove(loadMapButton);
            vBox.getChildren().remove(saveMapButton);
        } else {
            User user = User.getUserByUsername(WelcomeController.getUser().getUsername());
            assert user != null;
            if (user.getSelectedMaps().size() == 0){
                loadMapButton.setDisable(true);
            }
        }
    }

    public void createNewMaze() {
        SoundController.playButtonSound();
        maze.getChildren().clear();
        setMap(new Map(17, 11));
        showMap(map, maze, GameController.getCellWidth(), GameController.getCellHeight());
    }

    public void back(ActionEvent mouseEvent) throws IOException {
        SoundController.playButtonSound();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        SceneController.startWelcomeScene(stage);
    }

    public void saveMap() throws IOException {
        String textToShow = null;
        User user = User.getUserByUsername(WelcomeController.getUser().getUsername());
        if (user != null) {
            if (!user.getSelectedMaps().contains(map)) {
                SoundController.playButtonSound();
                user.addMap(map);
                SaverAndLoader.saveAllUsers();
                loadMapButton.setDisable(false);
                textToShow = "Map Saved!";
            } else {
                SoundController.playRefuseSound();
                textToShow = "This Map is Already Saved!";
            }
        }
        Timer timer = new Timer();
        textToShowToUser.setText(textToShow);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> textToShowToUser.setText(null));
            }
        };
        timer.schedule(timerTask, 2000);
    }

    public void openLoadMapController(MouseEvent mouseEvent) throws IOException {
        SoundController.playButtonSound();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        SceneController.startLoadMapScene(stage , map );
    }

    public void startGame(ActionEvent actionEvent) throws IOException {
        SoundController.playButtonSound();
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        SceneController.startGameController(stage , spinner.getValue() , map);
    }

}