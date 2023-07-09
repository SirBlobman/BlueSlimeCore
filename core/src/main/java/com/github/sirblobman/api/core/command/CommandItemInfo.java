package com.github.sirblobman.api.core.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.replacer.IntegerReplacer;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class CommandItemInfo extends PlayerCommand {
    private final CorePlugin plugin;

    public CommandItemInfo(@NotNull CorePlugin plugin) {
        super(plugin, "item-info");
        addAliases("iteminfo", "iteminformation", "item-information");
        setPermissionName("blue.slime.core.command.item-info");
        setDescription("A debug command for showing information about the item you are holding.");
        setUsage("/<command>");
        this.plugin = plugin;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull Player player, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull Player player, String @NotNull [] args) {
        ItemStack item = getHeldItem(player);
        if (ItemUtility.isAir(item)) {
            sendMessage(player, "error.invalid-held-item");
            return true;
        }

        String materialNameX = getMaterialNameX(item);
        String materialNameBukkit = getMaterialNameBukkit(item);
        String vanillaId = getVanillaId(item);

        Replacer materialReplacer = new StringReplacer("{material}", materialNameBukkit);
        Replacer xmaterialReplacer = new StringReplacer("{xmaterial}", materialNameX);
        Replacer vanillaIdReplacer = new StringReplacer("{vanilla}", vanillaId);
        sendMessage(player, "command.item-info.modern", materialReplacer, xmaterialReplacer, vanillaIdReplacer);

        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 13) {
            int materialId = getMaterialIdLegacy(item);
            short data = item.getDurability();

            Replacer materialIdReplacer = new IntegerReplacer("{material_id}", materialId);
            Replacer materialDataReplacer = new IntegerReplacer("{data}", data);
            sendMessage(player, "command.item-info.legacy", materialIdReplacer, materialDataReplacer);
        }

        return true;
    }

    private @NotNull CorePlugin getCorePlugin() {
        return this.plugin;
    }

    private @NotNull String getMaterialNameX(@NotNull ItemStack item) {
        try {
            XMaterial material = XMaterial.matchXMaterial(item);
            return material.name();
        } catch (IllegalArgumentException ex) {
            return "N/A";
        }
    }

    private @NotNull String getMaterialNameBukkit(@NotNull ItemStack item) {
        Material material = item.getType();
        return material.name();
    }

    private @NotNull String getVanillaId(@NotNull ItemStack item) {
        CorePlugin plugin = getCorePlugin();
        MultiVersionHandler multiVersionHandler = plugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();
        return itemHandler.getKeyString(item);
    }

    @SuppressWarnings("deprecation")
    private int getMaterialIdLegacy(@NotNull ItemStack item) {
        Material material = item.getType();
        return material.getId();
    }
}
