package GenerationMethods;

import assets.Node;
import assets.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//Algorithm:
//    Divide the map in half (either vertically or horizontally).
//    Recursively divide each half until the desired room size is reached.
//    Optionally add a gutter (1 tile border) around each room to prevent merging.
public class BinarySpacePartition {
    private final int width;
    private final int height;
    private final char[][] map;
    private final Random rand = new Random();
    private final List<Room> rooms = new ArrayList<>();

    public BinarySpacePartition(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[width][height];
    }

    public char[][] generateMap() {
        // Fill with walls
        for (char[] row : map) Arrays.fill(row, '#');

        Node root = new Node(0, 0, width, height);
        splitSpace(root, 6); // 6 is min room size threshold
        createRooms(root);
        connectRooms(root);

        return map;
    }

    // --- BSP Split ---
    private void splitSpace(Node node, int minSize) {
        if (node == null) return; // safety check

        // stop splitting when node is too small
        if (node.getWidth() <= minSize * 2 && node.getHeight() <= minSize * 2) return;

        boolean splitVertically = rand.nextBoolean();

        // bias toward splitting along the larger axis
        if (node.getWidth() > node.getHeight() && node.getWidth() / (float) node.getHeight() >= 1.25f) {
            splitVertically = true;
        } else if (node.getHeight() > node.getWidth() && node.getHeight() / (float) node.getWidth() >= 1.25f) {
            splitVertically = false;
        }

        int max = (splitVertically ? node.getWidth() : node.getHeight()) - minSize;
        if (max <= minSize) return; // too small to split further

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

        // Only recurse if children exist
        if (node.getLeft() != null) splitSpace(node.getLeft(), minSize);
        if (node.getRight() != null) splitSpace(node.getRight(), minSize);
    }

    // --- Room Creation ---
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

    private void carveRoom(Room r) {
        for (int i = r.getX(); i < r.getX() + r.getWidth(); i++) {
            for (int j = r.getY(); j < r.getY() + r.getHeight(); j++) {
                if (i >= 0 && i < width && j >= 0 && j < height) {
                    map[i][j] = '.';
                }
            }
        }
    }

    // --- Corridor Creation ---
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

    private Room getRoom(Node node) {
        if (node == null) return null;
        if (node.getRoom() != null) return node.getRoom();
        Room leftRoom = getRoom(node.getLeft());
        if (leftRoom != null) return leftRoom;
        return getRoom(node.getRight());
    }

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

    private void carveHorizontalTunnel(int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (x > 0 && x < width && y > 0 && y < height) map[x][y] = '.';
        }
    }

    private void carveVerticalTunnel(int y1, int y2, int x) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (x > 0 && x < width && y > 0 && y < height) map[x][y] = '.';
        }
    }

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
