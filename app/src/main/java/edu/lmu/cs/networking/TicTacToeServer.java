package edu.lmu.cs.networking;



import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * A server for a network multi-player tic tac toe game.  Modified and
 * extended from the class presented in Deitel and Deitel "Java How to
 * Program" book.  I made a bunch of enhancements and rewrote large sections
 * of the code.  The main change is instead of passing *data* between the
 * client and server, I made a TTTP (tic tac toe protocol) which is totally
 * plain text, so you can test the game with Telnet (always a good idea.)
 * The strings that are sent in TTTP are:
 *
 *  Client -> Server           Server -> Client
 *  ----------------           ----------------
 * SKIP
 * BOMB+
 *  ASK
 * GO
 * MOVE <n>  (0 <= n <= 8)    WELCOME <char>  (char in {X, O})
 *  QUIT +
 *  VALID_MOVE                  BOMBED(GRANITE EMPTY)
 *                             OTHER_PLAYER_MOVED <n>
 *                             VICTORY
 *                             DEFEAT
 *                            ////////// TIE-OUT IF USAGE!!!
 *                             MESSAGE <text>
 *
 *                              OPPONENT (координаты сдвига х,у)[+0-1] (координаты гранитных стен) [+0-1](координаты пустых)[+0-1]
 *                              (координата неизвестной стены) [+0-1]
 *
 *
 * A second change is that it allows an unlimited number of pairs of
 * players to play.
 */
public class TicTacToeServer {
//    static Logger log = Logger.getLogger(TicTacToeServer.class);
    public static volatile List<Game> gamesArchive = new ArrayList<Game>(){};

    /**
     * Runs the application. Pairs up clients that connect.
     */
    public static void main(String[] args) throws Exception {

        ServerSocket listener = new ServerSocket(8903);
        System.out.println("Minotavr game is Running");
        try {
            while (true) {
                Game game = new Game();
                gamesArchive.add(game);
                Game.Player player1 = game.new Player(listener.accept(), "player1",2,3);
                Game.Player player2 = game.new Player(listener.accept(), "player2",4,0);
                player1.setOpponent(player2);
                player2.setOpponent(player1);
                game.currentPlayer = player1;
                player1.start();
                player2.start();
            }
        } finally {
//            log.info("Listener closes");
            listener.close();
        }
    }
}

