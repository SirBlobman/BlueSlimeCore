package com.github.sirblobman.api.plugin;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.folia.IFoliaPlugin;
import com.github.sirblobman.api.nms.MultiVersionHandler;

public interface IMultiVersionPlugin extends IFoliaPlugin {
    @NotNull MultiVersionHandler getMultiVersionHandler();
}
