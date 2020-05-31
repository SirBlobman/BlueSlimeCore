package com.SirBlobman.api.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;

public class PlayerHandler_1_7_R4 extends PlayerHandler {
    public PlayerHandler_1_7_R4(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public void sendActionBar(Player player, String message) {
        String realMessage = ("[Action Bar] " + message);
        player.sendMessage(realMessage);
    }
    
    @Override
    public void sendTabInfo(Player player, String header, String footer) {
        String headerMessage = ("[Tab Header] " + header);
        player.sendMessage(headerMessage);
        
        String footerMessage = ("[Tab Footer] " + footer);
        player.sendMessage(footerMessage);
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
        
        float heartsFloat = (float) hearts;
        entityPlayer.setAbsorptionHearts(heartsFloat);
    }
    
    @Override
    public void sendCooldownPacket(Player player, Material material, int ticksLeft) {
        // Do Nothing
    }
}