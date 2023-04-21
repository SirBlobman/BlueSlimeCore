package com.github.sirblobman.api.utility.paper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.sirblobman.api.shaded.adventure.text.Component;

import static com.github.sirblobman.api.utility.paper.ComponentConverter.normalToShaded;
import static com.github.sirblobman.api.utility.paper.ComponentConverter.shadedToNormal;

public final class PaperHelper {
    public static void setDeathMessage(@NotNull Component message, @NotNull PlayerDeathEvent e) {
        net.kyori.adventure.text.Component paperMessage = shadedToNormal(message);
        e.deathMessage(paperMessage);
    }

    /**
     * @return The server TPS values [1m, 5m, 15m]
     */
    public static double @NotNull [] getServerTpsValues() {
        Server server = Bukkit.getServer();
        return server.getTPS();
    }

    /**
     * @return The server TPS value for 1m
     */
    public static double getServer1mTps() {
        Server server = Bukkit.getServer();
        double[] tpsArray = server.getTPS();
        return tpsArray[0];
    }

    public static @Nullable Component getCustomName(@NotNull Entity entity) {
        net.kyori.adventure.text.Component paperName = entity.customName();
        if (paperName == null) {
            return null;
        }

        return normalToShaded(paperName);
    }

    public static void setCustomName(@NotNull Entity entity, @Nullable Component name) {
        if (name == null) {
            entity.customName(null);
            return;
        }

        net.kyori.adventure.text.Component paperName = shadedToNormal(name);
        entity.customName(paperName);
    }

    public static @Nullable Component getDisplayName(@NotNull ItemStack item) {
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

    public static @Nullable List<Component> getLore(ItemStack item) {
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

    public static void setDisplayName(@NotNull ItemStack item, @Nullable Component name) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (name == null) {
            itemMeta.displayName(null);
        } else {
            net.kyori.adventure.text.Component paperName = shadedToNormal(name);
            itemMeta.displayName(paperName);
        }

        item.setItemMeta(itemMeta);
    }

    public static void setLore(@NotNull ItemStack item, @Nullable List<Component> lore) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (lore == null) {
            itemMeta.lore(null);
        } else {
            List<net.kyori.adventure.text.Component> paperLore = new ArrayList<>();
            for (Component line : lore) {
                net.kyori.adventure.text.Component loreLine = shadedToNormal(line);
                paperLore.add(loreLine);
            }

            itemMeta.lore(paperLore);
        }

        item.setItemMeta(itemMeta);
    }

    public static @NotNull Inventory createInventory(@Nullable InventoryHolder holder, int size,
                                                     @NotNull Component title) {
        net.kyori.adventure.text.Component paperTitle = shadedToNormal(title);

        if (size == 5) {
            return Bukkit.createInventory(holder, InventoryType.HOPPER, paperTitle);
        }

        if (size < 9) {
            throw new IllegalArgumentException("size must be equal to 5 or at least 9");
        }

        if (size > 54) {
            throw new IllegalArgumentException("size cannot be more than 54");
        }

        if (size % 9 != 0) {
            throw new IllegalArgumentException("size must be equal to 5 or divisible by 9");
        }

        return Bukkit.createInventory(holder, size, paperTitle);
    }
}
