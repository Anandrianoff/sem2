package edu.lmu.cs.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Andrey on 10.12.2016.
 */
public class Player extends Thread{
    String name;
    Player opponent;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    int positionX;
    int positionY;

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
        try{
            input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());
            output.println("WELCOME " + name);
            output.println("MESSAGE waiting your opponent");

        }catch (IOException ex){
            System.out.println("Die");

        }

    }

    @Override
    public void run() {
        super.run();
    }
}
