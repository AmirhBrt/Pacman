package control;

import model.Ghost;

public class HighModeException extends Exception{

    public HighModeException(){
        super();
    }

}

class EatGhostException extends HighModeException{

    private final Ghost ghost;
    public EatGhostException(Ghost ghost){
        super();
        this.ghost = ghost;
    }

    public Ghost getGhost() {
        return ghost;
    }
}

class WasEatenException extends Exception{

    public WasEatenException(){
        super();
    }

}