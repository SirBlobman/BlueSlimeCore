package com.SirBlobman.api.nms;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R2.PacketDataSerializer;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerListHeaderFooter;

import io.netty.buffer.Unpooled;

public class PlayerHandler_1_9_R2 extends PlayerHandler_1_8_R1 {
    @Override
    public void sendActionBar(Player player, String message) {
        BaseComponent[] chatComponent = TextComponent.fromLegacyText(message);
        ChatMessageType actionBar = ChatMessageType.ACTION_BAR;
        
        Spigot spigot = player.spigot();
        spigot.sendMessage(actionBar, chatComponent);
    }
    
    @Override
    public void setTabInfo(Player player, String header, String footer) {
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
            Logger logger = Logger.getLogger("SirBlobmanAPI");
            logger.log(Level.WARNING, "An error occurred while sending a tab packet.", ex);
        }
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
        
        float floatHearts = Double.valueOf(hearts).floatValue();
        entityPlayer.setAbsorptionHearts(floatHearts);
    }
}