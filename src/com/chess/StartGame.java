package com.chess;

import com.chess.gui.Table;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class StartGame implements ActionListener {
    private JFrame fr;
    private JPanel p1, p2, p3, p4;
    private JButton button1, button2;


    public void init(){
        fr = new JFrame("Chess");
        fr.setContentPane(new JLabel(new ImageIcon("img/chess.jpg")));
        p1 = new JPanel();
        p2 = new JPanel();
        p3 = new JPanel();
        p4 = new JPanel();

        BufferedImage buttonIcon = null;
        try {
            buttonIcon = ImageIO.read(new File("img/play.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        button1 = new JButton(new ImageIcon(buttonIcon));
        button1.setBorderPainted(false);
        button1.setFocusPainted(false);
        button1.setContentAreaFilled(false);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().show();
                fr.setVisible(false);
            }
        });

        BufferedImage buttonIcon2 = null;
        try {
            buttonIcon2 = ImageIO.read(new File("img/exit.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        button2 = new JButton(new ImageIcon(buttonIcon2));
        button2.setBorderPainted(false);
        button2.setFocusPainted(false);
        button2.setContentAreaFilled(false);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        p1.add(button1);
        p2.add(button2);
        p1.setOpaque(false);
        p2.setOpaque(false);
        p4.setLayout(new GridLayout(2, 1));
        p4.add(p1);
        p4.add(p2);
        p4.setOpaque(false);

        fr.setLayout(new GridBagLayout());
        fr.add(p4);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(900, 562);
        fr.setResizable(false);
        fr.setLocationRelativeTo(null);
        fr.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if(cmd.equals("Exit")){
            System.exit(0);
        }
        else if (cmd.equals("Play Classic Mode")){
            Table.get().show();
        }
        else if (cmd.equals("Play Random Mode")){
            Table.get().show();
        }
        fr.setVisible(false);
    }
}
