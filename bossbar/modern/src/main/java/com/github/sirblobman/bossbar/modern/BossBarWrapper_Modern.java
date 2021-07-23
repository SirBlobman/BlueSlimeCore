package com.github.sirblobman.bossbar.modern;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.github.sirblobman.bossbar.BossBarWrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BossBarWrapper_Modern extends BossBarWrapper {
    private BossBar bossBar;

    public BossBarWrapper_Modern(@NotNull Player player) {
        super(player);
        this.bossBar = null;
    }

    @Override
    public void addExtraPlayer(@NotNull Player player) {
        BossBar bossBar = getBossBar();
        bossBar.addPlayer(player);
    }

    @Override
    public void removeExtraPlayer(@NotNull Player player) {
        BossBar bossBar = getBossBar();
        bossBar.removePlayer(player);
    }

    @NotNull
    @Override
    public Set<Player> getExtraPlayers() {
        BossBar bossBar = getBossBar();
        List<Player> playerList = bossBar.getPlayers();
        return Collections.unmodifiableSet(new HashSet<>(playerList));
    }

    @Override
    public void removeAllExtraPlayers() {
        BossBar bossBar = getBossBar();
        bossBar.removeAll();
    }

    @Nullable
    @Override
    public String getTitle() {
        BossBar bossBar = getBossBar();
        return bossBar.getTitle();
    }

    @Override
    public void setTitle(@NotNull String title) {
        BossBar bossBar = getBossBar();
        bossBar.setTitle(title);
    }

    @Override
    public double getProgress() {
        BossBar bossBar = getBossBar();
        return bossBar.getProgress();
    }

    @Override
    public void setProgress(double progress) {
        BossBar bossBar = getBossBar();
        bossBar.setProgress(progress);
    }

    @Nullable
    @Override
    public BarColor getColor() {
        BossBar bossBar = getBossBar();
        return bossBar.getColor();
    }

    @Override
    public void setColor(@NotNull String colorName) {
        try {
            BarColor barColor = BarColor.valueOf(colorName);
            BossBar bossBar = getBossBar();
            bossBar.setColor(barColor);
        } catch(IllegalArgumentException ignored) {}
    }

    @Nullable
    @Override
    public BarStyle getStyle() {
        BossBar bossBar = getBossBar();
        return bossBar.getStyle();
    }

    @Override
    public void setStyle(@NotNull String styleName) {
        try {
            BarStyle barStyle = getStyle(styleName);
            BossBar bossBar = getBossBar();
            bossBar.setStyle(barStyle);
        } catch(IllegalArgumentException ignored) {}
    }

    @Override
    public void addFlag(@NotNull String flagName) {
        try {
            BarFlag barFlag = BarFlag.valueOf(flagName);
            BossBar bossBar = getBossBar();
            bossBar.addFlag(barFlag);
        } catch(IllegalArgumentException ignored) {}
    }

    @Override
    public void removeFlag(@NotNull String flagName) {
        try {
            BarFlag barFlag = BarFlag.valueOf(flagName);
            BossBar bossBar = getBossBar();
            bossBar.removeFlag(barFlag);
        } catch(IllegalArgumentException ignored) {}
    }

    @Override
    public boolean hasFlag(@NotNull String flagName) {
        try {
            BarFlag barFlag = BarFlag.valueOf(flagName);
            BossBar bossBar = getBossBar();
            return bossBar.hasFlag(barFlag);
        } catch(IllegalArgumentException ignored) {
            return false;
        }
    }

    @Override
    public boolean isVisible() {
        BossBar bossBar = getBossBar();
        return bossBar.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        BossBar bossBar = getBossBar();
        bossBar.setVisible(visible);
    }

    private BossBar getBossBar() {
        if(this.bossBar != null) {
            return this.bossBar;
        }

        this.bossBar = Bukkit.createBossBar("Default Title", BarColor.BLUE, BarStyle.SOLID);
        this.bossBar.addPlayer(getPlayer());
        return this.bossBar;
    }

    private BarStyle getStyle(String styleName) {
        String styleNameUpper = styleName.toUpperCase(Locale.US);
        switch(styleNameUpper) {
            case "PROGRESS": return BarStyle.SOLID;
            case "NOTCHED_6": return BarStyle.SEGMENTED_6;
            case "NOTCHED_10": return BarStyle.SEGMENTED_10;
            case "NOTCHED_12": return BarStyle.SEGMENTED_12;
            case "NOTCHED_20": return BarStyle.SEGMENTED_20;
            default: break;
        }

        return BarStyle.SOLID;
    }
}
