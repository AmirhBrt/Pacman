package control;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import view.WelcomeController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static control.StartGameController.showMap;

public class GameController {

    private static final double CELL_WIDTH = 50;
    private static final double CELL_HEIGHT = 50;

    private int startingLifeCounter;
    private int pacmanLifeCounter;
    private Map map;
    private Pacman pacman;
    private ArrayList<Ghost> allGhosts = new ArrayList<>();
    private boolean canPacmanMove = false;
    private boolean canGhostsMove = false;
    private boolean isPaused = false;
    private int second = 0;
    private int numberOfGhostsEaten = 0;
    private int coinAndBombNumber;
    private Timeline ghostTimeline;
    private Timeline pacmanTimeline;
    private Timeline intersectionTimeline;
    private final javax.swing.Timer timer = new javax.swing.Timer(1000, event -> {
        second++;
        System.out.println(second + " seconds passed from high mode.");
    });
    private int score = 0;

    @FXML
    private Label textToShow;
    @FXML
    private AnchorPane maze;
    @FXML
    private VBox lifeCounterVBox;
    @FXML
    private Label scoreField;
    @FXML
    private Button pauseButton;
    @FXML
    private Button restart;

    public static double getCellHeight() {
        return CELL_HEIGHT;
    }

    public static double getCellWidth() {
        return CELL_WIDTH;
    }

    public void setPacman(Pacman pacman) {
        this.pacman = pacman;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setPacmanLifeCounter(int pacmanLifeCounter) {
        this.pacmanLifeCounter = pacmanLifeCounter;
    }

    public void handleAction(Scene scene) {
        scene.setOnKeyPressed(event -> {
            Cell currentCell = (Cell) pacman.getParent();
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
                textToShow.setText(null);
                if (!canPacmanMove) {
                    canPacmanMove = true;
                    addDelay();
                }
                if (!currentCell.isHasTopBorder())
                    pacman.setDirection(Direction.UP);
            } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
                textToShow.setText(null);
                if (!canPacmanMove) {
                    canPacmanMove = true;
                    addDelay();
                }
                if (!currentCell.isHasBottomBorder())
                    pacman.setDirection(Direction.DOWN);
            } else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                textToShow.setText(null);
                if (!canPacmanMove) {
                    canPacmanMove = true;
                    addDelay();
                }
                if (!currentCell.isHasRightBorder())
                    pacman.setDirection(Direction.RIGHT);
            } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                textToShow.setText(null);
                if (!canPacmanMove) {
                    canPacmanMove = true;
                    addDelay();
                }
                if (!currentCell.isHasLeftBorder())
                    pacman.setDirection(Direction.LEFT);
            }
        });
    }

    public void init() {
        startingLifeCounter = pacmanLifeCounter;
        int rowNum = 17;
        int columnNum = 11;
        textToShow.setText("Press Any Key To Start!");
        showMap(map, maze, GameController.getCellWidth(), GameController.getCellHeight());
        initializePacmanPlace(rowNum, columnNum);
        addGhosts(rowNum, columnNum);
        addCoinAndBombToMap(map);
        for (int i = 0; i < pacmanLifeCounter; i++) {
            addHeart();
        }
        coinAndBombNumber = rowNum * columnNum - 5;
        addTimelines(rowNum, columnNum);
        addTimer(rowNum, columnNum);
        scoreField.setText(String.valueOf(score));
    }

    private void addHeart() {
        Heart heart = new Heart((int) CELL_WIDTH, (int) (CELL_HEIGHT * 0.8));
        lifeCounterVBox.getChildren().add(heart);
    }

    public void addDelay() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> canGhostsMove = true));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void addTimer(int rowNum, int columnNum) {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (second == 10) {
                    timer.stop();
                    for (Ghost ghost : allGhosts) {
                        ghost.setFill(new ImagePattern(ghost.getFirstImage()));
                    }
                    pacman.setHighMode(false);
                    second = 0;
                    numberOfGhostsEaten = 0;
                }
                if (coinAndBombNumber == 0) {
                    canGhostsMove = false;
                    canPacmanMove = false;
                    SoundController.playWinGameController();
                    initializePacmanPlace(rowNum, columnNum);
                    initializeGhostPlace(rowNum, columnNum);
                    addCoinAndBombToMap(map);
                    coinAndBombNumber = rowNum * columnNum - 5;
                    textToShow.setText("Press Any Key To Start!");
                }
            }
        };
        animationTimer.start();
    }

    private void handleIntersection() throws WasEatenException, EatGhostException {
        Bounds pacmanBounds = pacman.localToScene(pacman.getBoundsInLocal());
        for (Ghost ghost : allGhosts) {
            Bounds ghostBounds = ghost.localToScene(ghost.getBoundsInLocal());
            if (pacmanBounds.intersects(ghostBounds)) {
                if (!pacman.isHighMode()) {
                    if (ghost.canMove()) {
                        SoundController.playGameOver();
                        throw new WasEatenException();
                    }
                } else {
                    if (ghost.canMove()) {
                        SoundController.playEatGhostSound();
                        throw new EatGhostException(ghost);
                    }
                }
            }
        }
    }

    private void eatGhost(Ghost ghost) {
        Cell cell = (Cell) ghost.getParent();
        cell.getChildren().remove(ghost);
        Cell initializeCell = ghost.getInitializeCell();
        initializeCell.getChildren().add(ghost);
        ghost.setCanMove(false);
        numberOfGhostsEaten++;
        score += 200 * numberOfGhostsEaten;
        scoreField.setText(String.valueOf(score));
    }

    private void endGame() {
        canGhostsMove = false;
        canPacmanMove = false;
        pacmanTimeline.pause();
        ghostTimeline.pause();
    }

    private void eatCoinsAndBomb(Cell toGoCell) throws HighModeException {
        if (!toGoCell.getChildren().isEmpty()) {
            if (toGoCell.getChildren().get(0) instanceof Coin) {
                Coin coin = (Coin) toGoCell.getChildren().get(0);
                score += 5;
                scoreField.setText(String.valueOf(score));
                toGoCell.getChildren().remove(coin);
                coinAndBombNumber--;
                SoundController.playCoinSound();
            } else if (toGoCell.getChildren().get(0) instanceof EnergyBomb) {
                pacman.setHighMode(true);
                EnergyBomb energyBomb = (EnergyBomb) toGoCell.getChildren().get(0);
                toGoCell.getChildren().remove(energyBomb);
                coinAndBombNumber--;
                second = 0;
                SoundController.playBombSound();
                throw new HighModeException();
            }
        }
    }

    private void addTimelines(int rowNum, int columnNum) {
        pacmanTimeline = new Timeline(new KeyFrame(Duration.millis(300), event -> {
            if (canPacmanMove && !isPaused) {
                try {
                    movePacman();
                } catch (HighModeException highModeException) {
                    for (Ghost ghost : allGhosts)
                        ghost.setFill(new ImagePattern(ghost.getScaredImage()));
                    timer.start();
                }
            }
        }));
        pacmanTimeline.setCycleCount(Animation.INDEFINITE);
        pacmanTimeline.play();
        ghostTimeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {
            if (canGhostsMove && !isPaused)
                moveGhost();
        }));
        ghostTimeline.setCycleCount(Animation.INDEFINITE);
        ghostTimeline.play();
        intersectionTimeline = new Timeline(new KeyFrame(Duration.millis(100),
                event -> addIntersectionTimeline(rowNum, columnNum)));
        intersectionTimeline.setCycleCount(Animation.INDEFINITE);
        intersectionTimeline.play();
    }

    private void addIntersectionTimeline(int rowNum, int columnNum) {
        try {
            handleIntersection();
        } catch (WasEatenException e) {
            endGame();
            intersectionTimeline.pause();
            Timeline decreaseLifeCounter = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
                pacmanLifeCounter--;
                lifeCounterVBox.getChildren().remove(pacmanLifeCounter);
                if (pacmanLifeCounter == 0) {
                    textToShow.setText("Game Over!");
                    pauseButton.setDisable(true);
                    restart.setDisable(false);
                    restart.setVisible(true);
                    restart.setOnMouseClicked(event -> {
                        score = 0;
                        scoreField.setText(String.valueOf(score));
                        pacmanLifeCounter = startingLifeCounter;
                        for (int i = 0; i < pacmanLifeCounter; i++) {
                            addHeart();
                        }
                        textToShow.setText("Press Any Key To Start!");
                        pacmanTimeline.play();
                        ghostTimeline.play();
                        intersectionTimeline.play();
                        addCoinAndBombToMap(map);
                        pacman.setRotate(0);
                        pauseButton.setDisable(false);
                        restart.setDisable(true);
                        restart.setVisible(false);
                    });
                } else {
                    pacmanTimeline.play();
                    ghostTimeline.play();
                    intersectionTimeline.play();
                    textToShow.setText("Press Any Key To Start!");
                }
                initializePacmanPlace(rowNum, columnNum);
                initializeGhostPlace(rowNum, columnNum);
            }));
            decreaseLifeCounter.setCycleCount(1);
            decreaseLifeCounter.play();
        } catch (EatGhostException e) {
            eatGhost(e.getGhost());
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), ev -> e.getGhost().setCanMove(true)));
            timeline.setCycleCount(1);
            timeline.play();
        }
    }

    private void movePacman() throws HighModeException {
        Cell currentCell = (Cell) pacman.getParent();
        if (pacman.getDirection() == Direction.UP) {
            pacman.setRotate(-90);
            if (!currentCell.isHasTopBorder()) {
                Cell toGoCell = map.getMap()[currentCell.getxPlace() - 1][currentCell.getyPlace()];
                toGoCell.getChildren().add(pacman);
                currentCell.getChildren().remove(pacman);
                eatCoinsAndBomb(toGoCell);
            }
        } else if (pacman.getDirection() == Direction.DOWN) {
            pacman.setRotate(90);
            if (!currentCell.isHasBottomBorder()) {
                Cell toGoCell = map.getMap()[currentCell.getxPlace() + 1][currentCell.getyPlace()];
                toGoCell.getChildren().add(pacman);
                currentCell.getChildren().remove(pacman);
                eatCoinsAndBomb(toGoCell);
            }
        } else if (pacman.getDirection() == Direction.RIGHT) {
            pacman.setRotate(0);
            if (!currentCell.isHasRightBorder()) {
                Cell toGoCell = map.getMap()[currentCell.getxPlace()][currentCell.getyPlace() + 1];
                toGoCell.getChildren().add(pacman);
                currentCell.getChildren().remove(pacman);
                eatCoinsAndBomb(toGoCell);
            }
        } else if (pacman.getDirection() == Direction.LEFT) {
            pacman.setRotate(-180);
            if (!currentCell.isHasLeftBorder()) {
                Cell toGoCell = map.getMap()[currentCell.getxPlace()][currentCell.getyPlace() - 1];
                toGoCell.getChildren().add(pacman);
                currentCell.getChildren().remove(pacman);
                eatCoinsAndBomb(toGoCell);
            }
        }
    }

    private void moveGhost() {
        for (int i = 0; i < 4; i++) {
            Cell currentCell = (Cell) allGhosts.get(i).getParent();
            Collections.shuffle(allGhosts.get(i).getDirections());
            if (allGhosts.get(i).canMove())
                for (int j = 0; j < 4; j++) {
                    if (allGhosts.get(i).getDirections().get(j) == Direction.UP) {
                        if (!currentCell.isHasTopBorder()) {
                            Cell toGoCell = map.getMap()[currentCell.getxPlace() - 1][currentCell.getyPlace()];
                            toGoCell.getChildren().add(allGhosts.get(i));
                            currentCell.getChildren().remove(allGhosts.get(i));
                            break;
                        }
                    } else if (allGhosts.get(i).getDirections().get(j) == Direction.DOWN) {
                        if (!currentCell.isHasBottomBorder()) {
                            Cell toGoCell = map.getMap()[currentCell.getxPlace() + 1][currentCell.getyPlace()];
                            toGoCell.getChildren().add(allGhosts.get(i));
                            currentCell.getChildren().remove(allGhosts.get(i));
                            break;
                        }
                    } else if (allGhosts.get(i).getDirections().get(j) == Direction.RIGHT) {
                        if (!currentCell.isHasRightBorder()) {
                            Cell toGoCell = map.getMap()[currentCell.getxPlace()][currentCell.getyPlace() + 1];
                            toGoCell.getChildren().add(allGhosts.get(i));
                            currentCell.getChildren().remove(allGhosts.get(i));
                            break;
                        }
                    } else if (allGhosts.get(i).getDirections().get(j) == Direction.LEFT) {
                        if (!currentCell.isHasLeftBorder()) {
                            Cell toGoCell = map.getMap()[currentCell.getxPlace()][currentCell.getyPlace() - 1];
                            toGoCell.getChildren().add(allGhosts.get(i));
                            currentCell.getChildren().remove(allGhosts.get(i));
                            break;
                        }
                    }
                }
        }
    }

    private void addGhosts(int rowNum, int columnNum) {
        ArrayList<Image> allImages = new ArrayList<>();
        allImages.add(new Image(getClass().getResource("/images/green.gif").toExternalForm()));
        allImages.add(new Image(getClass().getResource("/images/gray.gif").toExternalForm()));
        allImages.add(new Image(getClass().getResource("/images/pink.gif").toExternalForm()));
        allImages.add(new Image(getClass().getResource("/images/orange.gif").toExternalForm()));
        Collections.shuffle(allImages);
        ArrayList<Ghost> ghosts = new ArrayList<>();
        ghosts.add(new Ghost(CELL_WIDTH * 0.8, CELL_HEIGHT * 0.8, allImages.get(0)));
        ghosts.add(new Ghost(CELL_WIDTH * 0.8, CELL_HEIGHT * 0.8, allImages.get(1)));
        ghosts.add(new Ghost(CELL_WIDTH * 0.8, CELL_HEIGHT * 0.8, allImages.get(2)));
        ghosts.add(new Ghost(CELL_WIDTH * 0.8, CELL_HEIGHT * 0.8, allImages.get(3)));
        for (int i = 0; i < 4; i++) {
            ghosts.get(i).setLayoutX(CELL_WIDTH / 8);
            ghosts.get(i).setLayoutY(CELL_HEIGHT / 8);
            ghosts.get(i).setWidth(CELL_WIDTH * 0.8);
            ghosts.get(i).setHeight(CELL_HEIGHT * 0.8);
        }
        allGhosts = ghosts;
        initializeGhostPlace(rowNum, columnNum);
    }

    private void addCoinAndBombToMap(Map map) {
        int bombX = new Random().nextInt(map.getMap().length) % map.getMap().length;
        int bomb1X = new Random().nextInt(map.getMap().length) % map.getMap().length;
        int bombY = new Random().nextInt(map.getMap()[0].length) % map.getMap()[0].length;
        int bomb1Y = new Random().nextInt(map.getMap()[0].length) % map.getMap()[0].length;
        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[0].length; j++) {
                Cell cell = (Cell) maze.getChildren().get(i * map.getMap()[0].length + j);
                if (cell.getChildren().isEmpty()) {
                    if (bombX == i && bombY == j) {
                        EnergyBomb energyBomb = new EnergyBomb((int) (CELL_WIDTH * 0.8), (int) (CELL_HEIGHT * 0.7));
                        energyBomb.setLayoutX(CELL_WIDTH / 16);
                        energyBomb.setLayoutY(CELL_HEIGHT / 16);
                        cell.getChildren().add(energyBomb);
                    } else if (bomb1X == i && bomb1Y == j) {
                        EnergyBomb energyBomb = new EnergyBomb((int) (CELL_WIDTH * 0.8), (int) (CELL_HEIGHT * 0.7));
                        energyBomb.setLayoutX(CELL_WIDTH / 16);
                        energyBomb.setLayoutY(CELL_HEIGHT / 16);
                        cell.getChildren().add(energyBomb);
                    } else {
                        Coin coin = new Coin(CELL_WIDTH / 8);
                        coin.setLayoutY(CELL_HEIGHT / 2);
                        coin.setLayoutX(CELL_WIDTH / 2);
                        cell.getChildren().add(coin);
                    }
                }
            }
        }
    }

    private void initializePacmanPlace(int rowNum, int columnNum) {
        Cell pane = (Cell) maze.getChildren().get(rowNum / 2 * columnNum + columnNum / 2);
        if (pacman.getParent() != null)
            ((Cell) pacman.getParent()).getChildren().remove(pacman);
        pane.getChildren().add(pacman);
        pacman.setLayoutX(CELL_WIDTH / 2);
        pacman.setLayoutY(CELL_HEIGHT / 2);
        pacman.setRotate(0);
    }

    private void initializeGhostPlace(int rowNum, int columnNum) {
        Cell cell = (Cell) maze.getChildren().get(0);
        cell.getChildren().remove(allGhosts.get(0));
        cell.getChildren().add(allGhosts.get(0));
        allGhosts.get(0).setInitializeCell(cell);
        cell = (Cell) maze.getChildren().get(rowNum - 1);
        cell.getChildren().remove(allGhosts.get(1));
        cell.getChildren().add(allGhosts.get(1));
        allGhosts.get(1).setInitializeCell(cell);
        cell = (Cell) maze.getChildren().get((columnNum - 1) * rowNum);
        cell.getChildren().remove(allGhosts.get(2));
        cell.getChildren().add(allGhosts.get(2));
        allGhosts.get(2).setInitializeCell(cell);
        cell = (Cell) maze.getChildren().get((columnNum - 1) * rowNum + rowNum - 1);
        cell.getChildren().remove(allGhosts.get(3));
        cell.getChildren().add(allGhosts.get(3));
        allGhosts.get(3).setInitializeCell(cell);
    }

    @FXML
    private void pauseGame() {
        if (isPaused) {
            pauseButton.setText("Pause");
        } else {
            pauseButton.setText("Resume");
        }
        isPaused = !isPaused;
    }

    @FXML
    private void exit(MouseEvent mouseEvent) throws IOException {
        if (WelcomeController.getUser() != null) {
            User user = User.getUserByUsername(WelcomeController.getUser().getUsername());
            if (user != null) {
                if (user.getMaxPoint() < score) {
                    user.setMaxPoint(score);
                    SaverAndLoader.saveAllUsers();
                }
            }
        }
        back(mouseEvent);
    }

    private void back(Event event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneController.startWelcomeScene(stage);
    }
}