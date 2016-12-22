package edu.lmu.cs.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A two-player game.
 */
public class Game {

    static final Cell[][] board1 = {{new Cell(CellTypes.BRICK), new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY), new Cell(CellTypes.BRICK), new Cell(CellTypes.EMPTY)},
            {new Cell(CellTypes.BRICK), new Cell(CellTypes.GRANITE), new Cell(CellTypes.EMPTY), new Cell(CellTypes.GRANITE), new Cell(CellTypes.BRICK)},
            {new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY), new Cell(CellTypes.GRANITE), new Cell(CellTypes.EMPTY)},
            {new Cell(CellTypes.EMPTY), new Cell(CellTypes.BRICK), new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY)},
            {new Cell(CellTypes.GRANITE), new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY), new Cell(CellTypes.EMPTY), new Cell(CellTypes.BRICK)}};

    /**
     * A board has nine squares.  Each square is either unowned or
     * it is owned by a player.  So we use a simple array of player
     * references.  If null, the corresponding square is unowned,
     * otherwise the array cell stores a reference to the player that
     * owns it.
     */
    private Player[] board = {
            null, null, null,
            null, null, null,
            null, null, null};

    /**
     * The current player.
     */
    Player currentPlayer;

    /**
     * Returns whether the current state of the board is such that one
     * of the players is a winner.
     * TODO
     */
    public boolean hasWinner(int shX, int shY) {
        return false;

    }

    /**
     * Returns whether there are no more empty squares.
     */
//    public boolean boardFilledUp() {
//        for (int i = 0; i < board.length; i++) {
//            if (board[i] == null) {
//                return false;
//            }
//        }
//        return true;
//    }

    /**
     * Called by the player threads when a player tries to make a
     * move.  This method checks to see if the move is legal: that
     * is, the player requesting the move must be the current player
     * and the square in which she is trying to move must not already
     * be occupied.  If the move is legal the game state is updated
     * (the square is set and the next player becomes current) and
     * the other player is notified of the move so it can update its
     * client.
     */
    @Deprecated
    public synchronized boolean legalMove(int location, Player player) {
        if (player == currentPlayer && board[location] == null) {
            board[location] = currentPlayer;
            currentPlayer = currentPlayer.opponent;
            // currentPlayer.otherPlayerMoved(location);
            /*
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
            return true;
        }
        return false;
    }

    public synchronized boolean canMoveHere(int shX, int shY, Player player) {
        if (player == currentPlayer && board1[currentPlayer.getRealPositionX() + shX][currentPlayer.getRealPositionY() + shY].getType().equals(CellTypes.EMPTY)) {
            currentPlayer = currentPlayer.opponent;
            currentPlayer.otherPlayerMoved(currentPlayer.getRealPositionX() + shX, currentPlayer.getRealPositionY() + shY);
        }
        return false;
    }

    /**
     * The class for the helper threads in this multithreaded server
     * application.  A Player is identified by a character mark
     * which is either 'X' or 'O'.  For communication with the
     * client the player has a socket with its input and output
     * streams.  Since only text is being communicated we use a
     * reader and a writer.
     */
    class Player extends Thread {
        String name;
        Player opponent;
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        int realPositionX;
        int realPositionY;

        int shiftX;
        int shiftY;

        public int getRealPositionX() {
            return realPositionX;
        }

        public void setRealPositionX(int realPositionX) {
            this.realPositionX = realPositionX;
        }

        public int getRealPositionY() {
            return realPositionY;
        }

        public void setRealPositionY(int realPositionY) {
            this.realPositionY = realPositionY;
        }

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, String name, int realPositionX, int realPositionY) {
            this.socket = socket;
            this.name = name;
            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME " + name);
                output.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        /**
         * Accepts notification of who the opponent is.
         */
        public void setOpponent(Player opponent) {
            this.opponent = opponent;
        }

        /**
         * Handles the otherPlayerMoved message.
         */
        public void otherPlayerMoved(int realPositionX, int realPositionY) {
            output.println("OPPONENT_MOVED " + realPositionX + realPositionY);
            // TODO: 15.12.2016  
//            output.println(
//                    hasWinner() ? "DEFEAT" : boardFilledUp() ? "TIE" : "");
        }

        /**
         * The run method of this thread.
         */
        public void run() {
            try {
                // The thread is only started after everyone connects.
                output.println("MESSAGE All players connected");

                // Tell the first player that it is her turn.
                if (name == "pl1") {
                    output.println("MESSAGE Your move");
                }

                // Repeatedly get commands from the client and process them.
                while (true) {
                    //// TODO: 15.12.2016
                    String command = input.readLine();
                    if (command.startsWith("MOVE")) {
                        shiftX = Integer.parseInt(command.substring(5));
                        shiftY = Integer.parseInt(command.substring(6));
                        if (canMoveHere(shiftX, shiftY, this)) {
                            //на клиенте нужно запомнить куда он пытался ходить
                            output.println("VALID_MOVE");
                            realPositionX += shiftX;
                            realPositionY += shiftY;
                            currentPlayer.opponent.output.println("OPPONENT "+((shiftX > 0)? "+" + shiftX: shiftX) + ((shiftY > 0)? "+" + shiftY: shiftY)+
                                    " ? ? ?");
                        }
                    } else if (command.startsWith("QUIT")) {
                        return;
                    }

                    if (command.startsWith("BOMB")) {
                        shiftX = Integer.parseInt(command.substring(5));
                        shiftY = Integer.parseInt(command.substring(6));
                        if (hasWinner(currentPlayer.realPositionX + shiftX, currentPlayer.realPositionY + shiftY)) {
                            output.println("WIN");
                            currentPlayer.opponent.output.println("DEFEAT");
                        } else {

                            if (board1[currentPlayer.getRealPositionX() + shiftX][currentPlayer.getRealPositionY() + shiftY].getType().equals(CellTypes.GRANITE)) {
                                output.println("BOMBED " + "g");
                                currentPlayer.opponent.output.println("OPPONENT ? "+((shiftX > 0)? "+" + shiftX: shiftX) + ((shiftY > 0)? "+" + shiftY: shiftY)+
                                        " ? ?");
                            }
                            if (board1[currentPlayer.getRealPositionX() + shiftX][currentPlayer.getRealPositionY() + shiftY].getType().equals(CellTypes.EMPTY)) {
                                output.println("BOMBED " + "e");
                                currentPlayer.opponent.output.println("OPPONENT ? ? "+((shiftX > 0)? "+" + shiftX: shiftX) + ((shiftY > 0)? "+" + shiftY: shiftY)+
                                        " ?");
                            }
                        }
                    }

                    if (command.startsWith("ASK")) {
                        shiftX = Integer.parseInt(command.substring(4));
                        shiftY = Integer.parseInt(command.substring(5));
                        if (board1[currentPlayer.getRealPositionX() + shiftX][currentPlayer.getRealPositionY() + shiftY].getType().equals(CellTypes.GRANITE) ||
                                board1[currentPlayer.getRealPositionX() + shiftX][currentPlayer.getRealPositionY() + shiftY].getType().equals(CellTypes.BRICK)) {
                            //u -uknown cell
                            // e-empty cell
                            output.println("ASK " + "u");
                            currentPlayer.opponent.output.println("OPPONENT ? ? ? "+((shiftX > 0)? "+" + shiftX: shiftX) + ((shiftY > 0)? "+" + shiftY: shiftY));
                        }
                        if (board1[currentPlayer.getRealPositionX() + shiftX][currentPlayer.getRealPositionY() + shiftY].getType().equals(CellTypes.EMPTY)) {
                            output.println("ASK " + "e");
                            currentPlayer.opponent.output.println("OPPONENT ? ?"+((shiftX > 0)? "+" + shiftX: shiftX) + ((shiftY > 0)? "+" + shiftY: shiftY)+
                                    " ?");
                        }

                    }
                }

            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Override
    public String toString() {
        if (currentPlayer != null) {
            return currentPlayer.name + " has won";
        } else {
            return "game in progress";
        }

    }
}