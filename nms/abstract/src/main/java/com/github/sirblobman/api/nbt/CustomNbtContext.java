package com.github.sirblobman.api.nbt;

import org.jetbrains.annotations.NotNull;

public interface CustomNbtContext {
    /**
     * Creates a new and empty nbt container instance.
     *
     * @return the new container instance
     */
    @NotNull CustomNbtContainer newCustomNbtContainer();
}
