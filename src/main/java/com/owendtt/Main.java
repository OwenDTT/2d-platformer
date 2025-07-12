package com.owendtt;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Platformer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            showMenu(frame);

            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void showMenu(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();

        JPanel menu = new JPanel(new GridLayout(4, 1, 10, 10));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        String[] difficulties = {"Easy", "Medium", "Hard", "Expert"};
        for (String diff : difficulties) {
            JButton btn = new JButton(diff);
            btn.setFont(btn.getFont().deriveFont(24f));
            btn.addActionListener(e -> {
                Game game = new Game(diff);

                frame.getContentPane().removeAll();
                frame.getContentPane().add(game);

                frame.getContentPane().revalidate();
                frame.getContentPane().repaint();

                frame.setSize(WIDTH, HEIGHT);
                frame.setLocationRelativeTo(null);

                game.setFocusable(true);
                game.requestFocusInWindow();
                game.start();
            });
            menu.add(btn);
        }

        frame.getContentPane().add(menu);
    }
}
