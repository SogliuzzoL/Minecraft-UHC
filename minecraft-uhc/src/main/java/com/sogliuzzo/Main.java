package com.sogliuzzo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sogliuzzo.commands.StartCommand;

public class Main extends JavaPlugin {
    public int time = 0;
    public BukkitRunnable gameRunnable;

    @Override
    public void onEnable() {
        getCommand("start").setExecutor(new StartCommand(this));
    }

    public void broadcastMessage(String msg) {
        String prefix = ChatColor.DARK_RED + "[" + ChatColor.GOLD + "UHC" + ChatColor.DARK_RED + "] "
                + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY;
        Bukkit.broadcastMessage(prefix + msg);
    }
}