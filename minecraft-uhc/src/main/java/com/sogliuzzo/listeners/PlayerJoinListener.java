package com.sogliuzzo.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sogliuzzo.Main;
import com.sogliuzzo.utils.GameStates;

import net.md_5.bungee.api.ChatColor;

public class PlayerJoinListener implements Listener {
    private Main main;

    public PlayerJoinListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.GREEN + "+ " + ChatColor.GRAY + event.getPlayer().getName());
        if (main.gameState == GameStates.WAITING || main.gameState == GameStates.STARTING) {
            event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
        } else {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        }
    }
}
