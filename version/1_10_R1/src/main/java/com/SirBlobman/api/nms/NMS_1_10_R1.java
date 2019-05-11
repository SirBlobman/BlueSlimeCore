package com.SirBlobman.api.nms;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_10_R1.PlayerConnection;

public class NMS_1_10_R1 extends NMS_Handler {
    @Override
    public void sendActionBar(Player player, String message) {
        Spigot spigot = player.spigot();
        String color = ChatColor.translateAlternateColorCodes('&', message);
        BaseComponent[] actionMessage = TextComponent.fromLegacyText(color);
        spigot.sendMessage(ChatMessageType.ACTION_BAR, actionMessage);
    }

    @Override
    public void sendBossBar(Player player, String title, double progress, String color, String style) {
        BarColor barColor = ((color == null || color.isEmpty()) ? BarColor.BLUE : BarColor.valueOf(color));
        BarStyle barStyle = ((style == null || style.isEmpty()) ? BarStyle.SOLID : BarStyle.valueOf(style));
        
        BossBar bossBar = new BossBar_1_10_R1(title, progress, barColor, barStyle);
        bossBar.sendTo(player);
    }

    @Override
    public void setTab(Player player, String header, String footer) {
        try {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            EntityPlayer entityPlayer = craftPlayer.getHandle();
            PlayerConnection playerConnection = entityPlayer.playerConnection;
            
            IChatBaseComponent headerComponent = ChatSerializer.a(toJson(header));
            IChatBaseComponent footerComponent = ChatSerializer.a(toJson(footer));
            
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            Class<?> packet_class = packet.getClass();
            Field headerField = packet_class.getDeclaredField("a");
            Field footerField = packet_class.getDeclaredField("b");
            headerField.setAccessible(true);
            footerField.setAccessible(true);
            headerField.set(packet, headerComponent);
            footerField.set(packet, footerComponent);
            headerField.setAccessible(false);
            headerField.setAccessible(false);
            
            playerConnection.sendPacket(packet);
        } catch(Exception ex) {
            Logger.getLogger("SirBlobmanAPI").warning("An error occurred while setting tab headers/footers:");
            ex.printStackTrace();
        }
    }

    @Override
    public double getMaxHealth(LivingEntity entity) {
        return entity.getMaxHealth();
    }

    @Override
    public void setMaxHealth(LivingEntity entity, double maxHealth) {
        entity.setMaxHealth(maxHealth);
    }

    @Override
    public Objective createScoreboardObjective(Scoreboard scoreboard, String name, String criteria, String displayName) {
        Objective obj = scoreboard.registerNewObjective(name, criteria);
        obj.setDisplayName(displayName);
        return obj;
    }
}