package GenerationMethods;

import java.util.Random;

/**
 * The {@code PerlinAndSimplexNoise} class provides a simple implementation
 * of Perlin noise for 2D procedural generation.
 * <p>
 * This generator can produce smooth, continuous noise maps suitable for terrain
 * heightmaps, texture synthesis, or any other application requiring natural-looking
 * random variation.
 * </p>
 *
 * <p><b>Algorithm Summary:</b></p>
 * <ol>
 *     <li>Generate a random permutation table for gradient lookup.</li>
 *     <li>Compute Perlin noise at each point in the 2D grid, using multiple octaves
 *         for increased detail and complexity.</li>
 *     <li>Normalize the final noise values to the range [0, 1].</li>
 * </ol>
 *
 * <p>
 * This implementation supports multiple configurable parameters:
 * </p>
 * <ul>
 *     <li><b>Octaves:</b> number of layers of detail (higher values increase complexity).</li>
 *     <li><b>Frequency:</b> initial scale of noise variation.</li>
 *     <li><b>Gain:</b> amplitude multiplier between octaves.</li>
 *     <li><b>Lacunarity:</b> frequency multiplier between octaves.</li>
 * </ul>
 *
 * <p><b>Note:</b> This implementation currently uses a Perlin-style gradient algorithm
 * and does not include Simplex optimization, despite the class name.</p>
 */
public class PerlinAndSimplexNoise {
    private final int width;
    private final int height;
    private final double[][] noiseMap;
    private final Random rand = new Random();

    /**
     * Constructs a {@code PerlinAndSimplexNoise} generator for a map of the given size.
     *
     * @param width  the width of the map in cells
     * @param height the height of the map in cells
     */
    public PerlinAndSimplexNoise(int width, int height) {
        this.width = width;
        this.height = height;
        this.noiseMap = new double[width][height];
    }

    /**
     * Generates a Perlin noise map using the provided parameters.
     *
     * @param octaves     the number of noise layers to combine
     * @param frequency   the initial frequency of the noise
     * @param gain        the amplitude multiplier applied after each octave
     * @param lacunarity  the frequency multiplier applied after each octave
     * @return a 2D array of normalized noise values ranging from 0 to 1
     */
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

    /**
     * Computes 2D Perlin noise for a given coordinate using the provided permutation table.
     *
     * @param x           the x-coordinate in noise space
     * @param y           the y-coordinate in noise space
     * @param permutation the gradient lookup permutation table
     * @return the noise value at the specified coordinate (typically in range [-1, 1])
     */
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

    /**
     * Smooths the interpolation curve for Perlin noise using the function 6t⁵ - 15t⁴ + 10t³.
     *
     * @param t the interpolation factor
     * @return the smoothed interpolation value
     */
    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    /**
     * Performs linear interpolation between two values.
     *
     * @param a the start value
     * @param b the end value
     * @param t the interpolation factor (0–1)
     * @return the interpolated value
     */
    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    /**
     * Calculates a gradient vector based on the hash value and the input coordinates.
     *
     * @param hash the hashed gradient index
     * @param x    the x-coordinate distance
     * @param y    the y-coordinate distance
     * @return the dot product of the gradient and the distance vector
     */
    private double grad(int hash, double x, double y) {
        switch (hash & 3) {
            case 0: return x + y;
            case 1: return -x + y;
            case 2: return x - y;
            case 3: return -x - y;
            default: return 0; // unreachable
        }
    }

    /**
     * Returns a visual representation of the generated noise map.
     * <p>
     * Each cell is represented by an ASCII character based on its height value:
     * </p>
     * <ul>
     *     <li>{@code '~'} → water (low elevation)</li>
     *     <li>{@code '.'} → plains</li>
     *     <li>{@code '^'} → hills</li>
     *     <li>{@code 'A'} → peaks (high elevation)</li>
     * </ul>
     *
     * @return a string-formatted map visualization
     */
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
