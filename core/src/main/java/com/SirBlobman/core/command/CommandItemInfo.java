package com.SirBlobman.core.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.SirBlobman.api.command.PlayerCommand;
import com.SirBlobman.api.nms.ItemHandler;
import com.SirBlobman.api.nms.MultiVersionHandler;
import com.SirBlobman.api.utility.ItemUtility;
import com.SirBlobman.api.utility.MessageUtility;
import com.SirBlobman.api.utility.VersionUtility;
import com.SirBlobman.core.CorePlugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.cryptomorin.xseries.XMaterial;

public final class CommandItemInfo extends PlayerCommand {
    private final CorePlugin plugin;
    public CommandItemInfo(CorePlugin plugin) {
        super(plugin, "item-info");
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(Player player, String[] args) {
        ItemStack item = getMainItem(player);
        if(ItemUtility.isAir(item)) {
            player.sendMessage("AIR does not have any information.");
            return true;
        }

        List<String> messageList = new ArrayList<>();
        String xMaterialName = getXMaterialName(item);
        messageList.add("&f&lXMaterial: &7" + xMaterialName);

        Material material = item.getType();
        String materialName = material.name();
        messageList.add("&f&lBukkit Material: &7" + materialName);

        MultiVersionHandler multiVersionHandler = this.plugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();
        String keyedName = itemHandler.getKeyString(item);
        messageList.add("&f&lInternal Name: &7" + keyedName);

        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion < 13) {
            @SuppressWarnings("deprecation")
            int id = material.getId();
            messageList.add("&f&lDeprecated ID: &7" + id);

            short data = item.getDurability();
            messageList.add("&f&lData/Meta Value: &7" + data);
        }

        String[] message = MessageUtility.colorArray(messageList.toArray(new String[0]));
        player.sendMessage(message);
        return true;
    }

    @SuppressWarnings("deprecation")
    private ItemStack getMainItem(Player player) {
        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion < 9) return player.getItemInHand();

        PlayerInventory playerInventory = player.getInventory();
        return playerInventory.getItemInMainHand();
    }

    private String getXMaterialName(ItemStack item) {
        try {
            XMaterial xMaterial = XMaterial.matchXMaterial(item);
            return xMaterial.name();
        } catch(NoSuchMethodError | Exception ex) {
            return "N/A";
        }
    }
}