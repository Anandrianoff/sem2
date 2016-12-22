package edu.lmu.cs.networking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A client for the TicTacToe game, modified and extended from the
 * class presented in Deitel and Deitel "Java How to Program" book.
 * I made a bunch of enhancements and rewrote large sections of the
 * code.  In particular I created the TTTP (Tic Tac Toe Protocol)
 * which is entirely text based.  Here are the strings that are sent:
 * <p>
 * Client -> Server           Server -> Client
 * ----------------           ----------------
 * MOVE <n>  (0 <= n <= 8)    WELCOME <char>  (char in {X, O})
 * QUIT                       VALID_MOVE
 * OTHER_PLAYER_MOVED <n>
 * VICTORY
 * DEFEAT
 * TIE
 * MESSAGE <text>
 */
public class TicTacToeClient {

    private String action;
    private JFrame frame = new JFrame("Minotavr");

    private JLabel messageLabel = new JLabel("");

    public ImageIcon getIcon() {
        return icon;
    }

    public ImageIcon getOpponentIcon() {
        return opponentIcon;
    }

    private ImageIcon icon;
    private ImageIcon opponentIcon;

    private Cell[][] cellBoard = new Cell[9][9];
    private Cell[][] cellBoardEnemy = new Cell[9][9];
    private Cell currentCell;

    private static int PORT = 8903;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Constructs the client by connecting to a server, laying out the
     * GUI and registering GUI listeners.
     */
    public TicTacToeClient(String serverAddress) throws Exception {

        icon = createImageIcon("pl2.png", "main player");
        opponentIcon = createImageIcon("pl1.png", "opponent player");

        // Setup networking
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Layout GUI
        JButton btnUp = new JButton("↑");
        JButton btnGo = new JButton("GO");
        JButton btnBomb = new JButton("BOMB");
//        JButton btnAsk = new JButton("ASK");
        JButton btnStay = new JButton("SKIP");
        JButton btnDown = new JButton("↓");
        JButton btnLeft = new JButton("←");
        JButton btnRight = new JButton("→");
        btnUp.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "up";
            }
        });
        btnGo.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "GO";
            }
        });
//        btnAsk.addMouseListener(new MouseAdapter() {
//            public void mousePressed(MouseEvent e) {
//                action = "ASK";
//            }
//        });
        btnDown.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "down";
            }
        });
        btnLeft.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "left";
            }
        });
        btnRight.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "right";
            }
        });
        btnStay.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "MOVE +0+0";
            }
        });
        btnUp.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "up";
            }
        });
        btnBomb.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "BOMB";
            }
        });


        messageLabel.setBackground(Color.lightGray);

        frame.getContentPane().add(messageLabel, "South");

        JPanel navPanel = new JPanel();
        navPanel.setSize(110, 370);
        navPanel.setBackground(Color.lightGray);
        navPanel.setLayout(new GridLayout(4, 0));
        navPanel.add(btnUp, "West");
        navPanel.add(btnRight, "West");
        navPanel.add(btnDown, "West");
        navPanel.add(btnLeft, "West");
        navPanel.add(btnGo, "West");
        navPanel.add(btnBomb, "West");
//        navPanel.add(btnAsk, "West");
        navPanel.add(btnStay, "West");
//        navPanel.setLayout(new GridLayout(9, 9, 4, 4));

        //main panel
        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.lightGray);
        boardPanel.setSize(390, 370);
        boardPanel.setLayout(new GridLayout(9, 9, 4, 4));
        //enemy panel
        JPanel enemyBoardPanel = new JPanel();
        enemyBoardPanel.setBackground(Color.lightGray);
        enemyBoardPanel.setSize(390, 370);
        enemyBoardPanel.setLayout(new GridLayout(9, 9, 4, 4));


        for (int i = 0; i < cellBoard.length; i++) {
            for (int j = 0; j < cellBoard.length; j++) {

//                    final int j = i;
                cellBoard[i][j] = new Cell();
                cellBoardEnemy[i][j] = new Cell();
                if (i == 4 & j == 4) {
                    cellBoard[i][j].setIcon(createImageIcon("shahid1.png", "main player"));
                    cellBoardEnemy[i][j].setIcon(createImageIcon("shahid2.png", "maindd player"));
                    boardPanel.add(cellBoard[i][j]);
                    enemyBoardPanel.add(cellBoardEnemy[i][j]);
                    continue;
                }

                    cellBoard[i][j].setIcon(createImageIcon("help1.png", "unknown"));
                    cellBoardEnemy[i][j].setIcon(createImageIcon("help1.png", "unknown"));
                    //                    cellBoard[i][j].addMouseListener(new MouseAdapter() {
//                public void mousePressed(MouseEvent e) {
//                    currentSquare = board[j];
//                    out.println("MOVE " + j);}});
                    boardPanel.add(cellBoard[i][j]);
                    enemyBoardPanel.add(cellBoardEnemy[i][j]);

            }
        }
        frame.getContentPane().add(boardPanel, "West");
        frame.getContentPane().add(navPanel, "Center");
        frame.getContentPane().add(enemyBoardPanel, "East");
    }

    /**
     * The main thread of the client will listen for messages
     * from the server.  The first message will be a "WELCOME"
     * message in which we receive our mark.  Then we go into a
     * loop listening for "VALID_MOVE", "OPPONENT_MOVED", "VICTORY",
     * "DEFEAT", "TIE", "OPPONENT_QUIT or "MESSAGE" messages,
     * and handling each message appropriately.  The "VICTORY",
     * "DEFEAT" and "TIE" ask the user whether or not to play
     * another game.  If the answer is no, the loop is exited and
     * the server is sent a "QUIT" message.  If an OPPONENT_QUIT
     * message is recevied then the loop will exit and the server
     * will be sent a "QUIT" message also.
     */
    public void play() throws Exception {
        String response;
        try {
            response = in.readLine();
            if (response.startsWith("WELCOME")) {
                String name = response.substring(8);

                frame.setTitle("Minotavr - Player " + name);
            }
            while (true) {
                response = in.readLine();
                if (response.startsWith("VALID_MOVE")) {
                    messageLabel.setText("Valid move, please wait");
                    currentCell.setIcon(icon);
                    currentCell.repaint();
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    int loc = Integer.parseInt(response.substring(15));
//                    cellBoard[0][0].setIcon();
//                    cellBoard[loc].repaint();
                    messageLabel.setText("Opponent moved, your turn");
                } else if (response.startsWith("VICTORY")) {
                    messageLabel.setText("You win");
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    messageLabel.setText("You lose");
                    break;
                } else if (response.startsWith("TIE")) {
                    messageLabel.setText("You tied");
                    break;
                } else if (response.startsWith("MESSAGE")) {
                    messageLabel.setText(response.substring(8));
                }
            }
            out.println("QUIT");
        } finally {
            socket.close();
        }
    }

    private boolean wantsToPlayAgain() {
        int response = JOptionPane.showConfirmDialog(frame,
                "Want to play again?",
                "Tic Tac Toe is Fun Fun Fun",
                JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }

    /**
     * Graphical square in the client window.  Each square is
     * a white panel containing.  A client calls setIcon() to fill
     * it with an Icon, presumably an X or O.
     */
//    static class Square extends JPanel {
//        JLabel label = new JLabel((Icon)null);
//
//        public Square() {
//            setBackground(Color.white);
//            add(label);
//        }
//
//        public void setIcon(Icon icon) {
//            label.setIcon(icon);
//        }
//    }

    /**
     * Runs the client as an application.
     */
    public static void main(String[] args) throws Exception {
        while (true) {
            //SMSSender.smsSend("Game started","79047640086");
            String serverAddress = (args.length == 0) ? "localhost" : args[1];
            TicTacToeClient client = new TicTacToeClient(serverAddress);
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.setSize(1300, 500);
            client.frame.setVisible(true);
            client.frame.setResizable(false);
            client.play();
            if (!client.wantsToPlayAgain()) {
                break;
            }
        }
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected ImageIcon createImageIcon(String path,
                                        String description) {

        java.net.URL imgURL = getClass().getClassLoader().getResource(path);//ClassLoader.getSystemResource(path);
        System.out.println(imgURL);
        if (imgURL != null) {
            //ImageIcon(this.getClass().getResource("/images/filename.png"));

            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


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
            new edu.lmu.cs.networking.Cell(CellTypes.UNKNOWN);
            add(label);
        }

        public void setIcon(Icon icon) {
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
}