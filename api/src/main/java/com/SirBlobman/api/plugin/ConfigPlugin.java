package com.SirBlobman.api.plugin;

import com.SirBlobman.api.configuration.ConfigManager;
import com.SirBlobman.api.configuration.PlayerDataManager;

public interface ConfigPlugin {
    ConfigManager<?> getConfigManager();
    PlayerDataManager<?> getPlayerDataManager();
}