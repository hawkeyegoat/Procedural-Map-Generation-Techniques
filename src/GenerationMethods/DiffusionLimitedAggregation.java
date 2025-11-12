package GenerationMethods;

import java.util.Random;

public class DiffusionLimitedAggregation {
    private final int width;
    private final int height;
    private final char[][] map;
    private final Random rand = new Random();

    public DiffusionLimitedAggregation(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[width][height];
    }

    public char[][] generateMap(int seedSize, int maxParticles) {
        // Step 1: Start with a solid map
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = '#';
            }
        }

        // Step 2: Create an initial open seed in the center
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

        // Step 3: Add new “particles” that try to attach to the open cluster
        int particles = 0;
        while (particles < maxParticles) {
            // Pick a random start point on the map edge
            int x, y;
            if (rand.nextBoolean()) {
                x = rand.nextBoolean() ? 0 : width - 1;
                y = rand.nextInt(height);
            } else {
                x = rand.nextInt(width);
                y = rand.nextBoolean() ? 0 : height - 1;
            }

            // Random direction (dx, dy)
            int dx = 0, dy = 0;

            // Step 4: Move particle until it hits open space
            boolean stuck = false;
            while (!stuck) {
                dx = rand.nextInt(3) - 1; // -1, 0, 1
                dy = rand.nextInt(3) - 1;
                if (dx == 0 && dy == 0) continue; // skip no-move

                int newX = x + dx;
                int newY = y + dy;

                // Stay within bounds
                if (newX <= 0 || newY <= 0 || newX >= width - 1 || newY >= height - 1) {
                    break; // if particle exits map, respawn another
                }

                x = newX;
                y = newY;

                // Check if next to open space (neighbor hit)
                if (isAdjacentToOpen(x, y)) {
                    map[x][y] = '.'; // carve out last solid tile
                    stuck = true;
                }
            }

            particles++;
        }

        return map;
    }

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
