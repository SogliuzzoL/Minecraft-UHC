package com.sogliuzzo.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.sogliuzzo.Main;

public class StartCommand implements CommandExecutor {

    private Main main;

    public StartCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getLogger().info("Starting the game.");
        // Bukkit.getScheduler().runTask(main, null);
        return true;
    }

}
