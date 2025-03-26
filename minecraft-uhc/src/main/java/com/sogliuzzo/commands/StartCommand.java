package com.sogliuzzo.commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.sogliuzzo.Main;

public class StartCommand implements CommandExecutor {

    private Main main;

    public StartCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        main.broadcastMessage("Starting a new game !");
        main.time = 0;
        if (main.gameRunnable != null)
            main.gameRunnable.cancel();
        main.gameRunnable = new BukkitRunnable() {

            @Override
            public void run() {
                main.time++;
            }

        };
        main.gameRunnable.runTaskTimer(main, 0, 20);
        return true;
    }

}
