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
    double scaleBedrockA = 0.1;
    double scaleBedrockB = 0.5;
    double scaleCavesA = 0.05;
    double scaleCavesB = 0.1;
    int octave = 4;
    double frenquency = 2;
    double amplitude = 1 / 2;
    double cavesOpeningRate = -0.55;

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        ArrayList<BlockPopulator> list = new ArrayList<>();
        list.add(new NaturePopulator());
        list.add(new MineralPopulator());
        list.add(new TreePopulator());
        return list;
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);

        PerlinNoiseGenerator noiseGenerator = new PerlinNoiseGenerator(world.getSeed());
        random = new Random(world.getSeed());

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;
                // Modification du biome
                biome.setBiome(x, z, Biome.FOREST);

                // Ajout du terrain
                int height = computeHeight(noiseGenerator, worldX, worldZ);
                for (int y = 1; y < height; y++) {
                    if (!computeCaves(noiseGenerator, worldX, y, worldZ)) {
                        chunkData.setBlock(x, y, z, Material.STONE);
                    } else if (y <= 11)
                        chunkData.setBlock(x, y, z, Material.LAVA);
                }
                // Ajout de la couche supérieur
                for (int y = height - 7; y <= height; y++) {
                    if (!computeCavesOpening(noiseGenerator, worldX, y, worldZ)) {
                        if (y == height) {
                            chunkData.setBlock(x, y, z, Material.GRASS);
                        } else if (y > height - 5) {
                            chunkData.setBlock(x, y, z, Material.DIRT);
                        } else {
                            chunkData.setBlock(x, y, z, Material.STONE);
                        }
                    }
                }
                // Ajout de la Bedrock
                int bedrockHeight = computeBedrock(noiseGenerator, worldX, worldZ);
                for (int y = 0; y <= bedrockHeight; y++) {
                    chunkData.setBlock(x, y, z, Material.BEDROCK);
                }
            }
        }

        return chunkData;
    }

    private int computeBedrock(PerlinNoiseGenerator noiseGenerator, int x, int z) {
        double bedrockANoise = noiseGenerator.noise(x * scaleBedrockA,
                z * scaleBedrockA,
                octave,
                frenquency,
                amplitude, true);
        double bedrockBNoise = noiseGenerator.noise(x * scaleBedrockB,
                z * scaleBedrockB,
                octave,
                frenquency,
                amplitude, true);
        double a = bedrockA(bedrockANoise);
        double aCoef = 1;
        double b = bedrockB(bedrockBNoise);
        double bCoef = 5;
        return (int) ((a * aCoef + b * bCoef) / (aCoef + bCoef));
    }

    private double bedrockA(double noise) {
        // g(x)=11.21 x^(4)-0.95 x^(3)-4.08 x^(2)+1.5 x+1.11
        return 11.21 * Math.pow(noise, 4) - 0.95 * Math.pow(noise, 3) - 4.08 * Math.pow(noise, 2) + 1.5 * noise + 1.11;
    }

    private double bedrockB(double noise) {
        // 6.97x^(4) + 10.86x^(3) + 3.34x^(2) - 6.58x + 1.48
        return 6.97 * Math.pow(noise, 4) + 10.86 * Math.pow(noise, 3) + 3.34 * Math.pow(noise, 2) - 6.58 * noise + 1.48;
    }

    private boolean computeCavesOpening(PerlinNoiseGenerator noiseGenerator, int x, int y, int z) {
        double cavesNoise = noiseGenerator.noise(x * scaleCavesA,
                y * scaleCavesB,
                z * scaleCavesA,
                octave,
                frenquency,
                amplitude, true);
        return cavesNoise < cavesOpeningRate;
    }

    private boolean computeCaves(PerlinNoiseGenerator noiseGenerator, int x, int y, int z) {
        double cavesNoise = noiseGenerator.noise(x * scaleCavesA,
                y * scaleCavesB,
                z * scaleCavesA,
                octave,
                frenquency,
                amplitude, true);
        return cavesNoise < 0;
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
