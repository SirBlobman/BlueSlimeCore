package com.github.sirblobman.api.utility.paper;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.serializer.gson.GsonComponentSerializer;

public final class ComponentConverter {
    public static @NotNull Component normalToShaded(@NotNull net.kyori.adventure.text.Component component) {
        String gson = normalToGSON(component);
        return gsonToShaded(gson);
    }

    public static @NotNull net.kyori.adventure.text.Component shadedToNormal(@NotNull Component component) {
        String gson = shadedToGSON(component);
        return gsonToNormal(gson);
    }

    public static @NotNull String normalToGSON(@NotNull net.kyori.adventure.text.Component component) {
        net.kyori.adventure.text.serializer.gson.GsonComponentSerializer serializer =
                net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson();
        return serializer.serialize(component);
    }

    public static @NotNull net.kyori.adventure.text.Component gsonToNormal(@NotNull String gson) {
        net.kyori.adventure.text.serializer.gson.GsonComponentSerializer serializer =
                net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson();
        return serializer.deserialize(gson);
    }

    public static @NotNull String shadedToGSON(@NotNull Component component) {
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        return serializer.serialize(component);
    }

    public static @NotNull Component gsonToShaded(@NotNull String gson) {
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        return serializer.deserialize(gson);
    }
}
