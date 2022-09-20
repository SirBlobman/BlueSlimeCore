package com.github.sirblobman.api.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;

import com.github.sirblobman.api.language.ComponentHelper;

import net.kyori.adventure.text.Component;

public final class PlayerHandler_1_19_R1 extends PlayerHandler {
    public PlayerHandler_1_19_R1(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void sendActionBar(Player player, String message) {
        Spigot spigot = player.spigot();
        BaseComponent[] componentMessage = TextComponent.fromLegacyText(message);
        spigot.sendMessage(ChatMessageType.ACTION_BAR, componentMessage);
    }

    @Override
    public void sendTabInfo(Player player, String header, String footer) {
        player.setPlayerListHeaderFooter(header, footer);
    }

    @Override
    public void forceRespawn(Player player) {
        Spigot spigot = player.spigot();
        spigot.respawn();
    }

    @Override
    public double getAbsorptionHearts(Player player) {
        return player.getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionHearts(Player player, double hearts) {
        player.setAbsorptionAmount(hearts);
    }

    @Override
    public void sendCooldownPacket(Player player, Material material, int ticksLeft) {
        player.setCooldown(material, ticksLeft);
    }

    @Override
    public void sendMenuTitleUpdate(Player player, Component title) {
        if (!(player instanceof CraftPlayer craftPlayer)) {
            return;
        }

        ServerPlayer serverPlayer = craftPlayer.getHandle();
        if (serverPlayer.containerMenu == null) {
            return;
        }

        int containerId = serverPlayer.containerMenu.containerId;
        MenuType<?> menuType = serverPlayer.containerMenu.getType();
        net.minecraft.network.chat.Component nmsTitle = convertComponent(title);

        Packet<?> packet = new ClientboundOpenScreenPacket(containerId, menuType, nmsTitle);
        sendPacket(player, packet);
    }

    private void sendPacket(Player player, Packet<?> packet) {
        if (!(player instanceof CraftPlayer craftPlayer)) {
            return;
        }

        ServerPlayer serverPlayer = craftPlayer.getHandle();
        serverPlayer.connection.send(packet);
    }

    private net.minecraft.network.chat.Component convertComponent(Component adventure) {
        String json = ComponentHelper.toGson(adventure);
        return Serializer.fromJson(json);
    }
}
