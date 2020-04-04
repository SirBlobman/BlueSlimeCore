package com.SirBlobman.api.nms;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;

import io.netty.buffer.Unpooled;

public class PlayerHandler_1_8_R3 extends PlayerHandler {
    public PlayerHandler_1_8_R3(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public void sendActionBar(Player player, String message) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        
        String json = toJSON(message);
        IChatBaseComponent component = ChatSerializer.a(json);
        byte actionBar = 2;
    
        PacketPlayOutChat actionBarPacket = new PacketPlayOutChat(component, actionBar);
        entityPlayer.playerConnection.sendPacket(actionBarPacket);
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
}