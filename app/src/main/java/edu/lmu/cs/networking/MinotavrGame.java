package edu.lmu.cs.networking;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.*;
import java.util.List;

/**
 * Created by Andrey on 10.12.2016.
 */
public class MinotavrGame {

    static List<Cell[][]> maps;

    static final Cell[][] board1 = {{new Cell(CellTypes.BRICK), new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY), new Cell(CellTypes.BRICK), new Cell(CellTypes.EMPTY)},
            {new Cell(CellTypes.BRICK), new Cell(CellTypes.GRANITE), new Cell(CellTypes.EMPTY), new Cell(CellTypes.GRANITE), new Cell(CellTypes.BRICK)},
            {new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY), new Cell(CellTypes.GRANITE), new Cell(CellTypes.EMPTY)},
            {new Cell(CellTypes.EMPTY), new Cell(CellTypes.BRICK), new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY)},
            {new Cell(CellTypes.GRANITE), new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY), new Cell(CellTypes.BRICK)}};

    static final Cell[][] board2 = {{new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY)},
        {new Cell(CellTypes.BRICK), new Cell(CellTypes.GRANITE), new Cell(CellTypes.GRANITE), new Cell(CellTypes.GRANITE),new Cell(CellTypes.BRICK)},
        {new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY),new Cell(CellTypes.GRANITE),new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY)},
        {new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY),new Cell(CellTypes.GRANITE),new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY)},
        {new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY),new Cell(CellTypes.GRANITE),new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY)}};
    private Player currentPlayer;

    static final Cell[][] board3 = {{new Cell(CellTypes.GRANITE), new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY),new Cell(CellTypes.GRANITE)},
            {new Cell(CellTypes.EMPTY), new Cell(CellTypes.BRICK), new Cell(CellTypes.EMPTY), new Cell(CellTypes.BRICK), new Cell(CellTypes.EMPTY)},
            {new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY),new Cell(CellTypes.GRANITE),new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY)},
            {new Cell(CellTypes.EMPTY), new Cell(CellTypes.BRICK), new Cell(CellTypes.EMPTY), new Cell(CellTypes.BRICK), new Cell(CellTypes.EMPTY)},
            {new Cell(CellTypes.GRANITE), new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY),new Cell(CellTypes.EMPTY),new Cell(CellTypes.GRANITE)}};

    Cell[][] currentMap;

    public MinotavrGame() {
        maps= new ArrayList<Cell[][]>();
        maps.add(board1);
        maps.add(board2);
        maps.add(board3);
        currentMap = maps.get((new Random()).nextInt(maps.size()));
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public synchronized boolean canGoThere(int x, int y, Player player){

        return true;
    }



}
