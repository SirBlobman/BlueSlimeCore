package com.SirBlobman.api.nms;

import org.bukkit.plugin.java.JavaPlugin;

public class NMS_Fallback extends AbstractNMS {
    private final ItemHandler itemHandler;
    private final PlayerHandler playerHandler;
    private final EntityHandler entityHandler;
    private final ScoreboardHandler scoreboardHandler;
    public NMS_Fallback(JavaPlugin plugin) {
        super(plugin);
        
        this.itemHandler = new ItemHandler_Fallback(plugin);
        this.playerHandler = new PlayerHandler_Fallback(plugin);
        this.entityHandler = new EntityHandler_Fallback(plugin);
        this.scoreboardHandler = new ScoreboardHandler_Fallback(plugin);
    }
    
    @Override
    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }
    
    @Override
    public EntityHandler getEntityHandler() {
        return this.entityHandler;
    }
    
    @Override
    public ScoreboardHandler getScoreboardHandler() {
        return this.scoreboardHandler;
    }
    
    @Override
    public ItemHandler getItemHandler() {
        return this.itemHandler;
    }
}