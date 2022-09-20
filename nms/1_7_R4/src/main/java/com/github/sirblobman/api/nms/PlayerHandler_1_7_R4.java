package com.github.sirblobman.api.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.language.ComponentHelper;

public final class PlayerHandler_1_7_R4 extends PlayerHandler {
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
        int inventorySize = topInventory.getSize();

        if (inventoryType != InventoryType.CHEST) {
            return;
        }

        int containerId = entityPlayer.activeContainer.windowId;
        String nmsTitle = convertComponent(title);

        Packet packet = new PacketPlayOutOpenWindow(containerId, 0, nmsTitle, inventorySize, true);
        sendPacket(player, packet);
    }

    private void sendPacket(Player player, Packet packet) {
        if (!(player instanceof CraftPlayer)) {
            return;
        }

        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        entityPlayer.playerConnection.sendPacket(packet);
    }

    private String convertComponent(Component adventure) {
        return ComponentHelper.toLegacy(adventure);
    }
}
