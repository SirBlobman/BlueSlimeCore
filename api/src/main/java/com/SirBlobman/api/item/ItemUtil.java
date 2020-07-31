package com.SirBlobman.api.item;

import java.util.List;

import com.SirBlobman.api.utility.Util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class ItemUtil {
    public static ItemStack getAir() {
        return new ItemStack(Material.AIR);
    }
    
    public static ItemStack newItem(Material type) {
        return newItem(type, 1);
    }
    
    public static ItemStack newItem(Material type, int amount) {
        return newItem(type, amount, 0);
    }
    
    public static ItemStack newItem(Material type, int amount, int damage) {
        return newItem(type, amount, damage, null);
    }
    
    public static ItemStack newItem(Material type, int amount, int damage, String displayName, String... loreArray) {
        List<String> loreList = Util.newList(loreArray);
        return newItem(type, amount, damage, displayName, loreList);
    }
    
    public static ItemStack newItem(Material type, int amount, int damage, String displayName, List<String> loreList) {
        ItemBuilder builder = new ItemBuilder().setType(type).setAmount(amount).setDamage(damage);
        if(displayName != null) builder = builder.setDisplayName(displayName);
        if(loreList != null) builder.setLore(loreList);
        
        return builder.create();
    }
    
    public static ItemStack newPotion(Material type, PotionEffectType mainEffect, PotionEffect[] extraEffectArray, String displayName, String... loreArray) {
        ItemStack item = newItem(type, 1, 0, displayName, loreArray);
        ItemMeta meta = item.getItemMeta();
        if(!(meta instanceof PotionMeta)) return item;
        
        PotionMeta potionMeta = (PotionMeta) meta;
        potionMeta.setMainEffect(mainEffect);
        for(PotionEffect effect : extraEffectArray) potionMeta.addCustomEffect(effect, true);
        
        item.setItemMeta(potionMeta);
        return item;
    }
    
    public static boolean isAir(ItemStack item) {
        if(item == null) return true;
        if(item.equals(getAir())) return true;
        
        Material type = item.getType();
        String typeName = type.name();
        return (type == Material.AIR || typeName.endsWith("_AIR"));
    }
    
    public static boolean hasMeta(ItemStack item) {
        if(isAir(item)) return false;
        return item.hasItemMeta();
    }
    
    public static boolean hasLore(ItemStack item) {
        if(!hasMeta(item)) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.hasLore();
    }
    
    public static boolean hasDisplayName(ItemStack item) {
        if(!hasMeta(item)) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.hasDisplayName();
    }
    
    public static boolean doesAnyLoreLineContain(ItemStack item, String string) {
        if(!hasLore(item)) return false;
        
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = meta.getLore();
        
        for(String lore : loreList) {
            if(!lore.contains(string)) continue;
            return true;
        }
        return false;
    }
    
    public static boolean doesAnyLoreLineStartWith(ItemStack item, String string) {
        if(!hasLore(item)) return false;
        
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = meta.getLore();
        
        for(String lore : loreList) {
            if(!lore.startsWith(string)) continue;
            return true;
        }
        return false;
    }
    
    public static boolean doesAnyLoreLineEndWith(ItemStack item, String string) {
        if(!hasLore(item)) return false;
        
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = meta.getLore();
        
        for(String lore : loreList) {
            if(!lore.endsWith(string)) continue;
            return true;
        }
        return false;
    }
    
    public static boolean areLoresEqual(ItemStack item1, ItemStack item2) {
        if(!hasLore(item1) || !hasLore(item2)) return false;
        
        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();
        
        List<String> lore1 = meta1.getLore();
        List<String> lore2 = meta2.getLore();
        return lore1.containsAll(lore2);
    }
    
    public static boolean areNamesEqual(ItemStack item1, ItemStack item2) {
        if(!hasDisplayName(item1) || !hasDisplayName(item2)) return false;
        
        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();
        
        String display1 = meta1.getDisplayName();
        String display2 = meta2.getDisplayName();
        return display1.equals(display2);
    }
}