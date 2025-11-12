package GenerationMethods;

import assets.Node;
import assets.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Implements the Binary Space Partitioning (BSP) dungeon generation algorithm.
 *
 * <p>The {@code BinarySpacePartition} class recursively divides the map into
 * smaller subspaces, creating rooms within each final leaf node and connecting
 * them with corridors. This approach is widely used in roguelike and procedural
 * generation systems to produce varied and structured dungeon layouts.</p>
 *
 * <h3>Algorithm Summary:</h3>
 * <ol>
 *     <li>Start with a solid map filled with walls.</li>
 *     <li>Recursively divide the map into smaller regions using BSP.</li>
 *     <li>Create a room in each leaf node (with optional gutter spacing).</li>
 *     <li>Connect the rooms with simple L-shaped (dogleg) corridors.</li>
 * </ol>
 *
 * <p>This implementation supports randomly biased splits based on region
 * proportions to avoid overly uniform maps.</p>
 *
 * <h3>Limitations:</h3>
 * <ul>
 *     <li>May produce clustered rooms if split thresholds are not tuned.</li>
 *     <li>Corridors are always straight or L-shaped.</li>
 * </ul>
 *
 * @author Logan Atkinson
 */
public class BinarySpacePartition {
    /** Total width of the generated map. */
    private final int width;
    /** Total height of the generated map. */
    private final int height;
    /** 2D array representing the map grid. */
    private final char[][] map;
    /** Random number generator used for splits and placements. */
    private final Random rand = new Random();
    /** List of rooms created within the BSP tree. */
    private final List<Room> rooms = new ArrayList<>();

    /**
     * Constructs a new {@code BinarySpacePartition} generator.
     *
     * @param width  the total width of the map
     * @param height the total height of the map
     */
    public BinarySpacePartition(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[width][height];
    }

    /**
     * Generates a dungeon map using Binary Space Partitioning.
     *
     * @return a 2D {@code char} array representing the generated map
     */
    public char[][] generateMap() {
        // Fill with walls
        for (char[] row : map) Arrays.fill(row, '#');

        Node root = new Node(0, 0, width, height);
        splitSpace(root, 6); // 6 = minimum room size threshold
        createRooms(root);
        connectRooms(root);

        return map;
    }

    /**
     * Recursively divides the given space into subregions until the minimum size is reached.
     *
     * @param node    the current node to split
     * @param minSize the minimum width/height before stopping division
     */
    private void splitSpace(Node node, int minSize) {
        if (node == null) return;

        // Stop splitting when node is too small
        if (node.getWidth() <= minSize * 2 && node.getHeight() <= minSize * 2) return;

        boolean splitVertically = rand.nextBoolean();

        // Bias toward splitting along the larger axis
        if (node.getWidth() > node.getHeight() && node.getWidth() / (float) node.getHeight() >= 1.25f) {
            splitVertically = true;
        } else if (node.getHeight() > node.getWidth() && node.getHeight() / (float) node.getWidth() >= 1.25f) {
            splitVertically = false;
        }

        int max = (splitVertically ? node.getWidth() : node.getHeight()) - minSize;
        if (max <= minSize) return;

        int split = rand.nextInt(max - minSize + 1) + minSize;

        if (splitVertically) {
            if (split < node.getWidth() - 1) {
                node.setLeft(new Node(node.getX(), node.getY(), split, node.getHeight()));
                node.setRight(new Node(node.getX() + split, node.getY(), node.getWidth() - split, node.getHeight()));
            }
        } else {
            if (split < node.getHeight() - 1) {
                node.setLeft(new Node(node.getX(), node.getY(), node.getWidth(), split));
                node.setRight(new Node(node.getX(), node.getY() + split, node.getWidth(), node.getHeight() - split));
            }
        }

        if (node.getLeft() != null) splitSpace(node.getLeft(), minSize);
        if (node.getRight() != null) splitSpace(node.getRight(), minSize);
    }

    /**
     * Recursively creates rooms within each leaf node of the BSP tree.
     *
     * @param node the current node being processed
     */
    private void createRooms(Node node) {
        if (node.isLeaf()) {
            int gutter = 1;
            int roomWidth = rand.nextInt(node.getWidth() - gutter * 2 - 3) + 3;
            int roomHeight = rand.nextInt(node.getHeight() - gutter * 2 - 3) + 3;
            int roomX = node.getX() + gutter + rand.nextInt(Math.max(1, node.getWidth() - roomWidth - gutter * 2));
            int roomY = node.getY() + gutter + rand.nextInt(Math.max(1, node.getHeight() - roomHeight - gutter * 2));

            Room room = new Room(roomX, roomY, roomWidth, roomHeight);
            carveRoom(room);
            node.setRoom(room);
            rooms.add(room);
        } else {
            if (node.getLeft() != null) createRooms(node.getLeft());
            if (node.getRight() != null) createRooms(node.getRight());
        }
    }

    /**
     * Converts a rectangular region of the map into open floor tiles.
     *
     * @param r the room to carve
     */
    private void carveRoom(Room r) {
        for (int i = r.getX(); i < r.getX() + r.getWidth(); i++) {
            for (int j = r.getY(); j < r.getY() + r.getHeight(); j++) {
                if (i >= 0 && i < width && j >= 0 && j < height) {
                    map[i][j] = '.';
                }
            }
        }
    }

    /**
     * Connects rooms in the BSP tree using L-shaped corridors.
     *
     * @param node the current BSP node
     */
    private void connectRooms(Node node) {
        if (node.getLeft() == null || node.getRight() == null) return;

        connectRooms(node.getLeft());
        connectRooms(node.getRight());

        Room roomA = getRoom(node.getLeft());
        Room roomB = getRoom(node.getRight());
        if (roomA != null && roomB != null) {
            carveCorridor(roomA, roomB);
        }
    }

    /**
     * Traverses the BSP tree to find a valid room within the given node.
     *
     * @param node the node to search
     * @return the first non-null room found, or {@code null} if none exist
     */
    private Room getRoom(Node node) {
        if (node == null) return null;
        if (node.getRoom() != null) return node.getRoom();
        Room leftRoom = getRoom(node.getLeft());
        if (leftRoom != null) return leftRoom;
        return getRoom(node.getRight());
    }

    /**
     * Creates an L-shaped corridor between two rooms.
     *
     * @param a the first room
     * @param b the second room
     */
    private void carveCorridor(Room a, Room b) {
        int x1 = a.centerX();
        int y1 = a.centerY();
        int x2 = b.centerX();
        int y2 = b.centerY();

        if (rand.nextBoolean()) {
            carveHorizontalTunnel(x1, x2, y1);
            carveVerticalTunnel(y1, y2, x2);
        } else {
            carveVerticalTunnel(y1, y2, x1);
            carveHorizontalTunnel(x1, x2, y2);
        }
    }

    /**
     * Carves a horizontal tunnel between two x-coordinates at a given y position.
     */
    private void carveHorizontalTunnel(int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (x > 0 && x < width && y > 0 && y < height) map[x][y] = '.';
        }
    }

    /**
     * Carves a vertical tunnel between two y-coordinates at a given x position.
     */
    private void carveVerticalTunnel(int y1, int y2, int x) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (x > 0 && x < width && y > 0 && y < height) map[x][y] = '.';
        }
    }

    /**
     * Returns a string representation of the generated map, formatted for printing.
     *
     * @return a multi-line string representing the dungeon layout
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                sb.append(map[i][j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
