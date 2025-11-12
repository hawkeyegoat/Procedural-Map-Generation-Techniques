package assets;
/**
 * Represents a single point or tile on the map.
 *
 * <p>A {@code Point} holds x and y coordinates along with a display symbol
 * used to visually represent it on the map (for example, walls, floors,
 * or entities). This class is used as a basic unit for rendering or
 * procedural generation.</p>
 *
 * <p>Common use cases include storing the position of objects,
 * drawing map tiles, or representing coordinates during generation algorithms.</p>
 *
 * @author Logan Atkinson
 */
public class Point {
    /** The x position (horizontal coordinate) of the point. */
    private int x;
    /** The y position (vertical coordinate) of the point. */
    private int y;
    /** The character symbol representing this point on the map. */
    private char symbol;

    /**
     * Constructs a {@code Point} with the given coordinates and symbol.
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @param symbol the character symbol to represent this point
     */
    public Point(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    /**
     * Returns the x coordinate of this point.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this point.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the character symbol representing this point.
     *
     * @return the display symbol
     */
    public char getSymbol() {
        return symbol;
    }
}
