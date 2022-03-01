package com.github.sirblobman.bossbar;

import java.util.Set;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BossBarWrapper {
    private final Player player;
    
    public BossBarWrapper(@NotNull Player player) {
        this.player = Validate.notNull(player, "player must not be null!");
        if(!this.player.isOnline()) throw new IllegalArgumentException("player must be online!");
    }
    
    @NotNull
    public final Player getPlayer() {
        return this.player;
    }
    
    public abstract void addExtraPlayer(@NotNull Player player);
    
    public abstract void removeExtraPlayer(@NotNull Player player);
    
    @NotNull
    public abstract Set<Player> getExtraPlayers();
    
    public abstract void removeAllExtraPlayers();
    
    @Nullable
    public abstract String getTitle();
    
    public abstract void setTitle(@NotNull String title);
    
    public abstract double getProgress();
    
    public abstract void setProgress(double progress);
    
    @Nullable
    public abstract Object getColor();
    
    public abstract void setColor(@NotNull String colorName);
    
    @Nullable
    public abstract Object getStyle();
    
    public abstract void setStyle(@NotNull String styleName);
    
    public abstract void addFlag(@NotNull String flagName);
    
    public abstract void removeFlag(@NotNull String flagName);
    
    public abstract boolean hasFlag(@NotNull String flagName);
    
    public abstract boolean isVisible();
    
    public abstract void setVisible(boolean visible);
}
