package com.github.sirblobman.api.utility.paper;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
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

    /**
     * @return The server TPS values [1m, 5m, 15m]
     */
    public static double[] getServerTpsValues() {
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

    @Nullable
    public static Component getCustomName(Entity entity) {
        if (entity == null) {
            return null;
        }

        net.kyori.adventure.text.Component paperName = entity.customName();
        if (paperName == null) {
            return null;
        }

        return normalToShaded(paperName);
    }

    public static void setCustomName(Entity entity, Component name) {
        if (entity == null) {
            return;
        }

        if (name == null) {
            entity.customName(null);
            return;
        }

        net.kyori.adventure.text.Component paperName = shadedToNormal(name);
        entity.customName(paperName);
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

    public static Inventory createInventory(InventoryHolder holder, int size, Component title) {
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
