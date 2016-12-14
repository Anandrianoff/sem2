package edu.lmu.cs.networking;

import javax.swing.*;

/**
 * Created by Andrey on 10.12.2016.
 */
public class Cell extends JPanel{

    public CellTypes getType() {
        return type;
    }

    public void setType(CellTypes type) {
        this.type = type;
        this.setBackground(type.getColor());
    }

    private CellTypes type;
    private boolean hasFirstPlayer;
    private boolean hasSecondPlayer;

    public boolean isHasFirstPlayer() {
        return hasFirstPlayer;
    }

    public void setHasFirstPlayer(boolean hasFirstPlayer) {
        this.hasFirstPlayer = hasFirstPlayer;
    }

    public boolean isHasSecondPlayer() {
        return hasSecondPlayer;
    }

    public void setHasSecondPlayer(boolean hasSecondPlayer) {
        this.hasSecondPlayer = hasSecondPlayer;
    }

    public Cell(CellTypes type) {
        this.type = type;
        this.setBackground(type.getColor());
        this.setVisible(true);
    }

    public Cell() {
        new Cell(CellTypes.UNKNOWN);
    }


}
