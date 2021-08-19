package com.github.sirblobman.api.item;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.nms.HeadHandler;
import com.github.sirblobman.api.utility.Validate;

public final class SkullBuilder {
    public static ItemBuilder withTexture(HeadHandler headHandler, String base64) {
        Validate.notNull(headHandler, "headHandler must not be null!");
        Validate.notEmpty(base64, "base64 must not be empty or null!");
        ItemStack playerHead = headHandler.getBase64Head(base64);
        return new ItemBuilder(playerHead);
    }
    
    public static ItemBuilder withOwner(HeadHandler headHandler, String playerName) {
        Validate.notNull(headHandler, "headHandler must not be null!");
        Validate.notEmpty(playerName, "playerName must not be empty or null!");
        ItemStack playerHead = headHandler.getPlayerHead(playerName);
        return new ItemBuilder(playerHead);
    }
    
    public static ItemBuilder withOwner(HeadHandler headHandler, OfflinePlayer player) {
        Validate.notNull(headHandler, "headHandler must not be null!");
        Validate.notNull(player, "player must not be null!");
        ItemStack playerHead = headHandler.getPlayerHead(player);
        return new ItemBuilder(playerHead);
    }
}
