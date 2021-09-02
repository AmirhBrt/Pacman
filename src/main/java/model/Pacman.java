package model;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Pacman extends Circle {

    private Direction direction;
    private boolean isHighMode;

    public Pacman(double radius) {
        super(radius);
        super.setFill(new ImagePattern(new Image(getClass().getResource("/images/pacman.gif").toExternalForm())));
        super.setEffect(new DropShadow(5, Color.BLACK));
        this.direction = null;
        isHighMode = false;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setHighMode(boolean highMode) {
        isHighMode = highMode;
    }

    public boolean isHighMode() {
        return isHighMode;
    }

}
