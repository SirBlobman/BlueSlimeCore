package com.github.sirblobman.api.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

public class PlayerHandler_1_7_R4 extends PlayerHandler {
    public PlayerHandler_1_7_R4(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public void sendActionBar(Player player, String message) {
        String newMessage = ("[Action Bar] " + message);
        player.sendMessage(newMessage);
    }
    
    @Override
    public void sendTabInfo(Player player, String header, String footer) {
        String newHeader = ("[Tab Header] " + header);
        String newFooter = ("[Tab Footer] " + footer);
        player.sendMessage(newHeader);
        player.sendMessage(newFooter);
    }
    
    @Override
    public void forceRespawn(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer nmsPlayer = craftPlayer.getHandle();
        PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
        nmsPlayer.playerConnection.a(packet);
    }
    
    @Override
    public double getAbsorptionHearts(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer nmsPlayer = craftPlayer.getHandle();
        return nmsPlayer.getAbsorptionHearts();
    }
    
    @Override
    public void setAbsorptionHearts(Player player, double hearts) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer nmsPlayer = craftPlayer.getHandle();
        nmsPlayer.setAbsorptionHearts((float) hearts);
    }
    
    @Override
    public void sendCooldownPacket(Player player, Material material, int ticksLeft) {
        // Do Nothing
    }
}
