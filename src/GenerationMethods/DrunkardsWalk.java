package GenerationMethods;

import java.util.Random;

//Algorithm:
//
//    Start with a solid map.
//    Place a “drunkard” (e.g., a digging entity) at a random point.
//    The drunkard moves randomly, carving out a path as it goes.
//    Set a maximum distance for the drunkard to travel before it “passes out”.
//    Repeat steps 2-4, spawning new drunkards within the open areas, until a desired percentage of the map is open. (using 1/3 as an example)
public class DrunkardsWalk {
    private final int width;
    private final int height;
    private final char[][] map;
    private final Random rand = new Random();

    public DrunkardsWalk(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[width][height];
    }

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
