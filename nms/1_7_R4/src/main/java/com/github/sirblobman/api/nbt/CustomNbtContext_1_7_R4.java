package com.github.sirblobman.api.nbt;

public final class CustomNbtContext_1_7_R4 implements CustomNbtContext {
    private final CustomNbtTypeRegistry_1_7_R4 registry;

    public CustomNbtContext_1_7_R4(CustomNbtTypeRegistry_1_7_R4 registry) {
        this.registry = registry;
    }

    @Override
    public CustomNbtContainer_1_7_R4 newCustomNbtContainer() {
        return new CustomNbtContainer_1_7_R4(this.registry);
    }
}
