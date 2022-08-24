package com.github.sirblobman.api.language.sound;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.utility.Validate;

import com.cryptomorin.xseries.XSound;
import org.jetbrains.annotations.NotNull;

public final class XSoundInfo extends SoundInfo {
    private XSound sound;

    public XSoundInfo(XSound sound) {
        this.sound = Validate.notNull(sound, "sound must not be null!");
    }

    @NotNull
    public XSound getSound() {
        return this.sound;
    }

    public void setSound(@NotNull XSound sound) {
        this.sound = sound;
    }

    @Override
    public void play(Player player) {
        float volume = getVolume();
        float pitch = getPitch();
        XSound sound = getSound();
        sound.play(player, volume, pitch);
    }
}
