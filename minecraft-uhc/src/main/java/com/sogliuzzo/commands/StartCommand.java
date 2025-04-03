package com.sogliuzzo.commands;

import java.util.Random;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sogliuzzo.Main;
import com.sogliuzzo.utils.GameStates;
import com.sogliuzzo.world.CustomWorldGenerator;

public class StartCommand implements CommandExecutor {

    private Main main;

    public StartCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        main.broadcastMessage("A new game is starting and a new map is being generated!");
        main.gameState = GameStates.STARTING;

        // Création d'un nouveau monde
        main.deleteWorld("uhc_world");
        WorldCreator worldCreator = new WorldCreator("uhc_world");
        worldCreator.generator(new CustomWorldGenerator());
        main.world = main.getServer().createWorld(worldCreator);

        // Paramètres du monde
        main.world.setDifficulty(org.bukkit.Difficulty.HARD);
        main.world.setGameRuleValue("doDaylightCycle", "false");
        main.world.setTime(0);
        main.world.setGameRuleValue("naturalRegeneration", "false");
        main.world.setKeepSpawnInMemory(false);

        // Téléportation des joueurs
        main.broadcastMessage("Map successfully generated and players are being teleported!");
        Random random = new Random();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setExp(0);
            player.setLevel(0);
            player.getInventory().clear();
            player.getEquipment().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setSaturation(20);
            player.setAllowFlight(false);
            for (Achievement achievement : Achievement.values()) {
                player.removeAchievement(achievement);
            }
            double x = (2 * random.nextDouble() - 1) * main.borderStartSize / 2;
            double z = (2 * random.nextDouble() - 1) * main.borderStartSize / 2;
            player.teleport(new Location(main.world, x, 300, z));
        }

        // Début de la partie
        main.broadcastMessage("All players have been teleported! You will be invincible for the next minute.");
        main.time = 0;
        main.world.getWorldBorder().setSize(main.borderStartSize);
        main.gameState = GameStates.STARTED;
        if (main.gameRunnable != null)
            main.gameRunnable.cancel();
        main.gameRunnable = new BukkitRunnable() {

            @Override
            public void run() {
                main.time++;
                // Final Heal & PvP
                if (main.time == main.finalHealTime) {
                    main.broadcastMessage("Final heal complete!");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.setHealth(20);
                    }
                }
                if (main.time == main.pvpTime) {
                    main.broadcastMessage("PvP is now enabled!");
                    main.gameState = GameStates.PVP;
                }
                // Gestion de la border
                if (main.time == main.borderStartTime) {
                    main.gameState = GameStates.BORDER;
                    main.world.getWorldBorder().setSize(main.borderEndSize, main.borderEndTime - main.borderStartTime);
                    main.broadcastMessage("The border is closing in!");
                } else if (main.time == main.borderEndTime) {
                    main.gameState = GameStates.MEETUP;
                    main.broadcastMessage("The border has reached its final size!");
                }
            }

        };
        main.gameRunnable.runTaskTimer(main, 0, 20);
        return true;
    }

}
