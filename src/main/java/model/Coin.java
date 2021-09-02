package model;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Coin extends Circle {

    public Coin(double radius) {
        super(radius , Color.valueOf("#FFC107"));
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(0.5);
        dropShadow.setOffsetY(0.5);
        super.setEffect(dropShadow);
    }

}
