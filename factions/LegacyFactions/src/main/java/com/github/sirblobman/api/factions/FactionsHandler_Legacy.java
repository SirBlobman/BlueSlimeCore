package com.github.sirblobman.api.factions;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import net.redstoneore.legacyfactions.Relation;
import net.redstoneore.legacyfactions.Role;
import net.redstoneore.legacyfactions.entity.FPlayer;
import net.redstoneore.legacyfactions.entity.FPlayerColl;
import net.redstoneore.legacyfactions.entity.Faction;
import net.redstoneore.legacyfactions.locality.Locality;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FactionsHandler_Legacy extends FactionsHandler {
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
        Locality locality = Locality.of(location);
        return wrap(locality.getFactionHere());
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

        if (faction1.getId().equals(faction2.getId())) {
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

        return fplayer.isAdminBypassing();
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

        Locality locality = Locality.of(location);
        Faction locationFaction = locality.getFactionHere();
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
        return relation.getColor();
    }

    @Override
    public @Nullable String getRolePrefix(@NotNull OfflinePlayer player) {
        FPlayer fplayer = getFPlayer(player);
        if (fplayer == null) {
            return null;
        }

        Role role = fplayer.getRole();
        return role.getPrefix();
    }

    private @Nullable FPlayer getFPlayer(@NotNull OfflinePlayer player) {
        return FPlayerColl.get(player);
    }

    private @Nullable FactionWrapper wrap(Faction faction) {
        if (faction == null) {
            return null;
        }

        return new FactionWrapper_Legacy(faction);
    }
}
