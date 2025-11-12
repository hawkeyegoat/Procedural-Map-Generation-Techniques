package GenerationMethods;

import java.util.Random;

public class PerlinAndSimplexNoise {
    private final int width;
    private final int height;
    private final double[][] noiseMap;
    private final Random rand = new Random();

    public PerlinAndSimplexNoise(int width, int height) {
        this.width = width;
        this.height = height;
        this.noiseMap = new double[width][height];
    }

    public double[][] generateMap(int octaves, double frequency, double gain, double lacunarity) {
        // Step 1: Generate random permutation for gradients
        int[] permutation = new int[512];
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) p[i] = i;
        for (int i = 0; i < 256; i++) {
            int j = rand.nextInt(256);
            int tmp = p[i];
            p[i] = p[j];
            p[j] = tmp;
        }
        for (int i = 0; i < 512; i++) permutation[i] = p[i & 255];

        // Step 2: Generate Perlin noise for each tile
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double amplitude = 1.0;
                double frequencyAcc = frequency;
                double noiseValue = 0.0;

                for (int o = 0; o < octaves; o++) {
                    double sampleX = x * frequencyAcc / width;
                    double sampleY = y * frequencyAcc / height;
                    double n = perlin(sampleX, sampleY, permutation);
                    noiseValue += n * amplitude;

                    amplitude *= gain;
                    frequencyAcc *= lacunarity;
                }

                noiseMap[x][y] = noiseValue;
                if (noiseValue < min) min = noiseValue;
                if (noiseValue > max) max = noiseValue;
            }
        }

        // Step 3: Normalize to [0,1]
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                noiseMap[x][y] = (noiseMap[x][y] - min) / (max - min);
            }
        }

        return noiseMap;
    }

    // --- Core Perlin implementation ---
    private double perlin(double x, double y, int[] permutation) {
        int xi = (int) Math.floor(x) & 255;
        int yi = (int) Math.floor(y) & 255;

        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        double u = fade(xf);
        double v = fade(yf);

        int aa = permutation[permutation[xi] + yi];
        int ab = permutation[permutation[xi] + yi + 1];
        int ba = permutation[permutation[xi + 1] + yi];
        int bb = permutation[permutation[xi + 1] + yi + 1];

        double x1, x2;
        x1 = lerp(grad(aa, xf, yf), grad(ba, xf - 1, yf), u);
        x2 = lerp(grad(ab, xf, yf - 1), grad(bb, xf - 1, yf - 1), u);
        return lerp(x1, x2, v);
    }

    private double fade(double t) {
        // 6t^5 - 15t^4 + 10t^3 smoothing curve
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y) {
        switch (hash & 3) {
            case 0: return x + y;
            case 1: return -x + y;
            case 2: return x - y;
            case 3: return -x - y;
            default: return 0; // unreachable
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double n = noiseMap[x][y];
                char c;
                if (n < 0.3) c = '~';      // water
                else if (n < 0.45) c = '.';
                else if (n < 0.7) c = '^'; // hills
                else c = 'A';              // peaks
                sb.append(c);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
