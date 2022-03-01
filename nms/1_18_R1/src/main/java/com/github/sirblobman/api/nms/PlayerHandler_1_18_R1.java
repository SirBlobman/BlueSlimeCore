package com.github.sirblobman.api.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerHandler_1_18_R1 extends PlayerHandler {
    public PlayerHandler_1_18_R1(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public void sendActionBar(Player player, String message) {
        Spigot spigot = player.spigot();
        BaseComponent[] componentMessage = TextComponent.fromLegacyText(message);
        spigot.sendMessage(ChatMessageType.ACTION_BAR, componentMessage);
    }
    
    @Override
    public void sendTabInfo(Player player, String header, String footer) {
        player.setPlayerListHeaderFooter(header, footer);
    }
    
    @Override
    public void forceRespawn(Player player) {
        Spigot spigot = player.spigot();
        spigot.respawn();
    }
    
    @Override
    public double getAbsorptionHearts(Player player) {
        return player.getAbsorptionAmount();
    }
    
    @Override
    public void setAbsorptionHearts(Player player, double hearts) {
        player.setAbsorptionAmount(hearts);
    }
    
    @Override
    public void sendCooldownPacket(Player player, Material material, int ticksLeft) {
        player.setCooldown(material, ticksLeft);
    }
}
