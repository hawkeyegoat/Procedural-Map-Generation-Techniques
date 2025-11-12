package GenerationMethods;

import assets.Node;

import java.util.Arrays;
import java.util.Random;

//Algorithm:
//
//    Initialize the map randomly with walls and floors (approximately 50/50).
//    Iterate through each tile (excluding edges):
//        Count the number of neighboring walls (including diagonals).
//        Apply rules based on neighbor count:
//            0 neighbors: Become a wall.
//            1-4 neighbors: Become a floor.
//            5+ neighbors: Become a wall. (suggesting rule customization)
//    Repeat step 2 for a set number of iterations.
public class CellularAutomata {
    private final int width;
    private final int height;
    private final Random rand = new Random();
    private final char[][] map;
    public CellularAutomata(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[width][height];
    }

    public char[][] generateMap(int iterations) {
        // Fill with around 50 percent walls, and floors randomly.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (rand.nextDouble() < 0.5) {
                    map[x][y] = '#'; // wall
                } else {
                    map[x][y] = '.'; // floor
                }
            }
        }
        // --- Step 2: Run cellular automata for N iterations
        for (int i = 0; i < iterations; i++) {
            smoothMap();
        }

        return map;
    }

    private void smoothMap() {
        char[][] newMap = new char[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Keep edges solid to avoid out-of-bounds checks
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    newMap[x][y] = '#';
                    continue;
                }

                int wallCount = countWallNeighbors(x, y);

                // --- Apply rules ---
                if (wallCount == 0) {
                    newMap[x][y] = '#';      // 0 neighbors: wall
                } else if (wallCount >= 1 && wallCount <= 4) {
                    newMap[x][y] = '.';      // 1–4 neighbors: floor
                } else {
                    newMap[x][y] = '#';      // 5+ neighbors: wall
                }
            }
        }

        // Copy new map over
        for (int x = 0; x < width; x++) {
            System.arraycopy(newMap[x], 0, map[x], 0, height);
        }
    }

    private int countWallNeighbors(int x, int y) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue; // skip self

                int nx = x + i;
                int ny = y + j;

                // Count as wall if on edge or wall
                if (nx < 0 || ny < 0 || nx >= width || ny >= height) {
                    count++;
                } else if (map[nx][ny] == '#') {
                    count++;
                }
            }
        }
        return count;
    }
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
