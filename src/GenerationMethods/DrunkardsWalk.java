package GenerationMethods;

import java.util.Random;

/**
 * The {@code DrunkardsWalk} class implements a procedural map generation algorithm
 * that simulates a "drunkard" wandering randomly and carving out paths in a solid grid.
 * <p>
 * This algorithm is often used for generating cave-like maps or organic dungeon layouts.
 * It begins with a solid map, places a random walker that moves randomly while carving
 * open spaces, and repeats this process until a desired percentage of the map is open.
 * </p>
 *
 * <p><b>Algorithm Summary:</b></p>
 * <ol>
 *     <li>Initialize a solid map filled with wall tiles ('#').</li>
 *     <li>Choose a random starting point for the drunkard.</li>
 *     <li>Move randomly in one of four directions, carving open tiles ('.') as it goes.</li>
 *     <li>After a set number of steps, spawn a new drunkard within an open space.</li>
 *     <li>Repeat until the target open-space percentage is reached.</li>
 * </ol>
 *
 * <p>
 * Typical use cases include cave generation, dungeon layouts, or any map that benefits
 * from irregular organic shapes.
 * </p>
 */
public class DrunkardsWalk {
    private final int width;
    private final int height;
    private final char[][] map;
    private final Random rand = new Random();

    /**
     * Constructs a {@code DrunkardsWalk} generator with the given map dimensions.
     *
     * @param width  the width of the map
     * @param height the height of the map
     */
    public DrunkardsWalk(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[width][height];
    }

    /**
     * Generates a map using the Drunkard's Walk algorithm.
     *
     * @param openPercent the fraction of the map to be opened (e.g., 0.33 for 33%)
     * @param maxSteps    the maximum number of steps each drunkard takes before stopping
     * @return a 2D character array representing the generated map,
     *         where '#' indicates walls and '.' indicates open space
     */
    public char[][] generateMap(double openPercent, int maxSteps) {
        // Step 1: Start with a solid map
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = '#';
            }
        }

        // Step 2: Choose a random starting point for the drunkard
        int x = rand.nextInt(width);
        int y = rand.nextInt(height);

        // Total number of tiles to open based on desired open percentage
        int totalTiles = width * height;
        int targetOpen = (int) (totalTiles * openPercent);
        int openCount = 0;

        // Step 3–5: Drunkard walks, carving until enough open space is created
        while (openCount < targetOpen) {
            int steps = 0;

            // Each drunkard gets a limited number of steps before "passing out"
            while (steps < maxSteps) {
                // Carve the current tile if it's still a wall
                if (map[x][y] == '#') {
                    map[x][y] = '.';
                    openCount++;
                }

                // If we've carved enough, stop early
                if (openCount >= targetOpen) break;

                // Choose a random direction
                int dir = rand.nextInt(4);
                switch (dir) {
                    case 0 -> x = Math.max(1, x - 1);           // left
                    case 1 -> x = Math.min(width - 2, x + 1);    // right
                    case 2 -> y = Math.max(1, y - 1);            // up
                    case 3 -> y = Math.min(height - 2, y + 1);   // down
                }

                steps++;
            }

            // Step 5: Spawn new drunkard somewhere in an open area
            int newX, newY;
            do {
                newX = rand.nextInt(width);
                newY = rand.nextInt(height);
            } while (map[newX][newY] != '.'); // ensure we start inside carved space

            x = newX;
            y = newY;
        }

        return map;
    }

    /**
     * Returns a string representation of the generated map.
     * <p>
     * Each row of the map is placed on a new line.
     * Walls are represented by '#' and open spaces by '.'.
     * </p>
     *
     * @return a string-formatted view of the generated map
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
