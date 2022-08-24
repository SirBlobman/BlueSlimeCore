package com.github.sirblobman.api.nbt;

public final class CustomNbtContext_1_9_R2 implements CustomNbtContext {
    private final CustomNbtTypeRegistry_1_9_R2 registry;

    public CustomNbtContext_1_9_R2(CustomNbtTypeRegistry_1_9_R2 registry) {
        this.registry = registry;
    }

    @Override
    public CustomNbtContainer_1_9_R2 newCustomNbtContainer() {
        return new CustomNbtContainer_1_9_R2(this.registry);
    }
}
