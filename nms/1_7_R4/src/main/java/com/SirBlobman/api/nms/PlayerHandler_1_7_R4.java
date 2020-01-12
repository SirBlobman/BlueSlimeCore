package com.SirBlobman.api.nms;

import org.bukkit.entity.Player;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;

public class PlayerHandler_1_7_R4 extends PlayerHandler {
    @Override
    public void sendActionBar(Player player, String message) {
        // ActionBar is not available in 1.7.10
    }
    
    @Override
    public void setTabInfo(Player player, String header, String footer) {
        // Tab Header/Footer is not available in 1.7.10
    }
    
    @Override
    public void forceRespawn(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        
        PacketPlayInClientCommand respawnPacket = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
        entityPlayer.playerConnection.a(respawnPacket);
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