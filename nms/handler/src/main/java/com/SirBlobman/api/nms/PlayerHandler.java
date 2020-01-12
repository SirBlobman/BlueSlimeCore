package com.SirBlobman.api.nms;

import org.bukkit.entity.Player;

import com.google.gson.JsonObject;

public abstract class PlayerHandler {
    protected final String toJSON(String message) {
        if(message == null) message = "";
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", message);
        return jsonObject.toString();
    }
    
    public abstract void sendActionBar(Player player, String message);
    public abstract void setTabInfo(Player player, String header, String footer);
    public abstract void forceRespawn(Player player);
    
    public abstract double getAbsorptionHearts(Player player);
    public abstract void setAbsorptionHearts(Player player, double hearts);
}
