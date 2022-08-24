package com.github.sirblobman.api.language.sound;

import org.bukkit.entity.Player;

public abstract class SoundInfo {
    private float volume;
    private float pitch;

    public SoundInfo() {
        this.volume = 1.0F;
        this.pitch = 1.0F;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public abstract void play(Player player);
}
