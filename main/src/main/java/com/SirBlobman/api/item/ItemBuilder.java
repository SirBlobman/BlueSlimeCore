package com.SirBlobman.api.item;

import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.SirBlobman.api.utility.Util;

public class ItemBuilder {
	private ItemStack item;
	private ItemMeta meta;
	public ItemBuilder() {this.item = new ItemStack(Material.AIR);}
	
	public ItemStack create() {
		ItemStack item = this.item.clone();
		ItemMeta meta = this.meta.clone();
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemBuilder setType(Material type) {
		this.item.setType(type);
		return this;
	}
	
	public ItemBuilder setAmount(int amount) {
		this.item.setAmount(amount);
		return this;
	}
	
	public ItemBuilder setDamage(int damage) {
		short data = (short) damage;
		this.item.setDurability(data);
		return this;
	}
	
	private void setupMeta() {
		if(this.meta == null) this.meta = this.item.getItemMeta();
	}
	
	public ItemBuilder setDisplayName(String displayName) {
		setupMeta();
		
		String color = Util.color(displayName);
		this.meta.setDisplayName(color);
		return this;
	}
	
	public ItemBuilder setLore(Collection<String> loreList) {
		setupMeta();
		
		List<String> copy = Util.newList(loreList);
		this.meta.setLore(copy);
		return this;
	}
	
	public ItemBuilder setLore(String... loreList) {
		List<String> copy = Util.newList(loreList);
		return setLore(copy);
	}
	
	public ItemBuilder setFlags(ItemFlag... flags) {
		setupMeta();
		
		this.meta.addItemFlags(flags);
		return this;
	}
	
	public ItemBuilder addEnchant(Enchantment enchant, int level) {
		setupMeta();
		
		this.meta.addEnchant(enchant, level, true);
		return this;
	}
}