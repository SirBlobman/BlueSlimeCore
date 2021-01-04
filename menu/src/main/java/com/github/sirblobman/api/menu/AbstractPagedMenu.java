package com.github.sirblobman.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractPagedMenu extends AbstractMenu {
    private int currentPage;
    public AbstractPagedMenu(JavaPlugin plugin, Player player) {
        super(plugin, player);
        this.currentPage = 1;
    }

    @Override
    public String getTitle() {
        String titleFormat = getTitleFormat();
        if(titleFormat == null) return null;

        int currentPage = getCurrentPage();
        String currentPageString = Integer.toString(currentPage);
        return titleFormat.replace("{page}", currentPageString);
    }

    public final int getCurrentPage() {
        return this.currentPage;
    }

    public final void openNextPage() {
        int currentPage = getCurrentPage();
        int maxPages = getMaxPages();
        int newPage = Math.min(maxPages, currentPage + 1);
        if(currentPage == newPage) return;

        this.currentPage = newPage;
        open();
    }

    public final void openPreviousPage() {
        int currentPage = getCurrentPage();
        int newPage = Math.max(1, currentPage - 1);
        if(currentPage == newPage) return;

        this.currentPage = newPage;
        open();
    }

    public abstract int getMaxPages();
    public abstract String getTitleFormat();
}