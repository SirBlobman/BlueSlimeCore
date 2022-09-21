package com.github.sirblobman.api.utility.paper;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.sirblobman.api.adventure.adventure.text.Component;

import org.jetbrains.annotations.Nullable;

import static com.github.sirblobman.api.utility.paper.ComponentConverter.normalToShaded;
import static com.github.sirblobman.api.utility.paper.ComponentConverter.shadedToNormal;

public final class PaperHelper {
    public static void setDeathMessage(Component message, PlayerDeathEvent e) {
        net.kyori.adventure.text.Component paperMessage = shadedToNormal(message);
        e.deathMessage(paperMessage);
    }

    @Nullable
    public static Component getDisplayName(ItemStack item) {
        if (item == null) {
            return null;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return null;
        }

        if (itemMeta.hasDisplayName()) {
            net.kyori.adventure.text.Component paperName = itemMeta.displayName();
            if (paperName == null) {
                return null;
            }

            return normalToShaded(paperName);
        }

        return null;
    }

    @Nullable
    public static List<Component> getLore(ItemStack item) {
        if (item == null) {
            return null;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return null;
        }

        if (itemMeta.hasLore()) {
            List<net.kyori.adventure.text.Component> paperLore = itemMeta.lore();
            if (paperLore == null) {
                return null;
            }

            return paperLore.parallelStream().map(ComponentConverter::normalToShaded).collect(Collectors.toList());
        }

        return null;
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
