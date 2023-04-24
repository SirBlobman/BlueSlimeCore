package com.github.sirblobman.api.update;

import org.jetbrains.annotations.NotNull;

public final class HangarInfo {
    private final String author;
    private final String project;

    public HangarInfo(@NotNull String author, @NotNull String project) {
        this.author = author;
        this.project = project;
    }

    public @NotNull String getAuthor() {
        return author;
    }

    public @NotNull String getProject() {
        return project;
    }
}
