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
import com.sogliuzzo.listeners.PlayerJoinListener;
import com.sogliuzzo.utils.GameStates;

public class Main extends JavaPlugin {
    public int time = 0;
    public BukkitRunnable gameRunnable;
    public World world;
    public GameStates gameState;

    @Override
    public void onEnable() {
        gameState = GameStates.WAITING;
        // Register Commands
        getCommand("start").setExecutor(new StartCommand(this));
        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
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