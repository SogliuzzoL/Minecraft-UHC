package com.sogliuzzo.world;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.World;
import java.util.Random;

public class NaturePopulator extends BlockPopulator {
    @SuppressWarnings("deprecation")
    @Override
    public void populate(World world, Random random, Chunk source) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (random.nextDouble() < 0.05) {
                    int worldX = source.getX() * 16 + x;
                    int worldZ = source.getZ() * 16 + z;
                    int height = world.getHighestBlockYAt(worldX, worldZ);

                    if (world.getBlockAt(worldX, height - 1, worldZ).getType() == Material.GRASS) {
                        double randNumber = random.nextDouble();
                        if (randNumber < 0.9) {
                            world.getBlockAt(worldX, height, worldZ).setType(Material.LONG_GRASS);
                            world.getBlockAt(worldX, height, worldZ).setData((byte) 1);
                        } else if (randNumber < 0.95) {
                            world.getBlockAt(worldX, height, worldZ).setType(Material.YELLOW_FLOWER);
                        } else
                            world.getBlockAt(worldX, height, worldZ).setType(Material.RED_ROSE);
                    }
                }
            }
        }
    }
}
