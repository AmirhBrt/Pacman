package model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;


public class Ghost extends Rectangle {

    private final Image scaredImage;
    private final Image firstImage;
    private final ArrayList<Direction> directions;
    private Cell initializeCell;
    private boolean canMove;

    public Ghost(double width, double height, Image firstImage) {
        super((int) width , (int) height);
        super.setFill(new ImagePattern(firstImage));
        this.firstImage = firstImage;
        this.directions = new ArrayList<>();
        directions.add(Direction.UP);
        directions.add(Direction.RIGHT);
        directions.add(Direction.LEFT);
        directions.add(Direction.DOWN);
        this.scaredImage = new Image(getClass().getResource("/images/blue.gif").toExternalForm());
        canMove = true;
    }

    public void setInitializeCell(Cell initializeCell) {
        this.initializeCell = initializeCell;
    }

    public Cell getInitializeCell() {
        return initializeCell;
    }

    public ArrayList<Direction> getDirections() {
        return directions;
    }

    public Image getFirstImage() {
        return firstImage;
    }

    public Image getScaredImage() {
        return scaredImage;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean canMove(){
        return canMove;
    }
}
