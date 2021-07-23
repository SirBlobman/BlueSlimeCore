package com.github.sirblobman.bossbar.legacy;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.sirblobman.bossbar.legacy.reflection.ClassBuilder;
import com.github.sirblobman.bossbar.legacy.reflection.NMSClass;

public final class BossBar extends BukkitRunnable {
    protected static int ENTITY_DISTANCE;
    protected final int ID;
    protected final Player receiver;
    protected String message;
    protected float health;
    protected float healthMinus;
    protected float minHealth;
    protected Location location;
    protected World world;
    protected boolean visible;
    protected Object dataWatcher;
    
    protected BossBar(Player player, String message, float percentage, float minHealth) {
        this.minHealth = 1.0F;
        this.visible = false;
        this.ID = new Random().nextInt();
        this.receiver = player;
        this.message = message;
        this.health = ((percentage / 100.0F) * this.getMaxHealth());
        this.minHealth = minHealth;
        this.world = player.getWorld();
        this.location = makeLocation(player.getLocation());
        
        if (percentage <= minHealth) {
            BossBarAPI.removeBar(player);
        }
    }
    
    protected Location makeLocation(Location base) {
        return base.getDirection().multiply(BossBar.ENTITY_DISTANCE).add(base.toVector()).toLocation(this.world);
    }
    
    public float getMaxHealth() {
        return 300.0F;
    }
    
    public void setHealth(float percentage) {
        this.health = ((percentage / 100.0F) * this.getMaxHealth());
        if (this.health <= this.minHealth) {
            BossBarAPI.removeBar(this.receiver);
        } else {
            this.sendMetadata();
        }
    }
    
    public float getHealth() {
        return this.health;
    }
    
    public void setMessage(String message) {
        this.message = message;
        if (this.isVisible()) {
            this.sendMetadata();
        }
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void run() {
        this.health -= this.healthMinus;
        if (this.health <= this.minHealth) {
            BossBarAPI.removeBar(this.receiver);
        } else {
            this.sendMetadata();
        }
    }
    
    public void setVisible(boolean flag) {
        if (flag == this.visible) {
            return;
        }

        if (flag) {
            this.spawn();
        } else {
            this.destroy();
        }
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    protected void updateMovement() {
        if (!this.visible) {
            return;
        }

        this.location = this.makeLocation(this.receiver.getLocation());
        try {
            Object packet = ClassBuilder.buildTeleportPacket(this.ID, this.getLocation());
            BossBarAPI.sendPacket(this.receiver, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void updateDataWatcher() {
        if (this.dataWatcher == null) {
            try {
                ClassBuilder.setDataWatcherValue(this.dataWatcher = ClassBuilder.buildDataWatcher(null), 17, 0);
                ClassBuilder.setDataWatcherValue(this.dataWatcher, 18, 0);
                ClassBuilder.setDataWatcherValue(this.dataWatcher, 19, 0);
                ClassBuilder.setDataWatcherValue(this.dataWatcher, 20, 1_000);
                ClassBuilder.setDataWatcherValue(this.dataWatcher, 0, 32);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ClassBuilder.setDataWatcherValue(this.dataWatcher, 6, this.health);
            ClassBuilder.setDataWatcherValue(this.dataWatcher, 11, 1);
            ClassBuilder.setDataWatcherValue(this.dataWatcher, 3, 1);
            ClassBuilder.setDataWatcherValue(this.dataWatcher, 10, this.message);
            ClassBuilder.setDataWatcherValue(this.dataWatcher, 2, this.message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void sendMetadata() {
        this.updateDataWatcher();
        try {
            Object metaPacket = ClassBuilder.buildNameMetadataPacket(this.ID, this.dataWatcher, 2, 3, this.message);
            BossBarAPI.sendPacket(this.receiver, metaPacket);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void spawn() {
        try {
            this.updateMovement();
            this.updateDataWatcher();
            Object packet = ClassBuilder.buildWitherSpawnPacket(this.ID, this.getLocation(), this.dataWatcher);
            BossBarAPI.sendPacket(this.receiver, packet);
            this.visible = true;
            this.sendMetadata();
            this.updateMovement();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("JavaReflectionInvocation")
    protected void destroy() {
        try {
            this.cancel();
        }
        catch (IllegalStateException ignored) {}

        try {
            Object parameter = new int[] {ID};
            Object packet = NMSClass.PacketPlayOutEntityDestroy.getConstructor(int[].class).newInstance(parameter);
            BossBarAPI.sendPacket(this.receiver, packet);
            this.visible = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        BossBar.ENTITY_DISTANCE = 32;
    }
}
