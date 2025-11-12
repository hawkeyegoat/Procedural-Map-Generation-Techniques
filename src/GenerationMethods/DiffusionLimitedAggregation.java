package GenerationMethods;

import java.util.Random;

/**
 * The {@code DiffusionLimitedAggregation} class implements a cave and structure generation
 * algorithm inspired by natural diffusion-limited growth processes, such as
 * crystal formation or coral-like patterns.
 * <p>
 * Algorithm Summary:
 * <ol>
 *     <li>Start with a solid map filled with walls.</li>
 *     <li>Place an initial “seed” of open tiles at the map’s center.</li>
 *     <li>Spawn random particles along the map’s edges.</li>
 *     <li>Each particle performs a random walk until it becomes adjacent to an
 *     existing open tile, at which point it solidifies (carves open space).</li>
 *     <li>Repeat the process until the desired number of particles have attached.</li>
 * </ol>
 * <p>
 * The result is a branching, organic cave structure resembling dendritic growth
 * patterns observed in physics and nature.
 * </p>
 *
 * @author Logan Atkinson
 */
public class DiffusionLimitedAggregation {

    /** The total width of the map. */
    private final int width;

    /** The total height of the map. */
    private final int height;

    /** 2D array representing the map grid (walls and open tiles). */
    private final char[][] map;

    /** Random number generator for particle movement and placement. */
    private final Random rand = new Random();

    /**
     * Constructs a {@code DiffusionLimitedAggregation} generator with a given map size.
     *
     * @param width  the width of the map
     * @param height the height of the map
     */
    public DiffusionLimitedAggregation(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[width][height];
    }

    /**
     * Generates a cave-like structure using diffusion-limited aggregation.
     *
     * @param seedSize     the radius of the initial open seed in the map center
     * @param maxParticles the number of diffusing particles to simulate
     * @return a 2D character array representing the generated map
     */
    public char[][] generateMap(int seedSize, int maxParticles) {
        // --- Step 1: Initialize map as solid walls ---
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = '#';
            }
        }

        // --- Step 2: Create an open seed region at the map center ---
        int centerX = width / 2;
        int centerY = height / 2;
        for (int x = -seedSize; x <= seedSize; x++) {
            for (int y = -seedSize; y <= seedSize; y++) {
                int nx = centerX + x;
                int ny = centerY + y;
                if (nx > 0 && ny > 0 && nx < width && ny < height) {
                    map[nx][ny] = '.';
                }
            }
        }

        // --- Step 3: Spawn and diffuse particles until they attach ---
        int particles = 0;
        while (particles < maxParticles) {
            // Start at a random map edge
            int x, y;
            if (rand.nextBoolean()) {
                x = rand.nextBoolean() ? 0 : width - 1;
                y = rand.nextInt(height);
            } else {
                x = rand.nextInt(width);
                y = rand.nextBoolean() ? 0 : height - 1;
            }

            // Step 4: Particle random walk
            boolean stuck = false;
            while (!stuck) {
                int dx = rand.nextInt(3) - 1; // -1, 0, or 1
                int dy = rand.nextInt(3) - 1;
                if (dx == 0 && dy == 0) continue; // skip stationary move

                int newX = x + dx;
                int newY = y + dy;

                // If particle moves outside map bounds, discard it
                if (newX <= 0 || newY <= 0 || newX >= width - 1 || newY >= height - 1) {
                    break;
                }

                x = newX;
                y = newY;

                // Check adjacency to open area
                if (isAdjacentToOpen(x, y)) {
                    map[x][y] = '.'; // carve open space
                    stuck = true;
                }
            }

            particles++;
        }

        return map;
    }

    /**
     * Determines if a given coordinate is adjacent to an open tile.
     *
     * @param x the x-coordinate to test
     * @param y the y-coordinate to test
     * @return {@code true} if the tile is next to an open space ('.'); {@code false} otherwise
     */
    private boolean isAdjacentToOpen(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int nx = x + i, ny = y + j;
                if (nx > 0 && ny > 0 && nx < width && ny < height) {
                    if (map[nx][ny] == '.') return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a formatted string representation of the generated map.
     *
     * @return a multi-line string showing the current state of the map
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(map[x][y]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
