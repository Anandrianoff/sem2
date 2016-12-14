package edu.lmu.cs.networking;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;

/**
 * Created by Andrey on 10.12.2016.
 */
public class MinotavrClient {
    String player;

    JPanel boardPanel;
    private String action;

    private JFrame mainFrame = new JFrame("Minotavr");
    private JFrame opponentFrame = new JFrame("Opponent");

    private static int PORT = 8903;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    Cell[][] board = new Cell[9][9];

    public static void main(String[] args) throws Exception {
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        MinotavrClient client = new MinotavrClient(serverAddress);
        client.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.mainFrame.setVisible(true);
        client.opponentFrame.setVisible(true);
        client.play();
    }

    private void play(){
        String response;
        try{
            response = in.readLine();
            System.out.println(response);
            if (response.startsWith("WELCOME")) {
//                char mark = response.charAt(8);
                board[4][4].setHasFirstPlayer(true);
                board[4][4].setType(CellTypes.EMPTY);
                boardPanel = new JPanel();
                boardPanel.setLayout(new GridLayout(9, 9, 2,2));
                boardPanel.setSize(390, 370);
                for (int i = 0; i < board.length; i++){
                    for (int j = 0; j < board[0].length; j++){
                        board[i][j] = new Cell();
                        board[i][j].setSize(boardPanel.getWidth()/9, boardPanel.getWidth()/9);
                        boardPanel.add(board[i][j]);
                    }
                }
            }
        }catch (Exception ex){

        }

    }

    public MinotavrClient(String serverAddress) throws Exception {
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        boardPanel = new JPanel();
        JPanel navPanel = new JPanel();
        navPanel.setSize(110, 400);
        navPanel.setLayout(new GridLayout(4, 0));
        boardPanel.setBackground(Color.BLACK);
        boardPanel.setLayout(new GridLayout(9, 9, 2,2));
        boardPanel.setSize(390, 370);
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                board[i][j] = new Cell();
                board[i][j].setSize(boardPanel.getWidth()/9, boardPanel.getWidth()/9);
                boardPanel.add(board[i][j]);
            }
        }
        JButton btnUp = new JButton("↑");
        btnUp.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "up";
            }
        });
        JButton btnGo = new JButton("GO");
        JButton btnBomb = new JButton("BOMB");
        JButton btnAsk = new JButton("ASK");
        JButton btnDown = new JButton("↓");
        JButton btnLeft = new JButton("←");
        JButton btnRight = new JButton("→");
        btnGo.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "GO";
            }
        });
        btnAsk.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                action = "ASK";
            }
        });
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
        navPanel.add(btnUp, "West");
        navPanel.add(btnRight, "West");
        navPanel.add(btnDown, "West");
        navPanel.add(btnLeft, "West");
        navPanel.add(btnGo, "West");
        navPanel.add(btnBomb, "West");
        navPanel.add(btnAsk,"West");
        mainFrame.setSize(500, 400);
        mainFrame.setResizable(false);
        mainFrame.getContentPane().add(boardPanel, "Center");
        mainFrame.getContentPane().add(navPanel, "East");
        opponentFrame.setSize(300, 300);
    }
}
