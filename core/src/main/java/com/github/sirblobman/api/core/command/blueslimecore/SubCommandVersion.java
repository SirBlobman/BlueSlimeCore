package com.github.sirblobman.api.core.command.blueslimecore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.api.command.ConsoleCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.update.HangarUpdateManager;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.TextComponent;
import com.github.sirblobman.api.shaded.adventure.text.format.NamedTextColor;
import com.github.sirblobman.api.shaded.adventure.text.format.TextDecoration;

public final class SubCommandVersion extends ConsoleCommand {
    private final CorePlugin plugin;

    public SubCommandVersion(@NotNull CorePlugin plugin) {
        super(plugin, "version");
        setPermissionName("blue.slime.core.command.blueslimecore.version");
        this.plugin = plugin;
    }

    @Override
    protected @NotNull LanguageManager getLanguageManager() {
        CorePlugin plugin = getCorePlugin();
        return plugin.getLanguageManager();
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull ConsoleCommandSender sender, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull ConsoleCommandSender sender, String @NotNull [] args) {
        List<Component> messageList = new ArrayList<>();
        messageList.add(Component.empty());

        addJavaVersionInformation(messageList);
        messageList.add(Component.empty());
        addServerVersionInformation(messageList);
        messageList.add(Component.empty());
        addDependencyInformation(messageList);
        messageList.add(Component.empty());
        addPluginVersionInformation(messageList);
        messageList.add(Component.empty());

        LanguageManager languageManager = getLanguageManager();
        for (Component message : messageList) {
            languageManager.sendMessage(sender, message);
        }

        return true;
    }

    private @NotNull CorePlugin getCorePlugin() {
        return this.plugin;
    }

    private @NotNull Component withPrefix(@NotNull String prefix, @Nullable String value) {
        TextComponent.Builder builder = Component.text().color(NamedTextColor.WHITE);
        builder.append(Component.text(prefix).decorate(TextDecoration.BOLD));
        builder.append(Component.text(":").decorate(TextDecoration.BOLD));
        builder.appendSpace();

        if (value != null) {
            builder.append(Component.text(value, NamedTextColor.GRAY));
        } else {
            builder.append(Component.text("N/A", NamedTextColor.GRAY));
        }

        return builder.build();
    }

    private @NotNull Component listElement(@NotNull String value) {
        TextComponent.Builder builder = Component.text().color(NamedTextColor.GRAY);
        builder.append(Component.text(" - ", NamedTextColor.WHITE, TextDecoration.BOLD));
        builder.append(Component.text(value));
        return builder.build();
    }

    private void addJavaVersionInformation(@NotNull List<Component> list) {
        try {
            String javaVersion = System.getProperty("java.version");
            String javaVendor = System.getProperty("java.vendor");
            String javaURL = System.getProperty("java.url");

            list.add(withPrefix("Java Version", javaVersion));
            list.add(withPrefix("Java Vendor", javaVendor));
            list.add(withPrefix("Java URL", javaURL));
        } catch (SecurityException | IllegalArgumentException | NullPointerException ignored) {
            list.add(withPrefix("Java Version", "Unknown"));
        }
    }

    private void addServerVersionInformation(@NotNull List<Component> list) {
        CorePlugin plugin = getCorePlugin();
        Server server = plugin.getServer();
        String version = server.getVersion();
        String bukkitVersion = server.getBukkitVersion();
        String minecraftVersion = VersionUtility.getMinecraftVersion();
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();

        list.add(withPrefix("Server Version", version));
        list.add(withPrefix("Bukkit Version", bukkitVersion));
        list.add(withPrefix("Minecraft Version", minecraftVersion));
        list.add(withPrefix("NMS Version", nmsVersion));
    }

    private void addDependencyInformation(@NotNull List<Component> list) {
        CorePlugin plugin = getCorePlugin();
        PluginDescriptionFile description = plugin.getDescription();
        list.add(Component.text("Dependency Information:", NamedTextColor.WHITE, TextDecoration.BOLD));

        List<String> loadBeforeList = description.getLoadBefore();
        List<String> softDependList = description.getSoftDepend();
        List<String> dependList = description.getDepend();

        List<String> fullDependencyList = new ArrayList<>(loadBeforeList);
        fullDependencyList.addAll(softDependList);
        fullDependencyList.addAll(dependList);

        if (fullDependencyList.isEmpty()) {
            list.add(listElement("None"));
            return;
        }

        Component missingText = Component.text("(not installed)", NamedTextColor.RED);
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (String dependencyName : fullDependencyList) {
            Plugin dependency = pluginManager.getPlugin(dependencyName);
            if(dependency == null) {
                list.add(listElement(dependencyName).append(Component.space()).append(missingText));
                continue;
            }

            PluginDescriptionFile dependencyDescription = dependency.getDescription();
            String dependencyFullName = dependencyDescription.getFullName();
            list.add(listElement(dependencyFullName));
        }
    }

    private void addPluginVersionInformation(@NotNull List<Component> list) {
        String localVersion = getPluginVersion();
        String remoteVersion = getRemoteVersion();

        list.add(Component.text("BlueSlimeCore by SirBlobman", NamedTextColor.WHITE, TextDecoration.BOLD));
        list.add(withPrefix("Local Version", localVersion));
        list.add(withPrefix("Remote Version", remoteVersion));
    }

    private @NotNull String getPluginVersion() {
        CorePlugin plugin = getCorePlugin();
        PluginDescriptionFile description = plugin.getDescription();
        return description.getVersion();
    }

    private @NotNull String getRemoteVersion() {
        CorePlugin plugin = getCorePlugin();
        HangarUpdateManager updateManager = plugin.getHangarUpdateManager();
        String remoteVersion = updateManager.getCachedHangarVersion(plugin);
        return (remoteVersion != null ? remoteVersion : "Not Available");
    }
}
