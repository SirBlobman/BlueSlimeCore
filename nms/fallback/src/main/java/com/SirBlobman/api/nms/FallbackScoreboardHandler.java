package com.SirBlobman.api.nms;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class FallbackScoreboardHandler extends ScoreboardHandler {
    @Override
    public Objective createObjective(Scoreboard scoreboard, String name, String criteria, String displayName) {
        return null;
    }
}