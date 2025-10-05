package com.github.sirblobman.api.factions;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import dev.kitteh.factions.FLocation;
import dev.kitteh.factions.FPlayer;
import dev.kitteh.factions.FPlayers;
import dev.kitteh.factions.Faction;
import dev.kitteh.factions.permissible.Relation;
import dev.kitteh.factions.permissible.Role;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public final class FactionsHandler_UUID4 extends FactionsHandler {
    @Override
    public boolean hasFaction(@NotNull OfflinePlayer player) {
        FPlayer fplayer = getFPlayer(player);
        return fplayer.hasFaction();
    }

    @Override
    public @Nullable FactionWrapper getFactionFor(@NotNull OfflinePlayer player) {
        if (hasFaction(player)) {
            FPlayer fplayer = getFPlayer(player);
            return wrap(fplayer.faction());
        }

        return null;
    }

    @Override
    public @NotNull FactionWrapper getFactionAt(@NotNull Location location) {
        FLocation flocation = new FLocation(location);
        return wrap(flocation.faction());
    }

    @Override
    public boolean isAlly(@NotNull OfflinePlayer player1, @NotNull OfflinePlayer player2) {
        if (player1.getUniqueId().equals(player2.getUniqueId())) {
            return true;
        }

        FPlayer fplayer1 = getFPlayer(player1);
        FPlayer fplayer2 = getFPlayer(player2);
        Relation relation = fplayer1.relationTo(fplayer2);
        return (relation.isMember() || relation.isAlly());
    }

    @Override
    public boolean isEnemy(@NotNull OfflinePlayer player1, @NotNull OfflinePlayer player2) {
        if (player1.getUniqueId().equals(player2.getUniqueId())) {
            return false;
        }

        FPlayer fplayer1 = getFPlayer(player1);
        FPlayer fplayer2 = getFPlayer(player2);
        Relation relation = fplayer1.relationTo(fplayer2);
        return relation.isEnemy();
    }

    @Override
    public boolean hasBypass(@NotNull OfflinePlayer player) {
        FPlayer fplayer = getFPlayer(player);
        return fplayer.adminBypass();
    }

    @Override
    public boolean isInEnemyLand(@NotNull OfflinePlayer player, @NotNull Location location) {
        FPlayer fplayer = getFPlayer(player);
        FLocation flocation = new FLocation(location);
        Faction faction = flocation.faction();

        Relation relation = fplayer.relationTo(faction);
        return relation.isEnemy();
    }

    @Override
    public boolean isInOwnFaction(@NotNull OfflinePlayer player, @NotNull Location location) {
        FactionWrapper faction = getFactionAt(location);
        return faction.isMember(player);
    }

    @Override
    public @NotNull ChatColor getRelationChatColor(@NotNull OfflinePlayer viewer, @NotNull OfflinePlayer player) {
        FPlayer fviewer = getFPlayer(viewer);
        FPlayer fplayer = getFPlayer(player);
        Relation relation = fviewer.relationTo(fplayer);

        TextColor textColor = relation.color();
        NamedTextColor nearestColor = NamedTextColor.nearestTo(textColor);
        return ChatColor.valueOf(nearestColor.toString().toUpperCase(Locale.US));
    }

    @Override
    public @NotNull String getRolePrefix(@NotNull OfflinePlayer player) {
        FPlayer fplayer = getFPlayer(player);
        Role role = fplayer.role();
        return role.getPrefix();
    }

    private @NotNull FPlayer getFPlayer(@NotNull OfflinePlayer player) {
        return FPlayers.fPlayers().get(player);
    }

    private @Nullable FactionWrapper wrap(Faction faction) {
        if (faction == null) {
            return null;
        }

        return new FactionWrapper_UUID4(faction);
    }
}
