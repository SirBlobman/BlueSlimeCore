package com.github.sirblobman.api.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_15_R1.Containers;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.language.ComponentHelper;


public class PlayerHandler_1_15_R1 extends PlayerHandler {
    public PlayerHandler_1_15_R1(JavaPlugin plugin) {
        super(plugin);
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
        if (!(player instanceof CraftPlayer)) {
            return;
        }

        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        if (entityPlayer.activeContainer == null) {
            return;
        }

        int containerId = entityPlayer.activeContainer.windowId;
        Containers<?> menuType = entityPlayer.activeContainer.getType();
        IChatBaseComponent nmsTitle = convertComponent(title);

        Packet<?> packet = new PacketPlayOutOpenWindow(containerId, menuType, nmsTitle);
        sendPacket(player, packet);
    }

    private void sendPacket(Player player, Packet<?> packet) {
        if (!(player instanceof CraftPlayer)) {
            return;
        }

        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        entityPlayer.playerConnection.sendPacket(packet);
    }

    private IChatBaseComponent convertComponent(Component adventure) {
        String json = ComponentHelper.toGson(adventure);
        return ChatSerializer.a(json);
    }
}
