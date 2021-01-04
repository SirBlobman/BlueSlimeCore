package com.github.sirblobman.api.nms.scoreboard;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.github.sirblobman.api.nms.Handler;
import com.github.sirblobman.api.utility.VersionUtility;

public class ScoreboardHandler extends Handler {
    public ScoreboardHandler(JavaPlugin plugin) {
        super(plugin);
    }

    @SuppressWarnings("deprecation")
    public Objective createObjective(Scoreboard scoreboard, String name, String criteria, String displayName) {
        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion < 13) {
            Objective objective = scoreboard.registerNewObjective(name, criteria);
            objective.setDisplayName(displayName);
            return objective;
        }

        return scoreboard.registerNewObjective(name, criteria, displayName);
    }
}