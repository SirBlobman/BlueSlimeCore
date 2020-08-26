package com.SirBlobman.api.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.SirBlobman.api.language.LanguageManager;

public abstract class PlayerCommand extends Command {
    public PlayerCommand(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract List<String> onTabComplete(Player player, String[] args);
    public abstract boolean execute(Player player, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return Collections.emptyList();

        Player player = (Player) sender;
        return onTabComplete(player, args);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            return execute(player, args);
        }

        LanguageManager languageManager = getLanguageManager();
        if(languageManager != null) {
            languageManager.sendMessage(sender, "error.not-player", null, true);
            return true;
        }

        sender.sendMessage("You are not a player!");
        return true;
    }
}