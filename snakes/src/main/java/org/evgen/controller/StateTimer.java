package org.evgen.controller;

import lombok.AllArgsConstructor;
import org.evgen.controller.interfaces.StateConstructor;

@AllArgsConstructor
public class StateTimer implements Runnable{

    private StateConstructor stateConstructor;
    private int delay;

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(delay);
                stateConstructor.releaseFreshState();
                //System.out.println("sent new state!!!");
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
