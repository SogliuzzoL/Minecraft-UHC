package com.sogliuzzo;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sogliuzzo.commands.StartCommand;
import com.sogliuzzo.listeners.BlockBreakListener;
import com.sogliuzzo.listeners.EntityDamageByEntityListener;
import com.sogliuzzo.listeners.EntityDamageListener;
import com.sogliuzzo.listeners.PlayerJoinListener;
import com.sogliuzzo.utils.GameStates;

public class Main extends JavaPlugin {
    public int time = 0;
    public BukkitRunnable gameRunnable;
    public World world;
    public GameStates gameState;

    // Border Settings
    public int borderStartTime = 60 * 60; // 1h
    public int borderEndTime = 60 * (60 + 15); // 1h15
    public int borderStartSize = 1000; // -500 à 500
    public int borderEndSize = 100; // -50 à 50

    // FinalHeal and PvP
    public int finalHealTime = 60 * 15; // 15 min
    public int pvpTime = 60 * 20; // 20 min
    public int invincibleTime = 60; // 1 min

    @Override
    public void onEnable() {
        gameState = GameStates.WAITING;
        // Register Commands
        getCommand("start").setExecutor(new StartCommand(this));
        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
        }
        Bukkit.getServer().unloadWorld(Bukkit.getWorld("uhc_world"), false);
    }

    public void broadcastMessage(String msg) {
        String prefix = ChatColor.DARK_RED + "[" + ChatColor.GOLD + "UHC" + ChatColor.DARK_RED + "] "
                + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY;
        Bukkit.broadcastMessage(prefix + msg);
    }

    public void deleteWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
            }
            Bukkit.getServer().unloadWorld(world, false);
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        if (worldFolder.exists()) {
            deleteWorldFolder(worldFolder);
        }
    }

    public void deleteWorldFolder(File worldFolder) {
        if (worldFolder.exists()) {
            for (File file : worldFolder.listFiles()) {
                if (file.isDirectory()) {
                    deleteWorldFolder(file);
                } else {
                    file.delete();
                }
            }
            worldFolder.delete();
        }
    }

}