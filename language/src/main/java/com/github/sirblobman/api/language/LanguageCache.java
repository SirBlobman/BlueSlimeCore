package com.github.sirblobman.api.language;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.utility.VersionUtility;

public final class LanguageCache {
    private static final Map<UUID, String> PLAYER_LOCALE_CACHE = new ConcurrentHashMap<>();

    public static void updateCachedLocale(Player player) {
        int minorVersion = VersionUtility.getMinorVersion();
        UUID playerId = player.getUniqueId();
        String defaultLocale = "default";

        if(minorVersion < 12) {
            PLAYER_LOCALE_CACHE.put(playerId, defaultLocale);
        } else {
            String localeName = player.getLocale();
            PLAYER_LOCALE_CACHE.put(playerId, localeName == null ? defaultLocale : localeName);
        }
    }

    public static void removeCachedLocale(Player player) {
        UUID playerId = player.getUniqueId();
        PLAYER_LOCALE_CACHE.remove(playerId);
    }

    public static String getCachedLocale(Player player) {
        UUID playerId = player.getUniqueId();
        if(!PLAYER_LOCALE_CACHE.containsKey(playerId)) {
            updateCachedLocale(player);
        }

        String defaultLocale = "default";
        return PLAYER_LOCALE_CACHE.getOrDefault(playerId, defaultLocale);
    }
}
