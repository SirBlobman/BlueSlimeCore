package com.github.sirblobman.api.utility.paper;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.sirblobman.api.adventure.adventure.text.Component;

import static com.github.sirblobman.api.utility.paper.ComponentConverter.shadedToNormal;

public final class PaperHelper {
    public static void setDeathMessage(Component message, PlayerDeathEvent e) {
        net.kyori.adventure.text.Component paperMessage = shadedToNormal(message);
        e.deathMessage(paperMessage);
    }

    public static void setDisplayName(ItemStack item, Component name) {
        if (item == null) {
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        net.kyori.adventure.text.Component paperName = shadedToNormal(name);
        itemMeta.displayName(paperName);
        item.setItemMeta(itemMeta);
    }

    public static void setLore(ItemStack item, List<Component> lore) {
        if (item == null) {
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        List<net.kyori.adventure.text.Component> paperLore = lore.parallelStream()
                .map(ComponentConverter::shadedToNormal).collect(Collectors.toList());
        itemMeta.lore(paperLore);
        item.setItemMeta(itemMeta);
    }
}
