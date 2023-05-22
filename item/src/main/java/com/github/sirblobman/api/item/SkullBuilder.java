package com.github.sirblobman.api.item;

import org.jetbrains.annotations.NotNull;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.nms.HeadHandler;
import com.github.sirblobman.api.utility.Validate;

public final class SkullBuilder {
    private final HeadHandler headHandler;

    public SkullBuilder(HeadHandler headHandler) {
        this.headHandler = Validate.notNull(headHandler, "headHandler must not be null!");
    }

    public @NotNull HeadHandler getHeadHandler() {
        return this.headHandler;
    }

    public ItemBuilder withTextureUrl(@NotNull String url) {
        HeadHandler headHandler = getHeadHandler();
        ItemStack playerHead = headHandler.getTextureHead(url);
        return new ItemBuilder(playerHead);
    }

    public ItemBuilder withTextureBase64(@NotNull String base64) {
        HeadHandler headHandler = getHeadHandler();
        ItemStack playerHead = headHandler.getBase64Head(base64);
        return new ItemBuilder(playerHead);
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder withOwner(@NotNull String playerName) {
        HeadHandler headHandler = getHeadHandler();
        ItemStack playerHead = headHandler.getPlayerHead(playerName);
        return new ItemBuilder(playerHead);
    }

    public ItemBuilder withOwner(@NotNull OfflinePlayer player) {
        HeadHandler headHandler = getHeadHandler();
        ItemStack playerHead = headHandler.getPlayerHead(player);
        return new ItemBuilder(playerHead);
    }
}
