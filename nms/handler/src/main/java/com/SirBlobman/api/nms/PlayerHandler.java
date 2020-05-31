package com.SirBlobman.api.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonObject;

public abstract class PlayerHandler {
    private final JavaPlugin plugin;
    public PlayerHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public final JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    protected final String toJSON(String string) {
        if(string == null) toJSON("");
    
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", string);
        return jsonObject.toString();
    }
    
    public abstract void sendActionBar(Player player, String message);
    public abstract void sendTabInfo(Player player, String header, String footer);
    public abstract void forceRespawn(Player player);
    
    public abstract double getAbsorptionHearts(Player player);
    public abstract void setAbsorptionHearts(Player player, double hearts);
    
    public abstract void sendCooldownPacket(Player player, Material material, int ticksLeft);
}
