package com.SirBlobman.api.nms;

import org.bukkit.plugin.java.JavaPlugin;

public class NMS_1_8_R3 extends AbstractNMS {
    private final ItemHandler itemHandler;
    private final PlayerHandler playerHandler;
    private final EntityHandler entityHandler;
    private final ScoreboardHandler scoreboardHandler;
    public NMS_1_8_R3(JavaPlugin plugin) {
        super(plugin);
        
        this.itemHandler = new ItemHandler_1_8_R3(plugin);
        this.playerHandler = new PlayerHandler_1_8_R3(plugin);
        this.entityHandler = new EntityHandler_1_8_R3(plugin);
        this.scoreboardHandler = new ScoreboardHandler_1_8_R3(plugin);
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