package com.github.sirblobman.api.core.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.utility.ItemUtility;
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
            sendMessage(player, "error.invalid-held-item", null);
            return true;
        }

        String materialNameX = getXMaterialName(item);
        String materialNameBukkit = getBukkitMaterialName(item);
        String vanillaId = getVanillaId(item);
        Replacer replacer = message -> message.replace("{material}", materialNameBukkit)
                .replace("{xmaterial}", materialNameX)
                .replace("{vanilla}", vanillaId);
        sendMessage(player, "command.item-info.modern", replacer);

        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 13) {
            String materialIdString = getMaterialIdString(item);
            String dataString = getDataString(item);
            Replacer legacyReplacer = message -> message.replace("{material_id}", materialIdString)
                    .replace("{data}", dataString);
            sendMessage(player, "command.item-info.legacy", legacyReplacer);
        }

        return true;
    }

    private CorePlugin getCorePlugin() {
        return this.plugin;
    }

    private String getXMaterialName(ItemStack item) {
        try {
            XMaterial material = XMaterial.matchXMaterial(item);
            return material.name();
        } catch(IllegalArgumentException ex) {
            return "N/A";
        }
    }

    private String getBukkitMaterialName(ItemStack item) {
        Material material = item.getType();
        return material.name();
    }

    private String getVanillaId(ItemStack item) {
        CorePlugin corePlugin = getCorePlugin();
        MultiVersionHandler multiVersionHandler = corePlugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();
        return itemHandler.getKeyString(item);
    }

    @SuppressWarnings("deprecation")
    private int getMaterialId(ItemStack item) {
        Material material = item.getType();
        return material.getId();
    }

    private String getMaterialIdString(ItemStack item) {
        int materialId = getMaterialId(item);
        return Integer.toString(materialId);
    }

    private String getDataString(ItemStack item) {
        short data = item.getDurability();
        return Short.toString(data);
    }
}
