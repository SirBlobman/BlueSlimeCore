package com.github.sirblobman.api.bungeecord.configuration;

import com.github.sirblobman.api.bungeecord.hook.permission.IPermissionHook;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;

import org.jetbrains.annotations.NotNull;

public interface IHookPlugin {
    @NotNull IPermissionHook getDefaultPermissionHook();

    @NotNull IVanishHook getDefaultVanishHook();

    @NotNull IPermissionHook getPermissionHook();

    @NotNull IVanishHook getVanishHook();
}
