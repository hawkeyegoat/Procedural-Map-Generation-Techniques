namespace Assets
{
    /// <summary>
    /// Represents a rectangular room used in procedural map generation.
    ///
    /// A <see cref="Room"/> defines a bounded area on a grid, described by its top-left
    /// coordinates and dimensions. It is commonly used in generation algorithms such as
    /// Binary Space Partitioning (BSP) or Simple Room Placement to represent spaces that
    /// can be carved, connected, or rendered on a map.
    ///
    /// Each room provides utility methods for checking overlap (intersection)
    /// and finding its center coordinates — often used when connecting rooms
    /// with corridors or tunnels.
    /// </summary>
    public class Room
    {
        /// <summary>
        /// The x-coordinate (horizontal position) of the room’s top-left corner.
        /// </summary>
        private int x;

        /// <summary>
        /// The y-coordinate (vertical position) of the room’s top-left corner.
        /// </summary>
        private int y;

        /// <summary>
        /// The width of the room in tiles.
        /// </summary>
        private int width;

        /// <summary>
        /// The height of the room in tiles.
        /// </summary>
        private int height;

        /// <summary>
        /// Constructs a <see cref="Room"/> with the specified position and dimensions.
        /// </summary>
        /// <param name="x">The x-coordinate of the top-left corner</param>
        /// <param name="y">The y-coordinate of the top-left corner</param>
        /// <param name="width">The total width of the room</param>
        /// <param name="height">The total height of the room</param>
        public Room(int x, int y, int width, int height)
        {
            this.X = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int X { get; set; }
        public int Y { get; set; }
        public int Width { get; set; }
        public int Height { get; set; }


        /// <summary>
        /// Checks whether this room intersects (overlaps) another room.
        /// </summary>
        /// <param name="other">The other room to check for intersection</param>
        /// <returns><c>true</c> if the rooms overlap; <c>false</c> otherwise</returns>
        public bool Intersects(Room other)
        {
            return (x <= other.X + other.width && x + width >= other.X &&
                    y <= other.y + other.height && y + height >= other.y);
        }

        /// <summary>
        /// Returns the x-coordinate of the center of this room.
        /// </summary>
        /// <returns>The center x-coordinate</returns>
        public int CenterX()
        {
            return x + width / 2;
        }

        /// <summary>
        /// Returns the y-coordinate of the center of this room.
        /// </summary>
        /// <returns>The center y-coordinate</returns>
        public int CenterY()
        {
            return y + height / 2;
        }
    }
}