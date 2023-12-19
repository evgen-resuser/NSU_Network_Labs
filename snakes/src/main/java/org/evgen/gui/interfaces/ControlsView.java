package org.evgen.gui.interfaces;

import org.evgen.protocol.GameInfo;

import java.util.HashSet;

public interface ControlsView {
    void updateGames(HashSet<GameInfo> games);
}
