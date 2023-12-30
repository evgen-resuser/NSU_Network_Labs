package org.evgen.gui;

import me.ippolitov.fit.snakes.SnakesProto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BoardMainPanel extends JPanel {

    private static final int SCALE = 15;
    private static final String LOADING_MSG = "Waiting for a fresh state...";
    private final int w;
    private final int h;

    public BoardMainPanel(int w, int h) {
        this.h = h;
        this.w = w;
        this.setPreferredSize(new Dimension(w * SCALE + 5, h * SCALE + 5));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();
    }

    private List<SnakesProto.GameState.Snake> snakes;
    private List<SnakesProto.GameState.Coord> foods;

    public void applyScene(SnakesProto.GameState state) {
        //if (snakes == null || foods == null) return;

        snakes = state.getSnakesList();
        foods = state.getFoodsList();
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (snakes == null || foods == null) {
            paintLoadingScreen(g);
            return;
        }
        paintField(g);
        paintSnakes(g);
        paintFoods(g);
    }

    private void paintLoadingScreen(Graphics g) {
        setBackground(Color.BLACK);
        g.setColor(Color.WHITE);
        int strW = g.getFontMetrics().stringWidth(LOADING_MSG);
        g.drawString(LOADING_MSG, (w * SCALE - strW) / 2, h / 2 * SCALE);
    }

    private final Colors colors = new Colors();
    private void paintSnakes(Graphics g) {
        //System.out.println(snakes);
        for (SnakesProto.GameState.Snake snake : snakes) {
            int id = snake.getPlayerId();


            g.setColor(colors.getColor(id % 10));
            //SnakesProto.GameState.Coord head = snake.getPoints(0);
            //g.drawString(String.valueOf(snake.getPlayerId()), (head.getX() * SCALE + 5), (head.getY() * SCALE - 2));

            int x = 0;
            int y = 0;

            for (SnakesProto.GameState.Coord point : snake.getPointsList()) {
                x += point.getX();
                y += point.getY();
                if (x < 0) x += w;
                if (x >= w) x %= w;
                if (y < 0) y += h;
                if (y >= h) y %= h;
                g.fillOval(x*SCALE, y*SCALE, SCALE, SCALE);
            }
        }
    }

    private void paintFoods(Graphics g) {
        g.setColor(Color.RED);

        for (SnakesProto.GameState.Coord food : foods) {
            g.drawOval(food.getX()*SCALE, food.getY()*SCALE, SCALE, SCALE);
        }
    }

    private void paintField(Graphics g){
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i != w+1; i++) {
            for (int j = 0; j != h+1; j++) {
                g.drawRect(i*SCALE, j*SCALE, 1, 1);
            }
        }
    }
}
