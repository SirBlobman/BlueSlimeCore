package com.github.sirblobman.api.factions;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import net.prosavage.factionsx.core.CustomRole;
import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.manager.PlayerManager;
import net.prosavage.factionsx.util.Relation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FactionsHandler_X extends FactionsHandler {
    @Override
    public boolean hasFaction(@NotNull OfflinePlayer player) {
        FPlayer fplayer = getFPlayer(player);
        if (fplayer == null) {
            return false;
        }

        return fplayer.hasFaction();
    }

    @Override
    public @Nullable FactionWrapper getFactionFor(@NotNull OfflinePlayer player) {
        if (hasFaction(player)) {
            FPlayer fplayer = getFPlayer(player);
            if (fplayer == null) {
                return null;
            }

            return wrap(fplayer.getFaction());
        }

        return null;
    }

    @Override
    public @Nullable FactionWrapper getFactionAt(@NotNull Location location) {
        Chunk chunk = location.getChunk();
        Faction faction = GridManager.INSTANCE.getFactionAt(chunk);
        return wrap(faction);
    }

    @Override
    public boolean isAlly(@NotNull OfflinePlayer player1, @NotNull OfflinePlayer player2) {
        if (player1.getUniqueId().equals(player2.getUniqueId())) {
            return true;
        }

        FPlayer fplayer1 = getFPlayer(player1);
        FPlayer fplayer2 = getFPlayer(player2);
        if (fplayer1 == null || fplayer2 == null) {
            return false;
        }

        Faction faction1 = fplayer1.getFaction();
        Faction faction2 = fplayer2.getFaction();
        if (faction1 == null || faction2 == null) {
            return false;
        }

        if (faction1.getId() == faction2.getId()) {
            return true;
        }

        Relation relation = faction1.getRelationTo(faction2);
        return (relation == Relation.ALLY);
    }

    @Override
    public boolean isEnemy(@NotNull OfflinePlayer player1, @NotNull OfflinePlayer player2) {
        if (player1.getUniqueId().equals(player2.getUniqueId())) {
            return false;
        }

        FPlayer fplayer1 = getFPlayer(player1);
        FPlayer fplayer2 = getFPlayer(player2);
        if (fplayer1 == null || fplayer2 == null) {
            return false;
        }

        Faction faction1 = fplayer1.getFaction();
        Faction faction2 = fplayer2.getFaction();
        if (faction1 == null || faction2 == null) {
            return false;
        }

        Relation relation = faction1.getRelationTo(faction2);
        return (relation == Relation.ENEMY);
    }

    @Override
    public boolean hasBypass(@NotNull OfflinePlayer player) {
        FPlayer fplayer = getFPlayer(player);
        if (fplayer == null) {
            return false;
        }

        return fplayer.getInBypass();
    }

    @Override
    public boolean isInEnemyLand(@NotNull OfflinePlayer player, @NotNull Location location) {
        FPlayer fplayer = getFPlayer(player);
        if (fplayer == null) {
            return false;
        }

        Faction playerFaction = fplayer.getFaction();
        if (playerFaction == null) {
            return false;
        }

        Chunk chunk = location.getChunk();
        Faction locationFaction = GridManager.INSTANCE.getFactionAt(chunk);
        if (locationFaction == null) {
            return false;
        }

        Relation relation = playerFaction.getRelationTo(locationFaction);
        return (relation == Relation.ENEMY);
    }

    @Override
    public boolean isInOwnFaction(@NotNull OfflinePlayer player, @NotNull Location location) {
        FactionWrapper faction = getFactionAt(location);
        if (faction == null) {
            return false;
        }

        return faction.isMember(player);
    }

    @Override
    public @Nullable ChatColor getRelationChatColor(@NotNull OfflinePlayer viewer, @NotNull OfflinePlayer player) {
        FPlayer fviewer = getFPlayer(viewer);
        FPlayer fplayer = getFPlayer(player);
        if (fviewer == null || fplayer == null) {
            return null;
        }

        Faction viewerFaction = fviewer.getFaction();
        Faction playerFaction = fplayer.getFaction();
        if (viewerFaction == null || playerFaction == null) {
            return null;
        }

        Relation relation = viewerFaction.getRelationTo(playerFaction);
        return getChatColor(relation);
    }

    @Override
    public @Nullable String getRolePrefix(@NotNull OfflinePlayer player) {
        FPlayer fplayer = getFPlayer(player);
        if (fplayer == null) {
            return null;
        }

        CustomRole role = fplayer.getRole();
        return role.getChatTag();
    }

    private @Nullable FPlayer getFPlayer(@NotNull OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        return PlayerManager.INSTANCE.getFPlayer(playerId);
    }

    private @Nullable FactionWrapper wrap(Faction faction) {
        if (faction == null) {
            return null;
        }

        return new FactionWrapper_X(faction);
    }

    private @Nullable ChatColor getChatColor(Relation relation) {
        if (relation == Relation.ALLY) {
            return ChatColor.LIGHT_PURPLE;
        }

        if (relation == Relation.ENEMY) {
            return ChatColor.RED;
        }

        if (relation == Relation.TRUCE) {
            return ChatColor.YELLOW;
        }

        if (relation == Relation.NEUTRAL) {
            return ChatColor.WHITE;
        }

        return null;
    }
}
