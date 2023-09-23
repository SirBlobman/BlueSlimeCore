package com.github.sirblobman.api.core.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.event.ClickEvent;
import com.github.sirblobman.api.shaded.adventure.text.event.HoverEvent;

public final class CommandItemToBase64 extends PlayerCommand {
    private final CorePlugin plugin;

    public CommandItemToBase64(@NotNull CorePlugin plugin) {
        super(plugin, "item-to-base64");
        setPermissionName("blue.slime.core.command.item-to-base64");
        this.plugin = plugin;
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull Player player, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull Player player, String @NotNull [] args) {
        ItemStack item = getHeldItem(player);
        if (ItemUtility.isAir(item)) {
            sendMessage(player, "error.invalid-held-item");
            return true;
        }

        String base64 = getBase64String(item);
        Component message = createCopyable(base64);

        LanguageManager languageManager = getLanguageManager();
        if (languageManager != null) {
            languageManager.sendMessage(player, message);
        } else {
            player.sendMessage(base64);
        }

        return true;
    }

    private @NotNull CorePlugin getCorePlugin() {
        return this.plugin;
    }

    private @NotNull String getBase64String(@NotNull ItemStack stack) {
        ItemHandler itemHandler = getCorePlugin().getMultiVersionHandler().getItemHandler();
        return itemHandler.toBase64String(stack);
    }

    private @NotNull Component createCopyable(@NotNull String text) {
        Component clickToCopy = Component.translatable("chat.copy.click");
        HoverEvent<Component> hoverEvent = HoverEvent.showText(clickToCopy);
        ClickEvent clickEvent = ClickEvent.copyToClipboard(text);
        return Component.text(text).clickEvent(clickEvent).hoverEvent(hoverEvent);
    }
}
