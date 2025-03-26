package com.sogliuzzo;

import org.bukkit.plugin.java.JavaPlugin;

import com.sogliuzzo.commands.StartCommand;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("start").setExecutor(new StartCommand(this));
    }
}