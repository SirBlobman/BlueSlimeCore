package com.github.sirblobman.api.core.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.VersionUtility;

import com.cryptomorin.xseries.XMaterial;

public final class CommandItemInfo extends PlayerCommand {
    private final CorePlugin plugin;

    public CommandItemInfo(CorePlugin plugin) {
        super(plugin, "item-info");
        setPermissionName("sirblobman.core.command.item-info");
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(Player player, String[] args) {
        ItemStack item = getHeldItem(player);
        if (ItemUtility.isAir(item)) {
            sendMessage(player, "error.invalid-held-item", null, true);
            return true;
        }

        List<String> messageList = getInformation(item);
        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 13) {
            addLegacyInformation(item, messageList);
        }

        String[] messageArray = messageList.toArray(new String[0]);
        String[] coloredArray = MessageUtility.colorArray(messageArray);
        player.sendMessage(coloredArray);
        return true;
    }

    private String getMaterialNameX(ItemStack item) {
        try {
            XMaterial material = XMaterial.matchXMaterial(item);
            return material.name();
        } catch (IllegalArgumentException ex) {
            return "N/A";
        }
    }

    private String getMaterialNameBukkit(ItemStack item) {
        Material material = item.getType();
        return material.name();
    }

    private String getInternalName(ItemStack item) {
        MultiVersionHandler multiVersionHandler = this.plugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();
        return itemHandler.getKeyString(item);
    }

    @SuppressWarnings("deprecation")
    private int getMaterialId(ItemStack item) {
        Material material = item.getType();
        return material.getId();
    }

    private List<String> getInformation(ItemStack item) {
        String materialNameX = getMaterialNameX(item);
        String materialNameBukkit = getMaterialNameBukkit(item);
        String internalName = getInternalName(item);

        List<String> messageList = new ArrayList<>();
        messageList.add("");
        messageList.add("&f&lXMaterial: &7" + materialNameX);
        messageList.add("&f&lBukkit Material: &7" + materialNameBukkit);
        messageList.add("&f&lInternal Name: &7" + internalName);
        return messageList;
    }

    private void addLegacyInformation(ItemStack item, List<String> messageList) {
        int id = getMaterialId(item);
        short data = item.getDurability();

        messageList.add("&f&lDeprecated ID: &7" + id);
        messageList.add("&f&lData/Meta Value: &7" + data);
    }
}
