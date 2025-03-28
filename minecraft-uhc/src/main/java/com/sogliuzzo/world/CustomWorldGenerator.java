package com.sogliuzzo.world;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.util.noise.PerlinNoiseGenerator;

public class CustomWorldGenerator extends ChunkGenerator {

    double scaleContinentalness = 0.01;
    double scaleErosion = 0.001;
    double scalePeaksValleys = 0.1;
    int octave = 8;
    double frenquency = 2;
    double amplitude = 1 / 2;

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        ArrayList<BlockPopulator> list = new ArrayList<>();
        list.add(new NaturePopulator());
        list.add(new TreePopulator());
        return list;
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);

        PerlinNoiseGenerator terrainNoiseGenerator = new PerlinNoiseGenerator(world.getSeed());
        random = new Random(world.getSeed());

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;
                // Modification du biome
                biome.setBiome(x, z, Biome.FOREST);

                // Ajout du terrain
                int height = computeHeight(terrainNoiseGenerator, worldX, worldZ);
                for (int y = 0; y < height; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
                chunkData.setBlock(x, height, z, Material.GRASS);
            }
        }

        return chunkData;
    }

    private int computeHeight(PerlinNoiseGenerator noiseGenerator, int x, int z) {
        double continentalnessNoise = noiseGenerator.noise(x * scaleContinentalness,
                z * scaleContinentalness,
                octave,
                frenquency,
                amplitude, true);
        double erosionNoise = noiseGenerator.noise(x * scaleErosion,
                z * scaleErosion,
                octave,
                frenquency,
                amplitude, true);
        double peaksValleysNoise = noiseGenerator.noise(x * scalePeaksValleys,
                z * scalePeaksValleys,
                octave,
                frenquency,
                amplitude, true);

        double a = continentalness(continentalnessNoise);
        double aCoef = 9;
        double b = erosion(erosionNoise);
        double bCoef = 0.1;
        double c = peaksValleys(peaksValleysNoise);
        double cCoef = 0.5;

        return (int) ((a * aCoef + b * bCoef + c * cCoef) / (aCoef + bCoef + cCoef));
    }

    private double continentalness(double noise) {
        // y = 58.78x^(4) - 30x^(3) - 52.63x^(2) + 34.54x + 87.35
        return 58.78 * Math.pow(noise, 4) - 30 * Math.pow(noise, 3) - 52.63 * Math.pow(noise, 2) + 34.54 * noise
                + 87.35;
    }

    private double erosion(double noise) {
        // 106.72x^(6) - 42.66x^(5) - 196.56x^(4) + 51.48x^(3) + 104.85x^(2) - 27.98x +
        // 55.23
        return 106.72 * Math.pow(noise, 6) - 42.66 * Math.pow(noise, 5) - 196.56 * Math.pow(noise, 4)
                + 51.48 * Math.pow(noise, 3) + 104.85 * Math.pow(noise, 2) - 27.98 * noise + 55.23;
    }

    private double peaksValleys(double noise) {
        // -35.76x^(4) + 2.89x^(3) + 33.31x^(2) + 15.18x + 64.01
        return -35.76 * Math.pow(noise, 4) + 2.89 * Math.pow(noise, 3) + 33.31 * Math.pow(noise, 2)
                + 15.18 * noise + 64.01;
    }
}
