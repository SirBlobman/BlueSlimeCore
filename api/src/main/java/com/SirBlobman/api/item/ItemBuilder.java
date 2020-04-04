package com.SirBlobman.api.item;

import java.util.List;

import com.SirBlobman.api.utility.MessageUtil;
import com.SirBlobman.api.utility.Util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
    private final ItemStack baseItem;
    private final ItemMeta baseMeta;
    
    public ItemBuilder() {
        this.baseItem = new ItemStack(Material.STONE, 1);
        this.baseMeta = this.baseItem.getItemMeta();
    }
    
    public ItemBuilder(ItemStack base) {
        this.baseItem = new ItemStack(base);
        this.baseMeta = this.baseItem.getItemMeta();
    }
    
    public ItemStack create() {
        if(this.baseItem == null) return new ItemStack(Material.AIR);
        if(this.baseMeta == null) return this.baseItem.clone();
        
        this.baseItem.setItemMeta(this.baseMeta);
        return this.baseItem.clone();
    }
    
    public ItemBuilder setType(Material type) {
        if(this.baseItem == null || type == null) return this;
        
        this.baseItem.setType(type);
        return this;
    }
    
    public ItemBuilder setAmount(int amount) {
        if(this.baseItem == null) return this;
        
        this.baseItem.setAmount(amount);
        return this;
    }
    
    public ItemBuilder setDamage(int damage) {
        if(this.baseItem == null) return this;
        short data = Integer.valueOf(damage).shortValue();
        
        this.baseItem.setDurability(data);
        return this;
    }
    
    public ItemBuilder setDisplayName(String displayName) {
        if(this.baseMeta == null || displayName == null) return this;
        String color = MessageUtil.color(displayName);
        
        this.baseMeta.setDisplayName(color);
        return this;
    }
    
    public ItemBuilder setLore(Iterable<String> loreList) {
        if(this.baseMeta == null || loreList == null) return this;
        
        List<String> colorLore = Util.newList();
        for(String line : loreList) {
            String color = MessageUtil.color(line);
            colorLore.add(color);
        }
        
        this.baseMeta.setLore(colorLore);
        return this;
    }
    
    public ItemBuilder setLore(String... loreArray) {
        if(this.baseMeta == null || loreArray == null) return this;
        
        List<String> loreList = Util.newList(loreArray);
        return setLore(loreList);
    }
    
    public ItemBuilder setFlags(ItemFlag... flagArray) {
        if(this.baseMeta == null || flagArray == null) return this;
        
        this.baseMeta.addItemFlags(flagArray);
        return this;
    }
    
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        if(this.baseMeta == null || enchantment == null || level < 0) return this;
        
        this.baseMeta.addEnchant(enchantment, level, true);
        return this;
    }
    
    public ItemBuilder setGlowing() {
        if(this.baseMeta == null) return this;
        if(!this.baseMeta.getEnchants().isEmpty()) return this;
        
        this.baseMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        return setFlags(ItemFlag.HIDE_ENCHANTS);
    }
}