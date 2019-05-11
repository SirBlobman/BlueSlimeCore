package com.SirBlobman.api.utility;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class ItemUtil {
    public static final ItemStack AIR = newItem(Material.AIR);
    public static boolean air(ItemStack is) {
        if(is == null) return true;
        if(is.equals(AIR)) return true;
        Material mat = is.getType();
        boolean air = (mat == Material.AIR);
        return air;
    }

    public static ItemStack newItem(Material mat) {
        ItemStack is = new ItemStack(mat);
        return is;
    }

    public static ItemStack newItem(Material mat, int amount) {
        ItemStack is = newItem(mat);
        if(!air(is)) is.setAmount(amount);
        return is;
    }

    public static ItemStack newItem(Material mat, int amount, int data) {
        ItemStack is = newItem(mat, amount);
        if(!air(is)) {
            short meta = (short) data;
            is.setDurability(meta);
        }
        return is;
    }

    public static ItemStack newItem(Material mat, int amount, int data, String name) {
        ItemStack is = newItem(mat, amount, data);
        if(!air(is)) {
            ItemMeta meta = is.getItemMeta();
            String disp = ChatColor.translateAlternateColorCodes('&', name);
            meta.setDisplayName(disp);
            meta.addItemFlags(ItemFlag.values());
            is.setItemMeta(meta);
        }
        return is;
    }

    public static ItemStack newItem(Material mat, int amount, int data, String name, String... lore) {
        ItemStack is = newItem(mat, amount, data, name);
        if(!air(is)) {
            ItemMeta meta = is.getItemMeta();
            for(int i = 0; i < lore.length; i++) {
                String line = lore[i];
                String color = ChatColor.translateAlternateColorCodes('&', line);
                lore[i] = color;
            }
            
            List<String> list = Arrays.asList(lore);
            meta.setLore(list);
            is.setItemMeta(meta);
        }
        return is;
    }

    public static ItemStack newPotion(PotionEffectType main, PotionEffect[] potionEffects, String disp, String... lore) {
        ItemStack is = newItem(Material.POTION, 1, 0, disp, lore);
        ItemMeta meta = is.getItemMeta();
        PotionMeta pm = (PotionMeta) meta;
        for(int i = 0; i < potionEffects.length; i++) {
            PotionEffect next = potionEffects[i];
            pm.addCustomEffect(next, false);
        }
        is.setItemMeta(pm);
        return is;
    }

    public static ItemStack conditionalMetaItem(boolean condition, Material mat, int amount, int metaIfTrue, int metaIfFalse, String disp, String... lore) {
        ItemStack is = newItem(mat, amount, (condition ? metaIfTrue : metaIfFalse), disp, lore);
        return is;
    }
}