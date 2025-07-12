package com.owendtt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class Player {
    private int x, y;
    private final int width  = 40;
    private final int height = 60;

    private double velX = 0, velY = 0;
    private final double speed        = 4;
    private final double jumpStrength = 14;
    private final double gravity      = 0.6;

    private BufferedImage sprite;

    private boolean left, right, jumping;
    private int jumpCount = 0;
    private final int maxJumps = 2; // double-jump

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;

        try {
            sprite = ImageIO.read(
                getClass().getResourceAsStream("/npc.png")
            );
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            sprite = null;
        }
    }

    public void update(List<Block> blocks) {
        if (left)       velX = -speed;
        else if (right) velX = speed;
        else            velX = 0;
        x += velX;

        velY += gravity;
        y    += velY;

        boolean landed = false;
        for (Block b : blocks) {
            if (getBounds().intersects(b.getBounds())) {
                if (velY > 0 && y + height - velY <= b.getY()) {
                    y    = b.getY() - height;
                    velY = 0;
                    landed = true;
                }
            }
        }
        if (landed) {
            jumpCount = 0;
        }

        if (jumping && jumpCount < maxJumps) {
            velY = -jumpStrength;
            jumpCount++;
            jumping = false;
        }
    }

    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_SPACE:
                jumping = true;
                break;
            default:
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_SPACE:
                jumping = false;
                break;
            default:
        }
    }
}
