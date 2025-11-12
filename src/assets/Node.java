package assets;

/**
 * Represents a rectangular partition or node used by generation methods,
 * primarily for Binary Space Partitioning (BSP) map generation.
 *
 * <p>Each {@code Node} defines a rectangular area of the map with coordinates
 * and dimensions. Nodes can be recursively divided into left and right child
 * nodes, allowing hierarchical spatial subdivision. Leaf nodes may contain
 * {@link Room} objects representing playable rooms within the map.</p>
 *
 * <p>This class acts as a fundamental data structure for procedural generation
 * algorithms that require recursive spatial partitioning, such as dungeon or
 * maze generation.</p>
 *
 * @author Logan Atkinson
 */
public class Node {
    /** The x position (horizontal coordinate) of the node's top-left corner. */
    private int x;
    /** The y position (vertical coordinate) of the node's top-left corner. */
    private int y;
    /** The total width of this node's area. */
    private int width;
    /** The total height of this node's area. */
    private int height;
    /** Reference to the left child node (first partition). */
    private Node left;
    /** Reference to the right child node (second partition). */
    private Node right;
    /** The {@link Room} contained within this node, if this node is a leaf. */
    private Room room;
    /**
     * Constructs a new {@code Node} representing a rectangular area.
     *
     * @param x the x position (horizontal coordinate) of the node
     * @param y the y position (vertical coordinate) of the node
     * @param width the width of the node
     * @param height the height of the node
     */
    public Node(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    /**
     * Returns the y position of this node.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }
    /**
     * Returns the width of this node.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }
    /**
     * Returns the x position of this node.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }
    /**
     * Returns the height of this node.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }
    /**
     * Returns the left child node of this partition.
     *
     * @return the left child node, or {@code null} if none
     */
    public Node getLeft() {
        return left;
    }
    /**
     * Sets the left child node of this partition.
     *
     * @param left the left child node
     */
    public void setLeft(Node left) {
        this.left = left;
    }
    /**
     * Returns the right child node of this partition.
     *
     * @return the right child node, or {@code null} if none
     */
    public Node getRight() {
        return right;
    }
    /**
     * Sets the right child node of this partition.
     *
     * @param right the right child node
     */
    public void setRight(Node right) {
        this.right = right;
    }
    /**
     * Returns the {@link Room} contained within this node.
     *
     * @return the room, or {@code null} if this node does not contain one
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Assigns a {@link Room} to this node.
     *
     * @param room the room to assign
     */
    public void setRoom(Room room) {
        this.room = room;
    }
    /**
     * Determines whether this node is a leaf node
     * (i.e., has no child nodes).
     *
     * @return {@code true} if this node has no children, {@code false} otherwise
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }
}
