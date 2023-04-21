package com.github.sirblobman.api.plugin;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.nms.MultiVersionHandler;

public interface IMultiVersionPlugin {
    @NotNull MultiVersionHandler getMultiVersionHandler();
}
