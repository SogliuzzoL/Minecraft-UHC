package com.sogliuzzo.commands;

import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.sogliuzzo.Main;
import com.sogliuzzo.utils.GameStates;
import com.sogliuzzo.world.CustomWorldGenerator;

public class PregenCommand implements CommandExecutor {

    private Main main;

    public PregenCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (main.gameState != GameStates.WAITING) {
            sender.sendMessage(main.prefix + "Reload the server before pregenerating a new map.");
            return true;
        }

        main.broadcastMessage("World creation and pregeneration are starting!");
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

        BukkitRunnable pregenTask = new BukkitRunnable() {
            int x = 0;
            int z = 0;

            @Override
            public void run() {
                if (x >= main.borderStartSize) {
                    main.gameState = GameStates.PREGEN;
                    main.broadcastMessage("The world has been created and pregenerated!");
                    cancel();
                    return;
                }

                int worldX = x - main.borderStartSize / 2;
                int worldZ = z - main.borderStartSize / 2;
                main.world.getChunkAt(worldX, worldZ).load();

                int total = (int) Math.pow(main.borderStartSize, 2);
                int current = x * main.borderStartSize + z;

                if (current % (total / 25) == 0) {
                    double percent = (current * 100) / total;
                    main.broadcastMessage(
                            "Pregeneration " + Math.round(percent) + "% completed. (" + worldX + ", " + worldZ + ")");
                }

                z += 16;
                if (z >= main.borderStartSize) {
                    z = 0;
                    x += 16;
                }
            }
        };

        pregenTask.runTaskTimer(main, 0L, 1L);

        return true;
    }

}
