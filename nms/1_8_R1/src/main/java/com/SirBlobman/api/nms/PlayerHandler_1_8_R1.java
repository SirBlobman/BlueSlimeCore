package com.SirBlobman.api.nms;

import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import net.minecraft.server.v1_8_R1.*;

import io.netty.buffer.Unpooled;

public class PlayerHandler_1_8_R1 extends PlayerHandler {
    @Override
    public void sendActionBar(Player player, String message) {
        String json = toJSON(message);
        
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
    
        IChatBaseComponent chatComponent = ChatSerializer.a(json);
        byte actionBar = 2;
        
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, actionBar);
        entityPlayer.playerConnection.sendPacket(packet);
    }
    
    @Override
    public void setTabInfo(Player player, String header, String footer) {
        String jsonHeader = toJSON(header);
        String jsonFooter = toJSON(footer);
        
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        
        IChatBaseComponent headerComponent = ChatSerializer.a(jsonHeader);
        IChatBaseComponent footerComponent = ChatSerializer.a(jsonFooter);
        
        PacketDataSerializer packetData = new PacketDataSerializer(Unpooled.buffer());
        packetData.a(headerComponent);
        packetData.a(footerComponent);
    
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        packet.a(packetData);
        
        entityPlayer.playerConnection.sendPacket(packet);
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
        
        float floatHearts = Double.valueOf(hearts).floatValue();
        entityPlayer.setAbsorptionHearts(floatHearts);
    }
}