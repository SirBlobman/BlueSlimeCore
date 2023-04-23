package com.github.sirblobman.api.nms.scoreboard;

import org.jetbrains.annotations.NotNull;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.github.sirblobman.api.nms.Handler;
import com.github.sirblobman.api.utility.VersionUtility;

/**
 * NMS Scoreboard Handler
 * @author SirBlobman
 */
public final class ScoreboardHandler extends Handler {
    /**
     * Create a new instance of this handler.
     * @param plugin The plugin that owns this instance.
     */
    public ScoreboardHandler(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Create a scoreboard objective with a display name.
     * Note that this method replaces the deprecated method in versions above 1.13.2.
     * @param scoreboard The scoreboard instance to use.
     * @param name The internal name of the objective.
     * @param criteria The criteria of the objective, usually "dummy".
     * @param displayName The display name for the objective that will be shown to players.
     * @return The new objective that was created.
     */
    public @NotNull Objective createObjective(@NotNull Scoreboard scoreboard, @NotNull String name,
                                              @NotNull String criteria, @NotNull String displayName) {
        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 13) {
            return createObjectiveLegacy(scoreboard, name, criteria, displayName);
        } else {
            return createObjectiveModern(scoreboard, name, criteria, displayName);
        }
    }

    @SuppressWarnings("deprecation") // Legacy Method
    private @NotNull Objective createObjectiveLegacy(@NotNull Scoreboard scoreboard, @NotNull String name,
                                                     @NotNull String criteria, @NotNull String displayName) {
        Objective objective = scoreboard.registerNewObjective(name, criteria);
        objective.setDisplayName(displayName);
        return objective;
    }

    private @NotNull Objective createObjectiveModern(@NotNull Scoreboard scoreboard, @NotNull String name,
                                                     @NotNull String criteria, @NotNull String displayName) {
        return scoreboard.registerNewObjective(name, criteria, displayName);
    }
}
