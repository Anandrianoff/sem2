package edu.lmu.cs.networking;

import javax.swing.*;


public class Cell extends JPanel {


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

    JLabel label = new JLabel((Icon) null);

    public Cell() {
        new Cell(CellTypes.UNKNOWN);
        add(label);
    }

    public void setIcon(ImageIcon icon) {
        label.setIcon(icon);
    }

    public Cell(CellTypes type) {
        this.type = type;
        this.setBackground(type.getColor());
        this.setVisible(true);
    }

//    public Cell() {
//        new Cell(CellTypes.UNKNOWN);
//    }


}

