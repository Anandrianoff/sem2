package edu.lmu.cs.networking;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

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
    private String shiftDirection;
    private JFrame frame = new JFrame("Minotavr");

    private JLabel messageLabel = new JLabel("");
    JPanel navPanel;

    JButton btnUp;
    JButton btnGo;
    JButton btnBomb;
    JButton btnSkip;
    JButton btnDown;
    JButton btnLeft;
    JButton btnRight;

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
    private Cell currentOppenetCell;

    private int clientPositionX = 4;
    private int clientPositionY = 4;

    private int opponentPositionX = 4;
    private int opponentPositionY = 4;

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
        btnUp = new JButton("↑");
        btnGo = new JButton("GO");
        btnBomb = new JButton("BOMB");
//        JButton btnAsk = new JButton("ASK");
        btnSkip = new JButton("SKIP");
        btnDown = new JButton("↓");
        btnLeft = new JButton("←");
        btnRight = new JButton("→");
//        btnUp.addMouseListener(new MouseAdapter() {
//            public void mousePressed(MouseEvent e) {
//                action = "up";
//            }
//        });
        btnGo.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "MOVE ";
            }
        });


        //0-1 x y  --> -y +x
        btnDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shiftDirection = MoveParser.intToString(0, -1);
                out.println(action + shiftDirection);
            }
        });

        // -1 0
        btnLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shiftDirection = MoveParser.intToString(-1, 0);
                out.println(action + shiftDirection);
            }
        });
        //1 0
        btnRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shiftDirection = MoveParser.intToString(1, 0);
                out.println(action + shiftDirection);
            }
        });
        btnSkip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shiftDirection = "+0+0";
                out.println("MOVE " + shiftDirection);
            }
        });
        btnUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shiftDirection = MoveParser.intToString(0, 1);
                System.out.println(action + shiftDirection);
                out.println(action + shiftDirection);
            }
        });
        btnBomb.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "BOMB ";
            }
        });


        messageLabel.setBackground(Color.lightGray);

        frame.getContentPane().add(messageLabel, "South");

        navPanel = new JPanel();
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
        navPanel.add(btnSkip, "West");

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
        currentCell = cellBoard[4][4];
        currentOppenetCell = cellBoardEnemy[4][4];
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
                if (name.contains("2")) {
                    btnUp.setEnabled(false);
                    btnDown.setEnabled(false);
                    btnLeft.setEnabled(false);
                    btnRight.setEnabled(false);
                    btnSkip.setEnabled(false);
                }
            }
            while (true) {
                response = in.readLine();
                if (response.startsWith("VALID_MOVE")) {
                    messageLabel.setText("Valid move, please wait");
//                    btnUp.setIcon(createImageIcon("bomb.png", "hyi"));
                    btnUp.setEnabled(false);
                    btnDown.setEnabled(false);
                    btnLeft.setEnabled(false);
                    btnRight.setEnabled(false);
                    btnSkip.setEnabled(false);
                    clientPositionX += -MoveParser.parseYToInt(shiftDirection);
                    clientPositionY += MoveParser.parseXToInt(shiftDirection);

                    currentCell.setIcon(createImageIcon("epty.gif", "dfwef"));
                    currentCell.repaint();
                    currentCell = cellBoard[clientPositionX][clientPositionY];
                    currentCell.setIcon(createImageIcon("shahid1.png", "seres"));
                    currentCell.repaint();
                    // OPPONENT (координаты сдвига х,у)[+0-1] (координаты гранитных стен) [+0-1](координаты пустых)[+0-1]
                    //          (координата неизвестной стены) [+0-1]
                } else if (response.startsWith("OPPONENT")) {
                    String[] parampampam = response.substring(9).split(" ");
//                    for (String s : parampampam){
//                        System.out.println(s);
//                    }

                    if (!parampampam[0].contains("?")) {
                        int tmpX = 0;
                        if (parampampam[0].charAt(0) == '+')
                            tmpX += Integer.parseInt(String.valueOf(parampampam[0].charAt(1)));
                        else
                            tmpX += -1 * Integer.parseInt(String.valueOf(parampampam[0].charAt(1)));

                        int tmpY = 0;
                        if (parampampam[0].charAt(2) == '+')
                            tmpY += Integer.parseInt(String.valueOf(parampampam[0].charAt(3)));
                        else
                            tmpY += -1 * Integer.parseInt(String.valueOf(parampampam[0].charAt(3)));
                        String opponentMove = ((tmpX >= 0) ? "+" + tmpX : "" + tmpX) + ((tmpY >= 0) ? "-" + tmpY : "+" + (-tmpY));
//                            MoveParser.convert(tmpX, tmpY);


                        int ublX = -tmpY;
                        int ublY = tmpX;
//                    if (opponentMove.charAt(0) == '+')
//                        ublX+= Integer.parseInt(String.valueOf(parampampam[0].charAt(1)));
//                    else
//                        ublX += -1 * Integer.parseInt(String.valueOf(parampampam[0].charAt(1)));
//
//                    int ublY= 0;
//                    if (opponentMove.charAt(2) == '+')
//                       ublY+= Integer.parseInt(String.valueOf(parampampam[0].charAt(3)));
//                    else
//                        ublY += -1 * Integer.parseInt(String.valueOf(parampampam[0].charAt(3)));
                        opponentPositionX += ublX;
                        opponentPositionY += ublY;

                        currentOppenetCell.setIcon(createImageIcon("epty.gif", "dfwef"));
                        currentOppenetCell.repaint();
                        currentOppenetCell = cellBoardEnemy[opponentPositionX][opponentPositionY];
                        currentOppenetCell.setIcon(createImageIcon("shahid2.png", "seres"));
                        currentOppenetCell.repaint();


//                    int loc = Integer.parseInt(response.substring(15));
////                    cellBoard[0][0].setIcon();
////                    cellBoard[loc].repaint();
                        btnUp.setEnabled(true);
                        btnDown.setEnabled(true);
                        btnLeft.setEnabled(true);
                        btnRight.setEnabled(true);
                        btnSkip.setEnabled(true);
                        messageLabel.setText("Opponent moved, your turn");
                    } else if (!parampampam[parampampam.length - 1].contains("?")) {
                        //// TODO: 23.12.2016
                        int tmpX = 0;
                        if (parampampam[parampampam.length - 1].charAt(0) == '+')
                            tmpX += Integer.parseInt(String.valueOf(parampampam[parampampam.length - 1].charAt(1)));
                        else
                            tmpX += -1 * Integer.parseInt(String.valueOf(parampampam[parampampam.length - 1].charAt(1)));

                        int tmpY = 0;
                        if (parampampam[parampampam.length - 1].charAt(2) == '+')
                            tmpY += Integer.parseInt(String.valueOf(parampampam[parampampam.length - 1].charAt(3)));
                        else
                            tmpY += -1 * Integer.parseInt(String.valueOf(parampampam[parampampam.length - 1].charAt(3)));
                        String opponentMove = ((tmpX >= 0) ? "+" + tmpX : "" + tmpX) + ((tmpY >= 0) ? "-" + tmpY : "+" + (-tmpY));

                        int ublX = -tmpY;
                        int ublY = tmpX;

                        System.out.println(opponentPositionX + ublX + "  " + opponentPositionY + ublY);
                        try {
                            cellBoardEnemy[opponentPositionX + ublX][opponentPositionY + ublY].setIcon(createImageIcon("smoke.png", "smoke"));
                            cellBoardEnemy[opponentPositionX + ublX][opponentPositionY + ublY].repaint();

                        } catch (Exception ex) {
                        }
                    }else if (!parampampam[2].contains("?")) {
                        //// TODO: 23.12.2016
                        int tmpX = 0;
                        if (parampampam[2].charAt(0) == '+')
                            tmpX += Integer.parseInt(String.valueOf(parampampam[2].charAt(1)));
                        else
                            tmpX += -1 * Integer.parseInt(String.valueOf(parampampam[2].charAt(1)));

                        int tmpY = 0;
                        if (parampampam[2].charAt(2) == '+')
                            tmpY += Integer.parseInt(String.valueOf(parampampam[2].charAt(3)));
                        else
                            tmpY += -1 * Integer.parseInt(String.valueOf(parampampam[2].charAt(3)));
                        String opponentMove = ((tmpX >= 0) ? "+" + tmpX : "" + tmpX) + ((tmpY >= 0) ? "-" + tmpY : "+" + (-tmpY));

                        int ublX = -tmpY;
                        int ublY = tmpX;

                        System.out.println(opponentPositionX + ublX + "  " + opponentPositionY + ublY);
                        try {
                            cellBoardEnemy[opponentPositionX + ublX][opponentPositionY + ublY].setIcon(createImageIcon("epty.gif", "smoke"));
                            cellBoardEnemy[opponentPositionX + ublX][opponentPositionY + ublY].repaint();

                        } catch (Exception ex) {
                        }
                    }
                    else if (!parampampam[1].contains("?")) {
                        //// TODO: 23.12.2016
                        int tmpX = 0;
                        if (parampampam[1].charAt(0) == '+')
                            tmpX += Integer.parseInt(String.valueOf(parampampam[1].charAt(1)));
                        else
                            tmpX += -1 * Integer.parseInt(String.valueOf(parampampam[1].charAt(1)));

                        int tmpY = 0;
                        if (parampampam[1].charAt(2) == '+')
                            tmpY += Integer.parseInt(String.valueOf(parampampam[1].charAt(3)));
                        else
                            tmpY += -1 * Integer.parseInt(String.valueOf(parampampam[1].charAt(3)));
                        String opponentMove = ((tmpX >= 0) ? "+" + tmpX : "" + tmpX) + ((tmpY >= 0) ? "-" + tmpY : "+" + (-tmpY));

                        int ublX = -tmpY;
                        int ublY = tmpX;

                        System.out.println(opponentPositionX + ublX + "  " + opponentPositionY + ublY);
                        try {
                            cellBoardEnemy[opponentPositionX + ublX][opponentPositionY + ublY].setIcon(createImageIcon("granit.png", "smoke"));
                            cellBoardEnemy[opponentPositionX + ublX][opponentPositionY + ublY].repaint();

                        } catch (Exception ex) {
                        }
                    }
                    btnUp.setEnabled(true);
                    btnDown.setEnabled(true);
                    btnLeft.setEnabled(true);
                    btnRight.setEnabled(true);
                    btnSkip.setEnabled(true);
                } else if (response.startsWith("WIN")) {
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
                } else if (response.startsWith("INVALID")) {
                    try{
                        cellBoard[clientPositionX + (-MoveParser.parseYToInt(shiftDirection))][clientPositionY + MoveParser.parseXToInt(shiftDirection)].setIcon(createImageIcon("smoke.png", "seres"));
                        cellBoard[clientPositionX + (-MoveParser.parseYToInt(shiftDirection))][clientPositionY + MoveParser.parseXToInt(shiftDirection)].repaint();
                    }catch (Exception ex){

                    }

                }
                else if (response.startsWith("BOMBED")){
                    String s =response.substring(6);
                    if (s.contains("e")){
                        cellBoard[clientPositionX + (-MoveParser.parseYToInt(shiftDirection))][clientPositionY + MoveParser.parseXToInt(shiftDirection)].setIcon(createImageIcon("epty.gif", "seres"));
                        cellBoard[clientPositionX + (-MoveParser.parseYToInt(shiftDirection))][clientPositionY + MoveParser.parseXToInt(shiftDirection)].repaint();

                    }
                    else if (s.contains("g")){
                        cellBoard[clientPositionX + (-MoveParser.parseYToInt(shiftDirection))][clientPositionY + MoveParser.parseXToInt(shiftDirection)].setIcon(createImageIcon("granit.png", "seres"));
                        cellBoard[clientPositionX + (-MoveParser.parseYToInt(shiftDirection))][clientPositionY + MoveParser.parseXToInt(shiftDirection)].repaint();
                    }
                    btnUp.setEnabled(false);
                    btnDown.setEnabled(false);
                    btnLeft.setEnabled(false);
                    btnRight.setEnabled(false);
                    btnSkip.setEnabled(false);
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