package org.evgen.gui;

import lombok.Setter;
import me.ippolitov.fit.snakes.SnakesProto;
import org.evgen.controller.TAdapter;
import org.evgen.gui.interfaces.BoardView;

import javax.swing.*;

public class BoardFrame extends JFrame implements BoardView {

    StatsPanel statsPanel;
    BoardMainPanel gamePanel;

    @Setter
    private int id;

    public BoardFrame(SnakesProto.GameConfig config, TAdapter adapter, int id){
        this.gamePanel = new BoardMainPanel(config.getWidth(), config.getHeight());
        this.statsPanel = new StatsPanel();
        this.id = id;

        //this.setUndecorated(true);
        System.out.println(adapter);
        gamePanel.addKeyListener(adapter);

        this.setLocation(500, 0);
        this.setVisible(true);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.getContentPane().add(statsPanel);
        this.getContentPane().add(gamePanel);
        this.pack();
    }

    @Override
    public void applyState(SnakesProto.GameState state) {
        statsPanel.setId(id);
        statsPanel.applyPlayersList(state.getPlayers().getPlayersList());
        gamePanel.applyScene(state);
    }

}
