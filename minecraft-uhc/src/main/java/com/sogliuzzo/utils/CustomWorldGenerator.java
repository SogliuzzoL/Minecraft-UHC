package com.sogliuzzo.utils;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.bukkit.util.noise.PerlinNoiseGenerator;

public class CustomWorldGenerator extends ChunkGenerator {

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Collections.singletonList(new TreePopulator());
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);
        PerlinNoiseGenerator terrainNoiseGenerator = new PerlinNoiseGenerator(world.getSeed());
        PerlinNoiseGenerator riverNoiseGenerator = new PerlinNoiseGenerator(world.getSeed() + 1);
        random = new Random(world.getSeed());
        double scaleTerrain = 0.05;
        double scaleRiver = 0.025;
        int baseHeight = 64;
        int terrainOctave = 4;
        double terrainFrequency = 0.5;
        double terrainAmplitude = 0.5;
        int riverOctave = 2;
        double riverFrequency = 0.25;
        double riverAmplitude = 0.25;
        double noiseFactor = 5;
        double seuilRiver = 0.1;
        double seuilRiver2 = 0.3;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;
                // Modification du biome
                biome.setBiome(x, z, Biome.FOREST);

                // Génération du terrain
                double terrainNoise = terrainNoiseGenerator.noise(worldX * scaleTerrain, worldZ * scaleTerrain, terrainOctave,
                        terrainFrequency,
                        terrainAmplitude);
                int height = (int) (baseHeight + terrainNoise * noiseFactor);

                // Génération des rivères
                double riverNoise = riverNoiseGenerator.noise(worldX * scaleRiver, worldZ * scaleRiver, riverOctave,
                        riverFrequency,
                        riverAmplitude);

                // Ajout des rivières
                if (Math.abs(riverNoise) < seuilRiver) {
                    // Changement de la hauteur
                    height = Math.min(height, baseHeight - 1);
                    for (int y = height - 4; y <= height; y++) {
                        chunkData.setBlock(x, y, z, Material.GRAVEL);
                    }
                    for (int y = height + 1; y <= baseHeight; y++) {
                        chunkData.setBlock(x, y, z, Material.WATER);
                    }
                } else if (Math.abs(riverNoise) < seuilRiver2) {
                    height = (int) ((terrainNoise * noiseFactor) / (seuilRiver2 - seuilRiver) * Math.abs(riverNoise) + baseHeight);
                    for (int y = height - 4; y < height; y++) {
                        chunkData.setBlock(x, y, z, Material.DIRT);
                    }
                    chunkData.setBlock(x, height, z, Material.GRASS);
                } else {
                    for (int y = height - 4; y < height; y++) {
                        chunkData.setBlock(x, y, z, Material.DIRT);
                    }
                    chunkData.setBlock(x, height, z, Material.GRASS);
                }

                // Ajout du terrain
                for (int y = 0; y < height - 4; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
            }
        }

        return chunkData;
    }
}
