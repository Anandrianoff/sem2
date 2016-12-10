package edu.lmu.cs.networking;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andrey on 10.12.2016.
 */
public class MinotavrServer {

    public static volatile List<MinotavrGame> archive = new ArrayList<MinotavrGame>();

    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(8091);
        try{

            while (true){

                MinotavrGame game = new MinotavrGame();
                archive.add(game);

                Player player1 = new Player("p1", listener.accept());
                Player player2 = new Player("p2", listener.accept());
                player1.setOpponent(player2);
                player2.setOpponent(player1);
                Random rnd = new Random();
                int tmpX = rnd.nextInt(6);
                int tmpY = rnd.nextInt(6);
                while(!game.currentMap[tmpX][tmpY].equals(CellTypes.EMPTY)){
                    tmpX = rnd.nextInt(6);
                    tmpY = rnd.nextInt(6);
                }
                player1.setPositionX(tmpX);
                player1.setPositionY(tmpY);
                tmpX = rnd.nextInt(6);
                tmpY = rnd.nextInt(6);
                while(!game.currentMap[tmpX][tmpY].equals(CellTypes.EMPTY)){
                    tmpX = rnd.nextInt(6);
                    tmpY = rnd.nextInt(6);
                }
                player2.setPositionX(tmpX);
                player2.setPositionY(tmpY);
                game.setCurrentPlayer(player1);
                player1.start();
                player2.start();
            }

        }catch (Exception ex){
            System.out.println("Problem");

        }finally {
            listener.close();
        }
    }
}
