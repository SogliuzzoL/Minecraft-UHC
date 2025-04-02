package com.sogliuzzo.world;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.Random;

public class TreePopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk source) {
        for (int x = 2; x < 14; x++) {
            for (int z = 2; z < 14; z++) {
                int worldX = source.getX() * 16 + x;
                int worldZ = source.getZ() * 16 + z;
                int height = world.getHighestBlockYAt(worldX, worldZ);
                Block block = world.getBlockAt(worldX, height - 1, worldZ);
                // FOREST
                if (block.getBiome() == Biome.FOREST) {
                    if (random.nextDouble() < 0.03) {
                        if (block.getType() == Material.GRASS) {
                            if (random.nextDouble() < 0.8) {
                                world.generateTree(new Location(world, worldX, height, worldZ), TreeType.TREE);
                            } else if (x > 6 && x < 10 && z > 6 && z < 10) {
                                world.generateTree(new Location(world, worldX, height, worldZ), TreeType.BIG_TREE);
                            } else
                                world.generateTree(new Location(world, worldX, height, worldZ), TreeType.TREE);
                        }
                    }
                }
                // ROOFED_FOREST
                if (block.getBiome() == Biome.ROOFED_FOREST) {
                    if (random.nextDouble() < 0.1) {
                        if (block.getType() == Material.GRASS) {
                            if (x > 3 && x < 13 && z > 3 && z < 13) {
                                world.generateTree(new Location(world, worldX, height, worldZ), TreeType.DARK_OAK);
                            } else
                                world.generateTree(new Location(world, worldX, height, worldZ), TreeType.TREE);
                        }
                    }
                }
            }
        }
    }
}
