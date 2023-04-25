package com.github.sirblobman.api.language.listener;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.language.LanguageManager;

public class UpdateLocaleTask<P extends Plugin> extends EntityTaskDetails<P, Player> {
    private final LanguageManager languageManager;

    public UpdateLocaleTask(@NotNull P plugin, @NotNull Player player, @NotNull LanguageManager languageManager) {
        super(plugin, player);
        this.languageManager = languageManager;
    }

    private @NotNull LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    @Override
    public void run() {
        Player player = getEntity();
        if (player == null) {
            cancel();
            return;
        }

        String playerLocale = player.getLocale();
        LanguageManager languageManager = getLanguageManager();
        languageManager.setLocale(player, playerLocale);
    }
}
