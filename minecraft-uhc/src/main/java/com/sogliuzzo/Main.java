package com.sogliuzzo;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
    @Override
    public void onEnable() {
        Bukkit.getLogger().info(getName());
        super.onEnable();
    }
}