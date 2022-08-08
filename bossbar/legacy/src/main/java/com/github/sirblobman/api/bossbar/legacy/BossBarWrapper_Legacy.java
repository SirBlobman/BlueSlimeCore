package com.github.sirblobman.api.bossbar.legacy;

import java.util.Collections;
import java.util.Set;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.bossbar.BossBarWrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BossBarWrapper_Legacy extends BossBarWrapper {
    public BossBarWrapper_Legacy(@NotNull Player player) {
        super(player);
    }

    @Override
    public void addExtraPlayer(@NotNull Player player) {
        // Do Nothing
    }

    @Override
    public void removeExtraPlayer(@NotNull Player player) {
        BossBarAPI.removeBar(player);
    }

    @Override
    public @NotNull Set<Player> getExtraPlayers() {
        return Collections.emptySet();
    }

    @Override
    public void removeAllExtraPlayers() {
        // Do Nothing
    }

    @Override
    public @Nullable String getTitle() {
        Player player = getPlayer();
        return BossBarAPI.getMessage(player);
    }

    @Override
    public void setTitle(@NotNull String title) {
        Player player = getPlayer();
        BossBarAPI.setMessage(player, title);
    }

    @Override
    public double getProgress() {
        Player player = getPlayer();
        double health = BossBarAPI.getHealth(player);
        return (health / 100.0D);
    }

    @Override
    public void setProgress(double progress) {
        Player player = getPlayer();
        float health = (float) (progress * 100.0F);
        BossBarAPI.setHealth(player, health);
    }

    @Override
    public @Nullable Object getColor() {
        return null;
    }

    @Override
    public void setColor(@NotNull String colorName) {
        // Do Nothing
    }

    @Override
    public @Nullable Object getStyle() {
        return null;
    }

    @Override
    public void setStyle(@NotNull String styleName) {
        // Do Nothing
    }

    @Override
    public void addFlag(@NotNull String flagName) {
        // Do Nothing
    }

    @Override
    public void removeFlag(@NotNull String flagName) {
        // Do Nothing
    }

    @Override
    public boolean hasFlag(@NotNull String flagName) {
        return false;
    }

    @Override
    public boolean isVisible() {
        Player player = getPlayer();
        BossBar bossBar = BossBarAPI.getBossBar(player);
        return (bossBar != null && bossBar.isVisible());
    }

    @Override
    public void setVisible(boolean visible) {
        Player player = getPlayer();
        BossBar bossBar = BossBarAPI.getBossBar(player);
        if (bossBar != null) {
            bossBar.setVisible(visible);
        }
    }
}
