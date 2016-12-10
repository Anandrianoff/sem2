package edu.lmu.cs.networking;

import java.awt.*;

/**
 * Created by Andrey on 10.12.2016.
 */
public enum CellTypes {
    BRICK(Color.ORANGE), GRANITE(Color.GRAY), UNKNOWN_WALL(Color.DARK_GRAY), EMPTY(Color.GREEN),
    UNKNOWN(Color.WHITE);

    public Color getColor() {
        return color;
    }

    private Color color;

    CellTypes(Color color) {
        this.color = color;
    }
}
