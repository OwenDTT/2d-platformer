package com.owendtt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH  = Main.WIDTH;
    private static final int HEIGHT = Main.HEIGHT;

    private final String difficulty;
    private final List<Block> blocks = new ArrayList<>();
    private Timer timer;
    private Player player;
    private boolean gameWon    = false;
    private boolean gameFailed = false;

    public Game(String difficulty) {
        this.difficulty = difficulty;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);
        addKeyListener(this);

        player = new Player(50, 100);
        initLevel();
        timer = new Timer(16, this);  // ~60 FPS
    }

    /** Call once this panel is added to the frame */
    public void start() {
        requestFocusInWindow();
        timer.start();
    }

    /** Populate blocks[] based on difficulty */
    private void initLevel() {
        int[][] levelData;
        switch (difficulty) {
            case "Easy":
                levelData = new int[][] {
                    {   0, HEIGHT - 50, WIDTH, 50 },
                    { 150, 450, 120, 20 },
                    { 350, 400, 100, 20 },
                    { 550, 450, 120, 20 },
                    { 250, 300, 100, 20 }
                };
                break;
            case "Medium":
                levelData = new int[][] {
                    {   0, HEIGHT - 50, WIDTH, 50 },
                    { 200, 400, 100, 20 },
                    { 400, 300, 150, 20 },
                    { 100, 500,  80, 20 },
                    { 300, 450, 120, 20 },
                    { 550, 350, 100, 20 },
                    { 700, 250, 150, 20 },
                    {  50, 300,  60, 20 }
                };
                break;
            case "Hard":
                levelData = new int[][] {
                    {   0, HEIGHT - 50, WIDTH, 50 },
                    { 150, 480,  80, 20 },
                    { 300, 430,  70, 20 },
                    { 450, 380,  90, 20 },
                    { 600, 330,  80, 20 },
                    { 200, 280,  60, 20 },
                    { 500, 230,  70, 20 },
                    { 700, 180, 100, 20 },
                    {  50, 350,  60, 20 }
                };
                break;
            case "Expert":
                levelData = new int[][] {
                    {   0, HEIGHT - 50, WIDTH, 50 },
                    { 100, 500,  60, 20 },
                    { 250, 460,  50, 20 },
                    { 400, 420,  60, 20 },
                    { 550, 380,  50, 20 },
                    { 700, 340,  60, 20 },
                    { 150, 300,  40, 20 },
                    { 300, 260,  50, 20 },
                    { 450, 220,  40, 20 },
                    { 600, 180,  50, 20 },
                    { 750, 140,  40, 20 }
                };
                break;
            default:
                levelData = new int[][] {{ 0, HEIGHT - 50, WIDTH, 50 }};
        }

        blocks.clear();
        for (int[] p : levelData) {
            blocks.add(new Block(p[0], p[1], p[2], p[3]));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Block b : blocks) b.draw(g);
        player.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameWon || gameFailed) return;

        player.update(blocks);
        Rectangle r = player.getBounds();

        // Check right-edge
        if (r.x + r.width >= WIDTH) {
            int groundTop = HEIGHT - 50;
            if (r.y + r.height >= groundTop) {
                // walked on ground → fail
                gameFailed = true;
                timer.stop();
                showFailScreen();
            } else {
                // landed on a platform → win
                gameWon = true;
                timer.stop();
                showWinScreen();
            }
            return;
        }

        repaint();
    }

    /** Show “You Win” screen */
    private void showWinScreen() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();

        JPanel win = new JPanel(new BorderLayout());
        win.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        JLabel msg = new JLabel("Congratulations – thanks for playing!", SwingConstants.CENTER);
        msg.setFont(msg.getFont().deriveFont(32f));
        win.add(msg, BorderLayout.CENTER);

        JButton back = new JButton("Back to Level Selection");
        back.setFont(back.getFont().deriveFont(24f));
        back.addActionListener(e -> {
            Main.showMenu(frame);
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
        });
        JPanel btns = new JPanel();
        btns.add(back);
        win.add(btns, BorderLayout.SOUTH);

        frame.getContentPane().add(win);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
    }

    /** Show “Level Failed” screen */
    private void showFailScreen() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();

        JPanel fail = new JPanel(new BorderLayout());
        fail.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        JLabel msg = new JLabel("Level failed – try again", SwingConstants.CENTER);
        msg.setFont(msg.getFont().deriveFont(32f));
        fail.add(msg, BorderLayout.CENTER);

        JButton retryButton = new JButton("Try Again");
        retryButton.setFont(retryButton.getFont().deriveFont(24f));
        retryButton.addActionListener(e -> {
            Game retryGame = new Game(difficulty);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(retryGame);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
            retryGame.setFocusable(true);
            retryGame.requestFocusInWindow();
            retryGame.start();
        });

        JButton back = new JButton("Back to Level Selection");
        back.setFont(back.getFont().deriveFont(24f));
        back.addActionListener(e -> {
            Main.showMenu(frame);
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
        });

        JPanel btns = new JPanel();
        btns.add(retryButton);
        btns.add(back);
        fail.add(btns, BorderLayout.SOUTH);

        frame.getContentPane().add(fail);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
    }

    // --- KeyListener impl ---
    @Override public void keyPressed(KeyEvent e)  { player.keyPressed(e); }
    @Override public void keyReleased(KeyEvent e) { player.keyReleased(e); }
    @Override public void keyTyped(KeyEvent e)    { }
}
