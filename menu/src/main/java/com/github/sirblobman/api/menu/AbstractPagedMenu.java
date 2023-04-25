package com.github.sirblobman.api.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.IFoliaPlugin;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.TextReplacementConfig;

public abstract class AbstractPagedMenu<P extends Plugin> extends AbstractMenu<P> {
    private int currentPage;

    public AbstractPagedMenu(@NotNull IFoliaPlugin<P> plugin, @NotNull Player player) {
        this(null, plugin, player);
    }

    public AbstractPagedMenu(@Nullable IMenu<P> parentMenu, @NotNull IFoliaPlugin<P> plugin, @NotNull Player player) {
        super(parentMenu, plugin, player);
        this.currentPage = 1;
    }

    @Override
    public Component getTitle() {
        Component titleFormat = getTitleFormat();
        if (titleFormat == null) {
            return null;
        }

        TextReplacementConfig.Builder replacementBuilder = TextReplacementConfig.builder();
        replacementBuilder.matchLiteral("{page}").replacement(Component.text(currentPage));
        TextReplacementConfig replacementConfig = replacementBuilder.build();
        return titleFormat.replaceText(replacementConfig);
    }

    public final int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int page) {
        this.currentPage = page;
    }

    public final void openNextPage() {
        int currentPage = getCurrentPage();
        int maximumPage = getMaxPages();

        int newPage = Math.min(currentPage + 1, maximumPage);
        if (currentPage == newPage) {
            return;
        }

        setCurrentPage(newPage);
        open();
    }

    public final void openPreviousPage() {
        int currentPage = getCurrentPage();
        int newPage = Math.max(currentPage - 1, 1);
        if (currentPage == newPage) {
            return;
        }

        setCurrentPage(newPage);
        open();
    }

    public abstract int getMaxPages();

    public abstract Component getTitleFormat();
}
