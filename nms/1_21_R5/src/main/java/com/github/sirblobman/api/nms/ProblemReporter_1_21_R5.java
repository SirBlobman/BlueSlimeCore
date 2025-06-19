package com.github.sirblobman.api.nms;

import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.plugin.Plugin;

import net.minecraft.util.ProblemReporter;

public final class ProblemReporter_1_21_R5 implements ProblemReporter {
    private final Plugin plugin;

    public ProblemReporter_1_21_R5(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public ProblemReporter forChild(PathElement pathElement) {
        return this;
    }

    @Override
    public void report(Problem problem) {
        Logger logger = getLogger();
        String description = problem.description();
        logger.warning("A problem has occurred:");
        logger.warning(description);
    }

    private @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    private @NotNull Logger getLogger() {
        return getPlugin().getLogger();
    }
}
