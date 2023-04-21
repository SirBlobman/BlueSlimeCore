package com.github.sirblobman.api.nbt;

import org.jetbrains.annotations.NotNull;

public final class CustomNbtContext_1_12_R1 implements CustomNbtContext {
    private final CustomNbtTypeRegistry_1_12_R1 registry;

    public CustomNbtContext_1_12_R1(@NotNull CustomNbtTypeRegistry_1_12_R1 registry) {
        this.registry = registry;
    }

    @Override
    public @NotNull CustomNbtContainer_1_12_R1 newCustomNbtContainer() {
        return new CustomNbtContainer_1_12_R1(this.registry);
    }
}
