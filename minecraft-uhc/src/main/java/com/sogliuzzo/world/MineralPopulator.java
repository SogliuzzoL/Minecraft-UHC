package com.sogliuzzo.world;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.PerlinNoiseGenerator;

public class MineralPopulator extends BlockPopulator {
    int emeraldLayer = 33;
    double scaleEmerald = 0.025;
    double rateEmerald = 0.00009765625;

    int diamondLayer = 16;
    double scaleDiamond = 0.25;
    double rateDiamond = 0.00078125;

    int goldLayer = 34;
    double scaleGold = 0.075;
    double rateGold = 0.0015625;

    int ironLayer = 64;
    double scaleIron = 0.175;
    double rateIron = 0.0015625;
    
    int lapisLayer = 34;
    double scaleLapis = 0.2;
    double rateLapis = 0.000390625;
    
    int redstoneLayer = 16;
    double scaleRedstone = 0.225;
    double rateRedstone = 0.000390625;
    
    int coalLayer = 128;
    double scaleCoal = 0.1;
    double rateCoal = 0.0015625;

    double waterRate = 0.0001;

    int octave = 2;
    double frenquency = 2;
    double amplitude = 1 / 2;

    @Override
    public void populate(World world, Random random, Chunk source) {
        PerlinNoiseGenerator noiseGenerator = new PerlinNoiseGenerator(world.getSeed());
        random = new Random(world.getSeed());

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = source.getX() * 16 + x;
                int worldZ = source.getZ() * 16 + z;
                int height = world.getHighestBlockYAt(worldX, worldZ);
                for (int y = 0; y <= height; y++) {
                    Block block = source.getBlock(worldX, y, worldZ);
                    if (block.getType() == Material.STONE) {
                        if (y < diamondLayer && computeDiamond(noiseGenerator, worldX, y, worldZ)) {
                            block.setType(Material.DIAMOND_ORE);
                        }
                        else if (y < goldLayer && computeGold(noiseGenerator, worldX, y, worldZ)) {
                            block.setType(Material.GOLD_ORE);
                        }
                        else if (y < ironLayer && computeIron(noiseGenerator, worldX, y, worldZ)) {
                            block.setType(Material.IRON_ORE);
                        }
                        else if (y < lapisLayer && computeLapis(noiseGenerator, worldX, y, worldZ)) {
                            block.setType(Material.LAPIS_ORE);
                        }
                        else if (y < redstoneLayer && computeRedstone(noiseGenerator, worldX, y, worldZ)) {
                            block.setType(Material.REDSTONE_ORE);
                        }
                        else if (y < coalLayer && computeCoal(noiseGenerator, worldX, y, worldZ)) {
                            block.setType(Material.COAL_ORE);
                        }
                        else if (y < emeraldLayer && computeEmerald(noiseGenerator, worldX, y, worldZ)) {
                            block.setType(Material.EMERALD_ORE);
                        }
                        // Water
                        if (random.nextDouble() < waterRate && nextToAir(world, block)) {
                            block.setType(Material.WATER);
                        }
                    }
                }
            }
        }
    }

    private boolean computeEmerald(PerlinNoiseGenerator noiseGenerator, int x, int y, int z) {
        double noise = noiseGenerator.noise(x * scaleEmerald, y * scaleEmerald, z * scaleEmerald, octave,
                frenquency, amplitude,
                true);
        return Math.abs(noise) < rateEmerald;
    }

    private boolean computeCoal(PerlinNoiseGenerator noiseGenerator, int x, int y, int z) {
        double noise = noiseGenerator.noise(x * scaleCoal, y * scaleCoal, z * scaleCoal, octave,
                frenquency, amplitude,
                true);
        return Math.abs(noise) < rateCoal;
    }

    private boolean computeRedstone(PerlinNoiseGenerator noiseGenerator, int x, int y, int z) {
        double noise = noiseGenerator.noise(x * scaleRedstone, y * scaleRedstone, z * scaleRedstone, octave,
                frenquency, amplitude,
                true);
        return Math.abs(noise) < rateRedstone;
    }

    private boolean computeLapis(PerlinNoiseGenerator noiseGenerator, int x, int y, int z) {
        double noise = noiseGenerator.noise(x * scaleLapis, y * scaleLapis, z * scaleLapis, octave,
                frenquency, amplitude,
                true);
        return Math.abs(noise) < rateLapis;
    }

    private boolean computeIron(PerlinNoiseGenerator noiseGenerator, int x, int y, int z) {
        double noise = noiseGenerator.noise(x * scaleIron, y * scaleIron, z * scaleIron, octave,
                frenquency, amplitude,
                true);
        return Math.abs(noise) < rateIron;
    }

    private boolean computeGold(PerlinNoiseGenerator noiseGenerator, int x, int y, int z) {
        double noise = noiseGenerator.noise(x * scaleGold, y * scaleGold, z * scaleGold, octave,
                frenquency, amplitude,
                true);
        return Math.abs(noise) < rateGold;
    }

    private boolean computeDiamond(PerlinNoiseGenerator noiseGenerator, int x, int y, int z) {
        double noise = noiseGenerator.noise(x * scaleDiamond, y * scaleDiamond, z * scaleDiamond, octave,
                frenquency, amplitude,
                true);
        return Math.abs(noise) < rateDiamond;
    }

    private boolean nextToAir(World world, Block block) {
        int x = (int) block.getLocation().getX();
        int y = (int) block.getLocation().getY();
        int z = (int) block.getLocation().getZ();

        if (world.getBlockAt(x + 1, y, z).getType() == Material.AIR)
            return true;
        if (world.getBlockAt(x + 1, y, z + 1).getType() == Material.AIR)
            return true;
        if (world.getBlockAt(x + 1, y, z - 1).getType() == Material.AIR)
            return true;
        if (world.getBlockAt(x - 1, y, z).getType() == Material.AIR)
            return true;
        if (world.getBlockAt(x - 1, y, z + 1).getType() == Material.AIR)
            return true;
        if (world.getBlockAt(x - 1, y, z - 1).getType() == Material.AIR)
            return true;

        return false;
    }
}
