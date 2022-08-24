package com.github.sirblobman.api.nbt;

public final class CustomNbtContext_1_8_R3 implements CustomNbtContext {
    private final CustomNbtTypeRegistry_1_8_R3 registry;

    public CustomNbtContext_1_8_R3(CustomNbtTypeRegistry_1_8_R3 registry) {
        this.registry = registry;
    }

    @Override
    public CustomNbtContainer_1_8_R3 newCustomNbtContainer() {
        return new CustomNbtContainer_1_8_R3(this.registry);
    }
}
