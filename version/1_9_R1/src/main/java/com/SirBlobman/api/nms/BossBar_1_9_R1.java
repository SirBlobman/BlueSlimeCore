package com.SirBlobman.api.nms;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public class BossBar_1_9_R1 extends BossBar {
	private static final Map<UUID, org.bukkit.boss.BossBar> BOSS_BARS = new HashMap<>();
	public BossBar_1_9_R1(Player player, String title, double progress, String barColor, String barStyle) {super(player, title, progress, barColor, barStyle);}

	@Override
	public BarColor getBarColor() {
		String barColorString = getBarColorString();
		if(barColorString == null || barColorString.isEmpty()) return BarColor.BLUE;
		return BarColor.valueOf(barColorString);
	}

	@Override
	public BarStyle getBarStyle() {
		String barStyleString = getBarStyleString();
		if(barStyleString == null || barStyleString.isEmpty()) return BarStyle.SOLID;
		return BarStyle.valueOf(barStyleString);
	}

	@Override
	public void send() {
		Player player = getPlayer();
		UUID uuid = player.getUniqueId();
		
		org.bukkit.boss.BossBar bossBar = BOSS_BARS.getOrDefault(uuid, null);
		if(bossBar == null) bossBar = Bukkit.createBossBar(getTitle(), getBarColor(), getBarStyle());
		
		bossBar.setColor(getBarColor());
		bossBar.setStyle(getBarStyle());
		bossBar.setTitle(getTitle());
		bossBar.setProgress(getProgress());
		
		bossBar.addPlayer(player);
		bossBar.setVisible(true);
		BOSS_BARS.put(uuid, bossBar);
		
		setCurrentBossBar(player, this);
	}

	@Override
	public void remove() {
		OfflinePlayer player = getOfflinePlayer();
		UUID uuid = player.getUniqueId();
		
		org.bukkit.boss.BossBar bossBar = BOSS_BARS.getOrDefault(uuid, null);
		if(bossBar == null) return;
		
		bossBar.setVisible(false);
		if(player.isOnline()) bossBar.removePlayer(player.getPlayer());
		BOSS_BARS.remove(uuid);
	}
}