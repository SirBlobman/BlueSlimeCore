package com.github.sirblobman.api.nms;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import io.netty.buffer.Unpooled;

public class PlayerHandler_1_17_R1 extends PlayerHandler {
    public PlayerHandler_1_17_R1(JavaPlugin plugin) {
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
        IChatBaseComponent headerComponent = IChatBaseComponent.ChatSerializer.a(headerJSON);
        IChatBaseComponent footerComponent = IChatBaseComponent.ChatSerializer.a(footerJSON);

        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer nmsPlayer = craftPlayer.getHandle();
        PacketDataSerializer packetData = new PacketDataSerializer(Unpooled.buffer());
        packetData.a(headerComponent);
        packetData.a(footerComponent);

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(packetData);
        nmsPlayer.b.sendPacket(packet);
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
