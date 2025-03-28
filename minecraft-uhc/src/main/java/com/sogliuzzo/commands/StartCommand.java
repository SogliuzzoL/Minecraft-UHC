package com.sogliuzzo.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.sogliuzzo.Main;
import com.sogliuzzo.world.CustomWorldGenerator;

public class StartCommand implements CommandExecutor {

    private Main main;

    public StartCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Création d'un nouveau monde
        main.deleteWorld("uhc_world");
        WorldCreator worldCreator = new WorldCreator("uhc_world");
        worldCreator.generator(new CustomWorldGenerator());
        main.world = main.getServer().createWorld(worldCreator);

        main.world.setDifficulty(org.bukkit.Difficulty.HARD);
        main.world.setGameRuleValue("doDaylightCycle", "false");
        main.world.setTime(0);
        main.world.setKeepSpawnInMemory(false);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(new Location(main.world, 0, 100, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 255));
        }

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
