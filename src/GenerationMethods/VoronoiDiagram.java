package GenerationMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import assets.Point;

/**
 * Generates a 2D Voronoi diagram using different distance metrics.
 * <p>
 * Each cell in the map is assigned to the nearest seed point, which defines
 * distinct regions represented by unique symbols. Supports Euclidean,
 * Manhattan, and Chebyshev distance types.
 * </p>
 */
public class VoronoiDiagram {
    /** The width of the generated map. */
    private final int width;

    /** The height of the generated map. */
    private final int height;

    /** The 2D array representing the generated Voronoi map. */
    private final char[][] map;

    /** Random number generator used for seed placement. */
    private final Random rand = new Random();

    /** Symbols used to represent distinct regions on the map. */
    private final char[] regionSymbols = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    /**
     * Constructs a new VoronoiDiagram generator with the specified dimensions.
     *
     * @param width  the width of the map
     * @param height the height of the map
     */
    public VoronoiDiagram(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[width][height];
    }

    /**
     * Generates a Voronoi diagram with a specified number of seed points and distance metric.
     * <p>
     * The algorithm:
     * <ol>
     *     <li>Randomly places {@code numSeeds} points across the map.</li>
     *     <li>For each map cell, calculates the distance to all seed points using the selected metric.</li>
     *     <li>Assigns the cell to the region of the closest seed, represented by a unique symbol.</li>
     * </ol>
     * Supported distance types:
     * <ul>
     *     <li><b>Euclidean</b> – straight-line distance (default)</li>
     *     <li><b>Manhattan</b> – grid-based distance</li>
     *     <li><b>Chebyshev</b> – king’s move distance</li>
     * </ul>
     *
     * @param numSeeds     the number of seed points to generate
     * @param distanceType the distance type ("euclidean", "manhattan", or "chebyshev")
     * @return a 2D character array representing the generated Voronoi map
     */
    public char[][] generateMap(int numSeeds, String distanceType) {
        // Step 1: Place seed points
        List<Point> seeds = new ArrayList<>();
        for (int i = 0; i < numSeeds; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            seeds.add(new Point(x, y, regionSymbols[i % regionSymbols.length]));
        }

        // Step 2: Assign each cell to the closest seed
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Point closest = null;
                double closestDist = Double.MAX_VALUE;

                for (Point seed : seeds) {
                    double dist = switch (distanceType.toLowerCase()) {
                        case "manhattan" -> manhattanDistance(x, y, seed.getX(), seed.getY());
                        case "chebyshev" -> chebyshevDistance(x, y, seed.getX(), seed.getY());
                        default -> euclideanDistance(x, y, seed.getX(), seed.getY());
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

    /**
     * Calculates the Euclidean (straight-line) distance between two points.
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @return the Euclidean distance
     */
    private double euclideanDistance(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculates the Manhattan (grid-based) distance between two points.
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @return the Manhattan distance
     */
    private double manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    /**
     * Calculates the Chebyshev (king's move) distance between two points.
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @return the Chebyshev distance
     */
    private double chebyshevDistance(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    /**
     * Returns a string representation of the generated Voronoi map.
     * Each row is followed by a newline character.
     *
     * @return a string representation of the Voronoi diagram
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
