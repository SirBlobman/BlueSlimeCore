package com.github.sirblobman.api.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.adventure.adventure.text.Component;

import com.google.gson.JsonObject;

public abstract class PlayerHandler extends Handler {
    public PlayerHandler(JavaPlugin plugin) {
        super(plugin);
    }

    protected final String asJSON(String text) {
        if (text == null) {
            text = "";
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", text);
        return jsonObject.toString();
    }

    public abstract void forceRespawn(Player player);

    public abstract double getAbsorptionHearts(Player player);

    public abstract void setAbsorptionHearts(Player player, double hearts);

    public abstract void sendActionBar(Player player, String message);

    public abstract void sendTabInfo(Player player, String header, String footer);

    public abstract void sendCooldownPacket(Player player, Material material, int ticksLeft);

    public abstract void sendMenuTitleUpdate(Player player, Component title);
}
