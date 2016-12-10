package edu.lmu.cs.networking;

/**
 * Created by Andrey on 10.12.2016.
 */
public class Cell{

    private CellTypes type;

    public Cell(CellTypes type) {
        this.type = type;
    }

    public Cell() {
        this.type = CellTypes.UNKNOWN;
    }


}
