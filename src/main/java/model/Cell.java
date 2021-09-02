package model;

import javafx.scene.layout.Pane;

public class Cell extends Pane{
    private final int xPlace;
    private final int yPlace;
    private boolean hasTopBorder;
    private boolean hasBottomBorder;
    private boolean hasRightBorder;
    private boolean hasLeftBorder;
    private boolean isVisited;


    public Cell(int xPlace, int yPlace) {
        super();
        this.hasBottomBorder = true;
        this.hasLeftBorder = true;
        this.hasRightBorder = true;
        this.hasTopBorder = true;
        this.xPlace = xPlace;
        this.yPlace = yPlace;
        this.isVisited = false;
    }

    public boolean isHasBottomBorder() {
        return hasBottomBorder;
    }

    public boolean isHasLeftBorder() {
        return hasLeftBorder;
    }

    public boolean isHasRightBorder() {
        return hasRightBorder;
    }

    public boolean isHasTopBorder() {
        return hasTopBorder;
    }

    public void setHasBottomBorder(boolean hasBottomBorder) {
        this.hasBottomBorder = hasBottomBorder;
    }

    public void setHasLeftBorder(boolean hasLeftBorder) {
        this.hasLeftBorder = hasLeftBorder;
    }

    public void setHasRightBorder(boolean hasRightBorder) {
        this.hasRightBorder = hasRightBorder;
    }

    public void setHasTopBorder(boolean hasTopBorder) {
        this.hasTopBorder = hasTopBorder;
    }

    public int getxPlace() {
        return xPlace;
    }

    public int getyPlace() {
        return yPlace;
    }

    public boolean isVisited() {
        return !isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
