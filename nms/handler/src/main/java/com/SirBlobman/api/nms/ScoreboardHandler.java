package com.SirBlobman.api.nms;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public abstract class ScoreboardHandler {
    public abstract Objective createObjective(Scoreboard scoreboard, String name, String criteria, String displayName);
}
