package com.github.sirblobman.api.language.sound;

import com.github.sirblobman.api.utility.Validate;

import com.cryptomorin.xseries.XSound;
import org.jetbrains.annotations.NotNull;

public final class SoundInfo {
    private XSound sound;
    private float volume;
    private float pitch;

    public SoundInfo(XSound sound) {
        this.sound = Validate.notNull(sound, "sound must not be null!");
        this.volume = 1.0F;
        this.pitch = 1.0F;
    }

    @NotNull
    public XSound getSound() {
        return sound;
    }

    public void setSound(XSound sound) {
        this.sound = Validate.notNull(sound, "sound must not be null!");
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
}
