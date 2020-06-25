package com.massivecraft.factions;

import org.bukkit.ChatColor;

public enum Rel {
    ENEMY, NEUTRAL, TRUCE, ALLY, RECRUIT, MEMBER, OFFICER, LEADER;
    
    public boolean isAtLeast(Rel rel) {
        throw new UnsupportedOperationException("Dummy API");
    }
    
    public ChatColor getColor() {
        throw new UnsupportedOperationException("Dummy API");
    }
    
    public String getPrefix() {
        throw new UnsupportedOperationException("Dummy API");
    }
}