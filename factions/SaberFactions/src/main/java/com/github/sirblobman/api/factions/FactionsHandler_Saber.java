package com.github.sirblobman.api.factions;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FactionsHandler_Saber extends FactionsHandler {
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
        Board board = Board.getInstance();
        FLocation flocation = FLocation.wrap(location);
        return wrap(board.getFactionAt(flocation));
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

        Relation relation = fplayer1.getRelationTo(fplayer2);
        return (relation.isMember() || relation.isAlly());
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

        Relation relation = fplayer1.getRelationTo(fplayer2);
        return relation.isEnemy();
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

        Board board = Board.getInstance();
        FLocation flocation = FLocation.wrap(location);
        Faction faction = board.getFactionAt(flocation);
        if (faction == null) {
            return false;
        }

        Relation relation = fplayer.getRelationTo(faction);
        return relation.isEnemy();
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

        Relation relation = fviewer.getRelationTo(fplayer);
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
        FPlayers fplayers = FPlayers.getInstance();
        return fplayers.getByOfflinePlayer(player);
    }

    private @Nullable FactionWrapper wrap(Faction faction) {
        if (faction == null) {
            return null;
        }

        return new FactionWrapper_Saber(faction);
    }
}
