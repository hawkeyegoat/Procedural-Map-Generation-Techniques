package GenerationMethods;
import java.util.Random;

/**
 * The {@code CellularAutomata} class implements a procedural generation technique
 * based on cellular automata rules. It generates natural-looking cave structures
 * by iteratively smoothing a randomly generated grid of walls and floors.
 * <p>
 * Algorithm Summary:
 * <ol>
 *     <li>Initialize the map with approximately 50% walls and 50% floors.</li>
 *     <li>Iterate through the grid for a specified number of iterations.</li>
 *     <li>For each tile, count neighboring wall tiles (including diagonals).</li>
 *     <li>Apply rules based on wall neighbor count:
 *         <ul>
 *             <li>0 neighbors: Become a wall.</li>
 *             <li>1–4 neighbors: Become a floor.</li>
 *             <li>5+ neighbors: Become a wall.</li>
 *         </ul>
 *     </li>
 * </ol>
 * This process yields organic cave or cavern-like maps useful for dungeon generation.
 * </p>
 *
 * @author Logan Atkinson
 */
public class CellularAutomata {
    /** The total width of the map. */
    private final int width;
    /** The total height of the map. */
    private final int height;
    /** Random number generator for initialization and rule application. */
    private final Random rand = new Random();
    /** The 2D character array representing the generated map. */
    private final char[][] map;

    /**
     * Constructs a {@code CellularAutomata} generator for a map of the given dimensions.
     *
     * @param width  the width of the map
     * @param height the height of the map
     */
    public CellularAutomata(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[width][height];
    }

    /**
     * Generates a cave-like map using cellular automata rules.
     *
     * @param iterations the number of smoothing iterations to perform
     * @return a 2D character array representing the generated map
     */
    public char[][] generateMap(int iterations) {
        // Initialize the map randomly with walls and floors
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = (rand.nextDouble() < 0.5) ? '#' : '.';
            }
        }

        // Run cellular automata smoothing for the specified number of iterations
        for (int i = 0; i < iterations; i++) {
            smoothMap();
        }

        return map;
    }

    /**
     * Applies one iteration of cellular automata smoothing rules
     * to the entire map.
     */
    private void smoothMap() {
        char[][] newMap = new char[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Keep edges solid to prevent boundary errors
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    newMap[x][y] = '#';
                    continue;
                }

                int wallCount = countWallNeighbors(x, y);

                // Apply neighbor rules
                if (wallCount == 0) {
                    newMap[x][y] = '#';
                } else if (wallCount >= 1 && wallCount <= 4) {
                    newMap[x][y] = '.';
                } else {
                    newMap[x][y] = '#';
                }
            }
        }

        // Copy smoothed map back to the main map
        for (int x = 0; x < width; x++) {
            System.arraycopy(newMap[x], 0, map[x], 0, height);
        }
    }

    /**
     * Counts the number of wall tiles surrounding a given coordinate.
     * Includes diagonals and handles edges as walls.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @return the number of neighboring wall tiles
     */
    private int countWallNeighbors(int x, int y) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue; // skip self

                int nx = x + i;
                int ny = y + j;

                // Out-of-bounds counts as wall
                if (nx < 0 || ny < 0 || nx >= width || ny >= height) {
                    count++;
                } else if (map[nx][ny] == '#') {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns a string representation of the current map.
     *
     * @return a multi-line string showing the generated map
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
