package com.SirBlobman.api.nms;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R2.PacketDataSerializer;
import net.minecraft.server.v1_16_R2.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;

import io.netty.buffer.Unpooled;

public class PlayerHandler_1_16_R2 extends PlayerHandler {
    public PlayerHandler_1_16_R2(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public void sendActionBar(Player player, String message) {
        BaseComponent[] componentArray = TextComponent.fromLegacyText(message);
        ChatMessageType actionBar = ChatMessageType.ACTION_BAR;
        
        Spigot spigot = player.spigot();
        spigot.sendMessage(actionBar, componentArray);
    }
    
    @Override
    public void sendTabInfo(Player player, String header, String footer) {
        String jsonHeader = toJSON(header);
        String jsonFooter = toJSON(footer);
    
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
    
        IChatBaseComponent headerComponent = ChatSerializer.a(jsonHeader);
        IChatBaseComponent footerComponent = ChatSerializer.a(jsonFooter);
    
        try {
            PacketDataSerializer packetData = new PacketDataSerializer(Unpooled.buffer());
            packetData.a(headerComponent);
            packetData.a(footerComponent);
        
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            packet.a(packetData);
        
            entityPlayer.playerConnection.sendPacket(packet);
        } catch(IOException ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while sending a tab packet.", ex);
        }
    }
    
    @Override
    public void forceRespawn(Player player) {
        Spigot spigot = player.spigot();
        spigot.respawn();
    }
    
    @Override
    public double getAbsorptionHearts(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        return entityPlayer.getAbsorptionHearts();
    }
    
    @Override
    public void setAbsorptionHearts(Player player, double hearts) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        
        float heartsFloat = (float) hearts;
        entityPlayer.setAbsorptionHearts(heartsFloat);
    }
    
    @Override
    public void sendCooldownPacket(Player player, Material material, int ticksLeft) {
        player.setCooldown(material, ticksLeft);
    }
}