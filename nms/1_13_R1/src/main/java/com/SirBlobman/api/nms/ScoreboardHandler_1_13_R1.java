package com.SirBlobman.api.nms;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler_1_13_R1 extends ScoreboardHandler {
    @Override
    public Objective createObjective(Scoreboard scoreboard, String name, String criteria, String displayName) {
        return scoreboard.registerNewObjective(name, criteria, displayName);
    }
}