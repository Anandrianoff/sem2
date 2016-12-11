package edu.lmu.cs.networking;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Andrey on 10.12.2016.
 */
public class MinotavrClient {

    private JFrame mainFrame = new JFrame("Minotavr");
    private JFrame opponentFrame = new JFrame("Opponent");

    private static int PORT = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public static void main(String[] args) throws Exception {
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        MinotavrClient client = new MinotavrClient(serverAddress);
        client.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.mainFrame.setVisible(true);
    }

    public MinotavrClient(String serverAddress) throws Exception {
//        socket = new Socket(serverAddress, PORT);
//        in = new BufferedReader(new InputStreamReader(
//                socket.getInputStream()));
//        out = new PrintWriter(socket.getOutputStream(), true);

        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.BLACK);
        boardPanel.setLayout(new GridLayout(9, 9));
        boardPanel.setSize(390, 390);
        mainFrame.setSize(500, 400);
        mainFrame.setResizable(false);
        mainFrame.getContentPane().add(boardPanel, "West");
    }
}
