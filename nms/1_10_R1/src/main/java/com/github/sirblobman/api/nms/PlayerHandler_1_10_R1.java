package com.github.sirblobman.api.nms;

import java.util.Locale;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.Item;
import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_10_R1.PacketPlayOutSetCooldown;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.util.CraftMagicNumbers;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.language.ComponentHelper;

public class PlayerHandler_1_10_R1 extends PlayerHandler {
    public PlayerHandler_1_10_R1(JavaPlugin plugin) {
        super(plugin);
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
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer nmsPlayer = craftPlayer.getHandle();

        Item item = CraftMagicNumbers.getItem(material);
        PacketPlayOutSetCooldown packet = new PacketPlayOutSetCooldown(item, ticksLeft);
        nmsPlayer.playerConnection.sendPacket(packet);
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
