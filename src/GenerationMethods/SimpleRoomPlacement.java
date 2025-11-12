package GenerationMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import assets.Room;

/**
 * Generates a simple dungeon map by placing randomly sized rectangular rooms
 * and connecting them with corridors.
 * <p>
 * Each room is placed without overlapping existing ones and connected using
 * a "dogleg" corridor pattern (horizontal + vertical or vertical + horizontal).
 * The map is represented as a 2D character array, where walls are '#' and open
 * floor spaces are '.'.
 * </p>
 */
public class SimpleRoomPlacement {
    /** The width of the generated map. */
    private int myWidth;

    /** The height of the generated map. */
    private int myHeight;

    /** The 2D character array representing the map layout. */
    private char[][] myMap;

    /** Random number generator used for room placement and corridor decisions. */
    private final Random random = new Random();

    /** List of rooms generated on the map. */
    private final List<Room> rooms = new ArrayList<>();

    /**
     * Constructs a new SimpleRoomPlacement generator with the specified map dimensions.
     *
     * @param theWidth  the width of the map
     * @param theHeight the height of the map
     */
    public SimpleRoomPlacement(final int theWidth, final int theHeight) {
        myWidth = theWidth;
        myHeight = theHeight;
        myMap = new char[theWidth][theHeight];
    }

    /**
     * Generates a dungeon map consisting of rectangular rooms connected by corridors.
     * <p>
     * Starts with a solid wall grid ('#'), then randomly places up to 10 rooms
     * of random sizes between 4x4 and 8x8 tiles. Each valid room is connected
     * to the previous room by a corridor.
     * </p>
     *
     * @return a 2D character array representing the generated dungeon map
     */
    public char[][] GenerateMap() {
        int roomCount = 0;
        int maxRooms = 10;
        int minSize = 4;
        int maxSize = 8;

        // Start with a solid map (all walls).
        Arrays.stream(myMap).forEach(a -> Arrays.fill(a, '#'));

        // Generate rooms until the maximum count is reached.
        while (roomCount < maxRooms) {
            int w = random.nextInt(maxSize - minSize + 1) + minSize;
            int h = random.nextInt(maxSize - minSize + 1) + minSize;
            int x = random.nextInt(myWidth - w - 1) + 1;
            int y = random.nextInt(myHeight - h - 1) + 1;

            Room newRoom = new Room(x, y, w, h);
            boolean overlap = false;

            // Check if the new room overlaps with existing rooms
            for (Room other : rooms) {
                if (newRoom.intersects(other)) {
                    overlap = true;
                    break;
                }
            }

            // If no overlap, carve the room and connect it to the previous one
            if (!overlap) {
                carveRoom(newRoom);
                if (!rooms.isEmpty()) {
                    Room prev = rooms.get(rooms.size() - 1);
                    createDoglegCorridor(prev, newRoom);
                }
                rooms.add(newRoom);
                roomCount++;
            }
        }
        return myMap;
    }

    /**
     * Carves out a rectangular room in the map by replacing walls ('#') with floors ('.').
     *
     * @param r the {@link Room} to carve out
     */
    private void carveRoom(Room r) {
        for (int i = r.getX(); i < r.getX() + r.getWidth(); i++) {
            for (int j = r.getY(); j < r.getY() + r.getHeight(); j++) {
                myMap[i][j] = '.';
            }
        }
    }

    /**
     * Creates a corridor connecting two rooms using a dogleg (L-shaped) pattern.
     * <p>
     * The order of horizontal and vertical carving is randomized.
     * </p>
     *
     * @param r1 the first room
     * @param r2 the second room
     */
    private void createDoglegCorridor(Room r1, Room r2) {
        int x1 = r1.centerX();
        int y1 = r1.centerY();
        int x2 = r2.centerX();
        int y2 = r2.centerY();

        if (random.nextBoolean()) {
            carveHorizontalTunnel(x1, x2, y1);
            carveVerticalTunnel(y1, y2, x2);
        } else {
            carveVerticalTunnel(y1, y2, x1);
            carveHorizontalTunnel(x1, x2, y2);
        }
    }

    /**
     * Carves a horizontal tunnel between two x-coordinates at a given y-coordinate.
     *
     * @param x1 the starting x-coordinate
     * @param x2 the ending x-coordinate
     * @param y  the y-coordinate of the tunnel
     */
    private void carveHorizontalTunnel(int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (x > 0 && x < myWidth && y > 0 && y < myHeight) {
                myMap[x][y] = '.';
            }
        }
    }

    /**
     * Carves a vertical tunnel between two y-coordinates at a given x-coordinate.
     *
     * @param y1 the starting y-coordinate
     * @param y2 the ending y-coordinate
     * @param x  the x-coordinate of the tunnel
     */
    private void carveVerticalTunnel(int y1, int y2, int x) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (x > 0 && x < myWidth && y > 0 && y < myHeight) {
                myMap[x][y] = '.';
            }
        }
    }

    /**
     * Returns a string representation of the generated map.
     * Each row of the map is followed by a newline.
     *
     * @return a string representation of the dungeon layout
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < myWidth; i++) {
            for (int j = 0; j < myHeight; j++) {
                sb.append(myMap[i][j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
