package com.SirBlobman.api.nms;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.inventivetalent.bossbar.BossBarAPI;

public class NMS_1_8_R3 extends NMS_Handler {
    @Override
    public void sendActionBar(Player player, String message) {
        try {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            EntityPlayer entityPlayer = craftPlayer.getHandle();
            PlayerConnection playerConnection = entityPlayer.playerConnection;
            
            IChatBaseComponent chatComponent = ChatSerializer.a(toJson(message));
            PacketPlayOutChat actionBar = new PacketPlayOutChat(chatComponent, (byte) 2);
            playerConnection.sendPacket(actionBar);
        } catch(Exception ex) {
            Logger.getLogger("SirBlobmanAPI").warning("An error occurred while sending an action bar:");
            ex.printStackTrace();
        }
    }

    @Override
    public void sendNewBossBar(Player player, String title, double progress, String color, String style) {
        if(!Bukkit.getPluginManager().isPluginEnabled("BossBarAPI")) {
            Logger.getLogger("SirBlobmanAPI").warning("BossBarAPI not installed, sending as chat instead!");
            
            String colored = ChatColor.translateAlternateColorCodes('&', title);
            player.sendMessage(colored);
            return;
        }

        BossBarAPI.Color barColor = ((color == null || color.isEmpty()) ? BossBarAPI.Color.BLUE : BossBarAPI.Color.valueOf(color));
        BossBarAPI.Style barStyle = ((style == null || style.isEmpty()) ? BossBarAPI.Style.PROGRESS : BossBarAPI.Style.valueOf(style.contains("SEGMENTED") ? style.replace("SEGMENTED", "NOTCHED") : "PROGRESS"));
        
        BossBar bossBar = new BossBar_1_8_R3(player, title, progress, barColor.name(), barStyle.name());
        bossBar.send();
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