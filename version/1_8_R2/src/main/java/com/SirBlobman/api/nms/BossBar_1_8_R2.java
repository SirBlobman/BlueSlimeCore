package com.SirBlobman.api.nms;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.TextComponent;

import org.inventivetalent.bossbar.BossBarAPI;
import org.inventivetalent.bossbar.BossBarAPI.Color;
import org.inventivetalent.bossbar.BossBarAPI.Style;

public class BossBar_1_8_R2 extends BossBar {
	private static final Map<UUID, org.inventivetalent.bossbar.BossBar> BOSS_BARS = new HashMap<>();
	public BossBar_1_8_R2(Player player, String title, double progress, String barColor, String barStyle) {super(player, title, progress, barColor, barStyle);}

	@Override
	public Color getBarColor() {
		String barColorString = getBarColorString();
		if(barColorString == null || barColorString.isEmpty()) return Color.BLUE;
		return Color.valueOf(barColorString);
	}

	@Override
	public Style getBarStyle() {
		String barStyleString = getBarStyleString();
		if(barStyleString == null || barStyleString.isEmpty()) return Style.PROGRESS;
		return Style.valueOf(barStyleString);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void send() {
		Player player = getPlayer();
		if(player == null) return;
		UUID uuid = player.getUniqueId();
		
		String titleString = getTitle();
		TextComponent title = new TextComponent(titleString);
		float progress = Double.valueOf(getProgress()).floatValue();
		
		org.inventivetalent.bossbar.BossBar bossBar = BOSS_BARS.getOrDefault(uuid, null);
		if(bossBar == null) bossBar = BossBarAPI.addBar(player, title, getBarColor(), getBarStyle(), progress);
		
		bossBar.setColor(getBarColor());
		bossBar.setStyle(getBarStyle());
		bossBar.setMessage(titleString);
		bossBar.setProgress(progress);
		
		bossBar.addPlayer(player);
		bossBar.setVisible(true);
		BOSS_BARS.put(uuid, bossBar);
		
		setCurrentBossBar(player, this);
	}

	@Override
	public void remove() {
		OfflinePlayer player = getOfflinePlayer();
		UUID uuid = player.getUniqueId();
		
		org.inventivetalent.bossbar.BossBar bossBar = BOSS_BARS.getOrDefault(uuid, null);
		if(bossBar == null) return;
		
		bossBar.setVisible(false);
		if(player.isOnline()) bossBar.removePlayer(player.getPlayer());
		BOSS_BARS.remove(uuid);
	}
}