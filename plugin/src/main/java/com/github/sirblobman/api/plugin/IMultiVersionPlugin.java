package com.github.sirblobman.api.plugin;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.folia.FoliaPlugin;
import com.github.sirblobman.api.nms.MultiVersionHandler;

public interface IMultiVersionPlugin extends FoliaPlugin {
    @NotNull MultiVersionHandler getMultiVersionHandler();
}
