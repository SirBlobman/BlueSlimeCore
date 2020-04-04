package com.SirBlobman.api.nms;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler_1_8_R3 extends ScoreboardHandler {
    public ScoreboardHandler_1_8_R3(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public Objective createObjective(Scoreboard scoreboard, String name, String criteria, String displayName) {
        Objective objective = scoreboard.registerNewObjective(name, criteria);
        objective.setDisplayName(displayName);
        return objective;
    }
}