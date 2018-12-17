package com.chess;

import javax.swing.*;
import java.awt.*;

class BackgroundImageJFrame extends JFrame {
    JButton b1;
    JLabel l1;

    public BackgroundImageJFrame()
    {
        setTitle("Chess");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon("chess.jpg")));
        setLayout(new FlowLayout());
        l1=new JLabel("Here is a button");
        b1=new JButton("I am a button");
        add(l1);
        add(b1);
        setSize(600,497);
    }
}