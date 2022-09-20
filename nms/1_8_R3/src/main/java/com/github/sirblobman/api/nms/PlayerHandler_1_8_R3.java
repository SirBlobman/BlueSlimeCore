package com.github.sirblobman.api.nms;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.language.ComponentHelper;

import io.netty.buffer.Unpooled;

public class PlayerHandler_1_8_R3 extends PlayerHandler {
    public PlayerHandler_1_8_R3(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void sendActionBar(Player player, String message) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer nmsPlayer = craftPlayer.getHandle();

        byte actionBar = 2;
        String jsonMessage = asJSON(message);
        IChatBaseComponent chatComponent = ChatSerializer.a(jsonMessage);

        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, actionBar);
        nmsPlayer.playerConnection.sendPacket(packet);
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
        } catch (IOException ex) {
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
        // Do Nothing
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

        InventoryView openInventoryView = player.getOpenInventory();
        Inventory topInventory = openInventoryView.getTopInventory();
        InventoryType inventoryType = topInventory.getType();
        String inventoryTypeName = inventoryType.name().toLowerCase(Locale.US);
        int inventorySize = topInventory.getSize();

        int containerId = entityPlayer.activeContainer.windowId;
        String inventoryTypeId = ("minecraft:" + inventoryTypeName);
        IChatBaseComponent nmsTitle = convertComponent(title);

        Packet<?> packet = new PacketPlayOutOpenWindow(containerId, inventoryTypeId, nmsTitle, inventorySize);
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
