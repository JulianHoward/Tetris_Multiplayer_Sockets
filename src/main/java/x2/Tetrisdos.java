package x2;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;

public class Tetrisdos extends JFrame {

    JLabel statusbar;


    public Tetrisdos(String a, Socket s) {
        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);
        Tablerodos board = new Tablerodos(this,a,s);
        add(board);
        board.start();
        setSize(400, 800);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public JLabel getStatusBar() {
        return statusbar;
    }

}
