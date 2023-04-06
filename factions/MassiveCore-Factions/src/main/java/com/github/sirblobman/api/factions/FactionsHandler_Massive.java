package com.github.sirblobman.api.factions;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FactionsHandler_Massive extends FactionsHandler {
    @Override
    public boolean hasFaction(@NotNull OfflinePlayer player) {
        MPlayer mplayer = getMPlayer(player);
        if (mplayer == null) {
            return false;
        }

        return mplayer.hasFaction();
    }

    @Override
    public @Nullable FactionWrapper getFactionFor(@NotNull OfflinePlayer player) {
        MPlayer mplayer = getMPlayer(player);
        if (mplayer == null) {
            return null;
        }

        return wrap(mplayer.getFaction());
    }

    @Override
    public @Nullable FactionWrapper getFactionAt(@NotNull Location location) {
        BoardColl boardColl = BoardColl.get();
        PS psLocation = PS.valueOf(location);
        return wrap(boardColl.getFactionAt(psLocation));
    }

    @Override
    public boolean isAlly(@NotNull OfflinePlayer player1, @NotNull OfflinePlayer player2) {
        if (player1.getUniqueId().equals(player2.getUniqueId())) {
            return true;
        }

        MPlayer mplayer1 = getMPlayer(player1);
        MPlayer mplayer2 = getMPlayer(player2);
        if (mplayer1 == null || mplayer2 == null) {
            return false;
        }

        Rel relation = mplayer1.getRelationTo(mplayer2);
        return relation.isAtLeast(Rel.ALLY);
    }

    @Override
    public boolean isEnemy(@NotNull OfflinePlayer player1, @NotNull OfflinePlayer player2) {
        if (player1.getUniqueId().equals(player2.getUniqueId())) {
            return false;
        }

        MPlayer mplayer1 = getMPlayer(player1);
        MPlayer mplayer2 = getMPlayer(player2);
        if (mplayer1 == null || mplayer2 == null) {
            return false;
        }

        Rel relation = mplayer1.getRelationTo(mplayer2);
        return (relation == Rel.ENEMY);
    }

    @Override
    public boolean hasBypass(@NotNull OfflinePlayer player) {
        MPlayer mplayer = getMPlayer(player);
        if (mplayer == null) {
            return false;
        }

        return mplayer.isOverriding();
    }

    @Override
    public boolean isInEnemyLand(@NotNull OfflinePlayer player, @NotNull Location location) {
        MPlayer mplayer = getMPlayer(player);
        if (mplayer == null) {
            return false;
        }

        BoardColl boardColl = BoardColl.get();
        PS psLocation = PS.valueOf(location);
        Faction faction = boardColl.getFactionAt(psLocation);
        if (faction == null) {
            return false;
        }

        Rel relation = mplayer.getRelationTo(faction);
        return (relation == Rel.ENEMY);
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
        MPlayer mviewer = getMPlayer(viewer);
        MPlayer mplayer = getMPlayer(player);
        if (mviewer == null || mplayer == null) {
            return null;
        }

        Rel relation = mviewer.getRelationTo(mplayer);
        return relation.getColor();
    }

    @Override
    public @Nullable String getRolePrefix(@NotNull OfflinePlayer player) {
        MPlayer mplayer = getMPlayer(player);
        if (mplayer == null) {
            return null;
        }

        Rel role = mplayer.getRole();
        return role.getPrefix();
    }

    private @Nullable MPlayer getMPlayer(@NotNull OfflinePlayer player) {
        return MPlayer.get(player);
    }

    private @Nullable FactionWrapper wrap(Faction faction) {
        if (faction == null) {
            return null;
        }

        return new FactionWrapper_Massive(faction);
    }
}
