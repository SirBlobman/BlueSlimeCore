package com.SirBlobman.api.nms;

import com.SirBlobman.api.nms.boss.bar.BossBarHandler;

public class NMS_1_13_R2 extends NMS_Handler {
    private final PlayerHandler playerHandler = new PlayerHandler_1_13_R2();
    private final EntityHandler entityHandler = new EntityHandler_1_13_R2();
    private final BossBarHandler bossBarHandler = new BossBarHandler_1_13_R2();
    private final ScoreboardHandler scoreboardHandler = new ScoreboardHandler_1_13_R2();
    
    @Override
    public BossBarHandler getBossBarHandler() {
        return this.bossBarHandler;
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
}