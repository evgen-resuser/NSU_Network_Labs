package org.evgen.controller;

import me.ippolitov.fit.snakes.SnakesProto;
import org.evgen.controller.interfaces.Controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TAdapter extends KeyAdapter {

    private final Controller controller;

    public TAdapter(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        switch (key) {
            case(KeyEvent.VK_LEFT) -> controller.applySteerMsg(SnakesProto.Direction.LEFT);
            case(KeyEvent.VK_RIGHT) -> controller.applySteerMsg(SnakesProto.Direction.RIGHT);
            case(KeyEvent.VK_UP) -> controller.applySteerMsg(SnakesProto.Direction.UP);
            case(KeyEvent.VK_DOWN) -> controller.applySteerMsg(SnakesProto.Direction.DOWN);
            default -> {/*do nothing*/}
        }
    }
}