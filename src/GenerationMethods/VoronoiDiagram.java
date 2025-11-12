package GenerationMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import assets.Point;

public class VoronoiDiagram {
    private final int width;
    private final int height;
    private final char[][] map;
    private final Random rand = new Random();

    // Characters used for different regions (can be anything)
    private final char[] regionSymbols = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    public VoronoiDiagram(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[width][height];
    }

    public char[][] generateMap(int numSeeds, String distanceType) {
        // Step 1: Place seed points
        List<Point> seeds = new ArrayList<>();
        for (int i = 0; i < numSeeds; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            seeds.add(new Point(x, y, regionSymbols[i % regionSymbols.length]));
        }

        // Step 2: Assign each tile to the closest seed
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Point closest = null;
                double closestDist = Double.MAX_VALUE;

                for (Point seed : seeds) {
                    double dist = switch (distanceType.toLowerCase()) {
                        case "manhattan" -> manhattanDistance(x, y, seed.getX(), seed.getY());
                        case "chebyshev" -> chebyshevDistance(x, y, seed.getX(), seed.getY());
                        default -> euclideanDistance(x, y, seed.getX(), seed.getY()); // default Euclidean
                    };

                    if (dist < closestDist) {
                        closestDist = dist;
                        closest = seed;
                    }
                }

                map[x][y] = closest.getSymbol();
            }
        }

        return map;
    }

    // --- Distance functions ---
    private double euclideanDistance(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private double chebyshevDistance(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
    //----------------------------
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
