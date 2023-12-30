package org.evgen.gui;

import org.evgen.controller.MainController;
import org.evgen.gui.interfaces.ControlsView;
import org.evgen.protocol.GameInfo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashSet;

public class ControlPanel extends JPanel implements ControlsView {

    private JPanel buttons;
    private JPanel otherBoards;
    private JPanel descr;
    private JCheckBox isViewer = new JCheckBox();

    private MainController masterController;

    public ControlPanel(MainController controller) {
        this.masterController = controller;

        this.setPreferredSize(new Dimension(400, 400));

        initButtonsPanel();
        initOtherBoardsPanel();
        initDescription();

        this.add(buttons);
        this.add(otherBoards);


        isViewer.setText("Connect as viewer");
        this.add(isViewer);

        this.add(descr);
//        JButton b = new JButton("kill me");
//        b.addActionListener( e -> {
//            controller.testDeath();
//        });
//        this.add(b);

    }

    private void initButtonsPanel() {
        buttons = new JPanel();
        buttons.setBorder(new LineBorder(Color.LIGHT_GRAY));
        buttons.setPreferredSize(new Dimension(325, 40));

        JButton start = new JButton("New Game");
        JButton quit = new JButton("Quit");

        start.addActionListener(actionEvent -> {
            masterController.startGame();
        });

        quit.addActionListener(actionEvent -> {
            masterController.stopGame();
        });

        buttons.add(start);
        buttons.add(quit);
    }

    private void initDescription() {
        this.descr = new JPanel();
        descr.setBorder(new LineBorder(Color.LIGHT_GRAY));
        descr.setLayout(new BoxLayout(descr, BoxLayout.Y_AXIS));
        descr.add(new JLabel("Role icons: "));
        descr.add(new JLabel("★ - Master"));
        descr.add(new JLabel("☆ - Deputy"));
        descr.add(new JLabel("○ - Normal player"));
    }

    private JList<GameInfo> otherGames;

    private void initOtherBoardsPanel() {
        otherBoards = new JPanel();
        otherBoards.setLayout(new BorderLayout());
        otherBoards.setBorder(new LineBorder(Color.LIGHT_GRAY));
        otherBoards.setPreferredSize(new Dimension(325, 200));

        otherGames = new JList<>();
        otherGames.setFixedCellWidth(300);
        otherGames.setDragEnabled(false);
        otherGames.addListSelectionListener(e -> {
                    if(!otherGames.getValueIsAdjusting()) {
                        masterController.connect(otherGames.getSelectedValue(), isViewer.isSelected());
                    }
                }
        );
        otherBoards.add(otherGames, BorderLayout.NORTH);

    }

    @Override
    public void updateGames(HashSet<GameInfo> games) {
        GameInfo[] gamesArr = games.toArray(new GameInfo[0]);
        otherGames.setListData(gamesArr);
    }
}
