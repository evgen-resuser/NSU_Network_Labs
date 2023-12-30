package org.evgen.gui;

import lombok.Setter;
import me.ippolitov.fit.snakes.SnakesProto;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class StatsPanel extends JPanel {

    JPanel scoreboardPanel;
    @Setter
    int id;

    public StatsPanel() {
        this.scoreboardPanel = new JPanel();
        scoreboardPanel.setPreferredSize(new Dimension(200, 100));
        scoreboardPanel.setBorder(new LineBorder(Color.GRAY));
        scoreboardPanel.setFocusable(false);

        initScoreboard();

        this.add(new JLabel("Scoreboard"));
        this.add(scoreboardPanel);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setFocusable(false);
    }

    private JList<String> playersList;
    private void initScoreboard() {
        playersList = new JList<>();
        playersList.setFocusable(false);
        playersList.setFixedCellWidth(180);
        playersList.setCellRenderer(new NewListRenderer());

        scoreboardPanel.add(playersList);
    }

    int[] colorsArr = new int[50];
    public void applyPlayersList(List<SnakesProto.GamePlayer> playerList) {
        String[] players = new String[playerList.size()];
        int i = 0;
        List<SnakesProto.GamePlayer> tmp = new ArrayList<>(playerList);
        tmp.sort(Comparator.comparingInt(SnakesProto.GamePlayer::getScore).reversed());
        for (SnakesProto.GamePlayer player : tmp) {
            String roleIcon = "";
            switch (player.getRole()) {
                case MASTER -> roleIcon = "★";
                case DEPUTY -> roleIcon = "☆";
                case NORMAL -> roleIcon = "○";
                case VIEWER -> {/*do nothing*/}
            }
            String name = String.format(
                    "%s %s (%d) %d", roleIcon, player.getName(), player.getId(), player.getScore()
            );
            if (id == player.getId()) name += " (you)";
            players[i] = name;
            colorsArr[i] = player.getId() % 10;
            i++;
        }
        playersList.setListData(players);
    }

    private Colors colors = new Colors();

    private class NewListRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);
            c.setBackground(colors.getColor(colorsArr[index]));
            return c;
        }
    }

}
