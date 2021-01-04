package com.github.sirblobman.api.nms;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketDataSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutPlayerListHeaderFooter;

import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerHandler_1_13_R2 extends PlayerHandler {
    public PlayerHandler_1_13_R2(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void sendActionBar(Player player, String message) {
        BaseComponent[] baseComponents = TextComponent.fromLegacyText(message);
        Spigot spigot = player.spigot();
        spigot.sendMessage(ChatMessageType.ACTION_BAR, baseComponents);
    }

    @Override
    public void sendTabInfo(Player player, String header, String footer) {
        String headerJSON = asJSON(header);
        String footerJSON = asJSON(footer);
        IChatBaseComponent headerComponent = ChatSerializer.a(headerJSON);
        IChatBaseComponent footerComponent = ChatSerializer.a(footerJSON);

        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer nmsPlayer = craftPlayer.getHandle();
        try {
            PacketDataSerializer packetData = new PacketDataSerializer(Unpooled.buffer());
            packetData.a(headerComponent);
            packetData.a(footerComponent);

            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            packet.a(packetData);
            nmsPlayer.playerConnection.sendPacket(packet);
        } catch(IOException ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while sending a tab packet:", ex);
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
        player.setCooldown(material, ticksLeft);
    }
}