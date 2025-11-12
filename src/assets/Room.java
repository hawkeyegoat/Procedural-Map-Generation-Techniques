package assets;

/**
 * Represents a rectangular room used in procedural map generation.
 *
 * <p>A {@code Room} defines a bounded area on a grid, described by its top-left
 * coordinates and dimensions. It is commonly used in generation algorithms such as
 * Binary Space Partitioning (BSP) or Simple Room Placement to represent spaces that
 * can be carved, connected, or rendered on a map.</p>
 *
 * <p>Each room provides utility methods for checking overlap (intersection)
 * and finding its center coordinates — often used when connecting rooms
 * with corridors or tunnels.</p>
 *
 * @author Logan Atkinson
 */
public class Room {

    /** The x-coordinate (horizontal position) of the room’s top-left corner. */
    private int x;

    /** The y-coordinate (vertical position) of the room’s top-left corner. */
    private int y;

    /** The width of the room in tiles. */
    private int width;

    /** The height of the room in tiles. */
    private int height;

    /**
     * Constructs a {@code Room} with the specified position and dimensions.
     *
     * @param x the x-coordinate of the top-left corner
     * @param y the y-coordinate of the top-left corner
     * @param width the total width of the room
     * @param height the total height of the room
     */
    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Checks whether this room intersects (overlaps) another room.
     *
     * @param other the other room to check for intersection
     * @return {@code true} if the rooms overlap; {@code false} otherwise
     */
    public boolean intersects(Room other) {
        return (x <= other.x + other.width && x + width >= other.x &&
                y <= other.y + other.height && y + height >= other.y);
    }

    /**
     * Returns the x-coordinate of the center of this room.
     *
     * @return the center x-coordinate
     */
    public int centerX() {
        return x + width / 2;
    }

    /**
     * Returns the y-coordinate of the center of this room.
     *
     * @return the center y-coordinate
     */
    public int centerY() {
        return y + height / 2;
    }

    /**
     * Returns the total height of this room.
     *
     * @return the height in tiles
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the total width of this room.
     *
     * @return the width in tiles
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the x-coordinate of the top-left corner of this room.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the top-left corner of this room.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }
}
