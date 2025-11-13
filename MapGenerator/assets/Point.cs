namespace Assets
{
    /// <summary>
    /// Represents a single point or tile on the map.
    ///
    /// <para>A <see cref="Point"/> holds x and y coordinates along with a display symbol
    /// used to visually represent it on the map (for example, walls, floors,
    /// or entities). This class is used as a basic unit for rendering or
    /// procedural generation.</para>
    ///
    /// <para>Common use cases include storing the position of objects,
    /// drawing map tiles, or representing coordinates during generation algorithms.</para>
    ///
    /// <author>Logan Atkinson</author>
    /// </summary>
    public class Point
    {
        /// <summary>
        /// The x position (horizontal coordinate) of the point.
        /// </summary>
        private int x;

        /// <summary>
        /// The y position (vertical coordinate) of the point.
        /// </summary>
        private int y;

        /// <summary>
        /// The character symbol representing this point on the map.
        /// </summary>
        private char symbol;

        /// <summary>
        /// Constructs a <see cref="Point"/> with the given coordinates and symbol.
        /// </summary>
        /// <param name="x">The x coordinate of the point</param>
        /// <param name="y">The y coordinate of the point</param>
        /// <param name="symbol">The character symbol to represent this point</param>
        public Point(int x, int y, char symbol)
        {
            X = x;
            Y = y;
            Symbol = symbol;
        }
        public int X {get; set;}
        public int Y {get; set;}
        public char Symbol {get; set;}
    }
}