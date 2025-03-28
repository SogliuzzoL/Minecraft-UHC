package com.sogliuzzo.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.World;
import java.util.Random;

public class TreePopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk source) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (random.nextDouble() < 0.01) {
                    int worldX = source.getX() * 16 + x;
                    int worldZ = source.getZ() * 16 + z;
                    int height = world.getHighestBlockYAt(worldX, worldZ);

                    if (world.getBlockAt(worldX, height - 1, worldZ).getType() == Material.GRASS) {
                        if (random.nextDouble() < 0.8) {
                            world.generateTree(new Location(world, worldX, height, worldZ), TreeType.TREE);
                        } else
                            world.generateTree(new Location(world, worldX, height, worldZ), TreeType.BIG_TREE);
                    }
                }
            }
        }
    }
}
