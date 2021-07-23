package com.github.sirblobman.bossbar.modern;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.sirblobman.bossbar.BossBarWrapper;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Flag;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public final class BossBarWrapper_Paper extends BossBarWrapper {
    private final BossBar bossBar;
    private final Set<UUID> extraPlayerSet;

    public BossBarWrapper_Paper(@NotNull Player player) {
        super(player);

        Component defaultTitle = getDefaultTitle();
        this.bossBar = BossBar.bossBar(defaultTitle, 1.0F, Color.BLUE, Overlay.PROGRESS);
        player.showBossBar(this.bossBar);

        this.extraPlayerSet = new HashSet<>();
    }

    @Override
    public void addExtraPlayer(@NotNull Player player) {
        player.showBossBar(this.bossBar);
        this.extraPlayerSet.add(player.getUniqueId());
    }

    @Override
    public void removeExtraPlayer(@NotNull Player player) {
        player.hideBossBar(this.bossBar);
        this.extraPlayerSet.remove(player.getUniqueId());
    }

    @Override
    public @NotNull Set<Player> getExtraPlayers() {
        this.extraPlayerSet.removeIf(uuid -> Bukkit.getPlayer(uuid) == null);
        return Collections.unmodifiableSet(this.extraPlayerSet.stream().map(Bukkit::getPlayer)
                .collect(Collectors.toSet()));
    }

    @Override
    public void removeAllExtraPlayers() {
        this.extraPlayerSet.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null) removeExtraPlayer(player);
        });

        this.extraPlayerSet.clear();
    }

    @Override
    public String getTitle() {
        Component component = this.bossBar.name();
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    @Override
    public void setTitle(@NotNull String title) {
        Component component = LegacyComponentSerializer.legacySection().deserialize(title);
        this.bossBar.name(component);
    }

    @Override
    public double getProgress() {
        return this.bossBar.progress();
    }

    @Override
    public void setProgress(double progress) {
        float floatProgress = (float) progress;
        this.bossBar.progress(floatProgress);
    }

    @Override
    public Color getColor() {
        return this.bossBar.color();
    }

    @Override
    public void setColor(@NotNull String colorName) {
        try {
            Color color = Color.valueOf(colorName);
            this.bossBar.color(color);
        } catch(IllegalArgumentException ignored) {}
    }

    @Override
    public Overlay getStyle() {
        return this.bossBar.overlay();
    }

    @Override
    public void setStyle(@NotNull String styleName) {
        try {
            Overlay overlay = Overlay.valueOf(styleName);
            this.bossBar.overlay(overlay);
        } catch(IllegalArgumentException ignored) {}
    }

    @Override
    public void addFlag(@NotNull String flagName) {
        try {
            Flag flag = Flag.valueOf(flagName);
            this.bossBar.addFlag(flag);
        } catch(IllegalArgumentException ignored) {}
    }

    @Override
    public void removeFlag(@NotNull String flagName) {
        try {
            Flag flag = Flag.valueOf(flagName);
            this.bossBar.removeFlag(flag);
        } catch(IllegalArgumentException ignored) {}
    }

    @Override
    public boolean hasFlag(@NotNull String flagName) {
        try {
            Flag flag = Flag.valueOf(flagName);
            return this.bossBar.hasFlag(flag);
        } catch(IllegalArgumentException ignored) {
            return false;
        }
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void setVisible(boolean visible) {
        // Do Nothing
    }

    private Component getDefaultTitle() {
        return Component.text("Default Title");
    }
}
