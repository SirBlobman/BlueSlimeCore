package com.github.sirblobman.api.language.sound;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

public final class CustomSoundInfo extends SoundInfo {
    private String soundName;

    public CustomSoundInfo(String soundName) {
        this.soundName = Validate.notNull(soundName, "soundName must not be null!");
    }

    @NotNull
    public String getSoundName() {
        return this.soundName;
    }

    public void setSoundName(@NotNull String soundName) {
        this.soundName = soundName;
    }

    @Override
    public void play(Player player) {
        String soundName = getSoundName();
        Location playerLocation = player.getLocation();
        float volume = getVolume();
        float pitch = getPitch();
        player.playSound(playerLocation, soundName, volume, pitch);
    }
}
