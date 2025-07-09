package com.owendtt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Block {
    private final int x, y, width, height;
    private final Color color = Color.DARK_GRAY;

    public Block(int x, int y, int width, int height) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() {
        return y;
    }
}
