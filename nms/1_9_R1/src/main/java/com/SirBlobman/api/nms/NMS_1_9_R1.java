package com.SirBlobman.api.nms;

import com.SirBlobman.api.nms.boss.bar.BossBarHandler;
import com.SirBlobman.api.nms.boss.bar.BossBarHandler_1_9_R1;
import com.SirBlobman.api.nms.boss.bar.BossBarWrapper;

import org.bukkit.entity.Player;

public class NMS_1_9_R1 extends NMS_1_8_R1 {
    private final BossBarHandler bossBarHandler = new BossBarHandler_1_9_R1();
    private final PlayerHandler playerHandler = new PlayerHandler_1_9_R1();
    
    @Override
    public BossBarHandler getBossBarHandler() {
        return this.bossBarHandler;
    }
    
    @Override
    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }
}