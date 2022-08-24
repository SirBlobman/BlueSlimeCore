package com.github.sirblobman.api.nbt;

public final class CustomNbtContext_1_11_R1 implements CustomNbtContext {
    private final CustomNbtTypeRegistry_1_11_R1 registry;

    public CustomNbtContext_1_11_R1(CustomNbtTypeRegistry_1_11_R1 registry) {
        this.registry = registry;
    }

    @Override
    public CustomNbtContainer_1_11_R1 newCustomNbtContainer() {
        return new CustomNbtContainer_1_11_R1(this.registry);
    }
}
