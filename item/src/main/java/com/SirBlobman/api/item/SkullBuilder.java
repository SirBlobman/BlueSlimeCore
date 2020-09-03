package com.SirBlobman.api.item;

import org.bukkit.inventory.ItemStack;

import com.SirBlobman.api.nms.HeadHandler;
import com.SirBlobman.api.utility.Validate;

public final class SkullBuilder {
    public static ItemBuilder withOwner(HeadHandler headHandler, String username) {
        Validate.notNull(headHandler, "headHandler must not be null!");
        ItemStack playerHead = headHandler.getPlayerHead(username);
        return new ItemBuilder(playerHead);
    }

    public static ItemBuilder withTexture(HeadHandler headHandler, String base64) {
        Validate.notNull(headHandler, "headHandler must not be null!");
        ItemStack playerHead = headHandler.getBase64Head(base64);
        return new ItemBuilder(playerHead);
    }
}