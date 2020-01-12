package com.SirBlobman.api.nms;

import com.SirBlobman.api.nms.boss.bar.BossBarHandler;

public class NMS_Fallback extends NMS_Handler {
    private final BossBarHandler bossBarHandler = new FallbackBossBarHandler();
    private final PlayerHandler playerHandler = new FallbackPlayerHandler();
    private final EntityHandler entityHandler = new FallbackEntityHandler();
    
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
}