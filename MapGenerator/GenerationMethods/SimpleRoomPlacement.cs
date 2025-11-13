using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Assets;

namespace GenerationMethods
{
    /// <summary>
    /// Generates a simple dungeon map by placing randomly sized rectangular rooms
    /// and connecting them with corridors.
    /// <para>
    /// Each room is placed without overlapping existing ones and connected using
    /// a "dogleg" corridor pattern (horizontal + vertical or vertical + horizontal).
    /// The map is represented as a 2D character array, where walls are '#' and open
    /// floor spaces are '.'.
    /// </para>
    /// </summary>
    public class SimpleRoomPlacement
    {
        /// <summary>The width of the generated map.</summary>
        private int myWidth;

        /// <summary>The height of the generated map.</summary>
        private int myHeight;

        /// <summary>The 2D character array representing the map layout.</summary>
        private char[][] myMap;

        /// <summary>Random number generator used for room placement and corridor decisions.</summary>
        private readonly Random random = new Random();

        /// <summary>List of rooms generated on the map.</summary>
        private readonly List<Room> rooms = new List<Room>();

        /// <summary>
        /// Constructs a new SimpleRoomPlacement generator with the specified map dimensions.
        /// </summary>
        /// <param name="theWidth">the width of the map</param>
        /// <param name="theHeight">the height of the map</param>
        public SimpleRoomPlacement(int theWidth, int theHeight)
        {
            myWidth = theWidth;
            myHeight = theHeight;
            myMap = new char[theWidth][];
            for (int i = 0; i < theWidth; i++)
            {
                myMap[i] = new char[theHeight];
            }
        }

        /// <summary>
        /// Generates a dungeon map consisting of rectangular rooms connected by corridors.
        /// <para>
        /// Starts with a solid wall grid ('#'), then randomly places up to 10 rooms
        /// of random sizes between 4x4 and 8x8 tiles. Each valid room is connected
        /// to the previous room by a corridor.
        /// </para>
        /// </summary>
        /// <returns>a 2D character array representing the generated dungeon map</returns>
        public char[][] GenerateMap()
        {
            int roomCount = 0;
            int maxRooms = 10;
            int minSize = 4;
            int maxSize = 8;

            // Start with a solid map (all walls).
            foreach (var a in myMap)
            {
                Array.Fill(a, '#');
            }

            // Generate rooms until the maximum count is reached.
            while (roomCount < maxRooms)
            {
                int w = random.Next(maxSize - minSize + 1) + minSize;
                int h = random.Next(maxSize - minSize + 1) + minSize;
                int x = random.Next(myWidth - w - 1) + 1;
                int y = random.Next(myHeight - h - 1) + 1;

                Room newRoom = new Room(x, y, w, h);
                bool overlap = false;

                // Check if the new room overlaps with existing rooms
                foreach (Room other in rooms)
                {
                    if (newRoom.Intersects(other))
                    {
                        overlap = true;
                        break;
                    }
                }

                // If no overlap, carve the room and connect it to the previous one
                if (!overlap)
                {
                    carveRoom(newRoom);
                    if (rooms.Count > 0)
                    {
                        Room prev = rooms[rooms.Count - 1];
                        createDoglegCorridor(prev, newRoom);
                    }
                    rooms.Add(newRoom);
                    roomCount++;
                }
            }
            return myMap;
        }

        /// <summary>
        /// Carves out a rectangular room in the map by replacing walls ('#') with floors ('.').
        /// </summary>
        /// <param name="r">the <see cref="Room"/> to carve out</param>
        private void carveRoom(Room r)
        {
            for (int i = r.X; i < r.X + r.Width; i++)
            {
                for (int j = r.Y; j < r.Y + r.Height; j++)
                {
                    myMap[i][j] = '.';
                }
            }
        }

        /// <summary>
        /// Creates a corridor connecting two rooms using a dogleg (L-shaped) pattern.
        /// <para>
        /// The order of horizontal and vertical carving is randomized.
        /// </para>
        /// </summary>
        /// <param name="r1">the first room</param>
        /// <param name="r2">the second room</param>
        private void createDoglegCorridor(Room r1, Room r2)
        {
            int x1 = r1.CenterX();
            int y1 = r1.CenterY();
            int x2 = r2.CenterX();
            int y2 = r2.CenterY();

            if (random.Next(2) == 1)
            {
                carveHorizontalTunnel(x1, x2, y1);
                carveVerticalTunnel(y1, y2, x2);
            }
            else
            {
                carveVerticalTunnel(y1, y2, x1);
                carveHorizontalTunnel(x1, x2, y2);
            }
        }

        /// <summary>
        /// Carves a horizontal tunnel between two x-coordinates at a given y-coordinate.
        /// </summary>
        /// <param name="x1">the starting x-coordinate</param>
        /// <param name="x2">the ending x-coordinate</param>
        /// <param name="y">the y-coordinate of the tunnel</param>
        private void carveHorizontalTunnel(int x1, int x2, int y)
        {
            for (int x = Math.Min(x1, x2); x <= Math.Max(x1, x2); x++)
            {
                if (x > 0 && x < myWidth && y > 0 && y < myHeight)
                {
                    myMap[x][y] = '.';
                }
            }
        }

        /// <summary>
        /// Carves a vertical tunnel between two y-coordinates at a given x-coordinate.
        /// </summary>
        /// <param name="y1">the starting y-coordinate</param>
        /// <param name="y2">the ending y-coordinate</param>
        /// <param name="x">the x-coordinate of the tunnel</param>
        private void carveVerticalTunnel(int y1, int y2, int x)
        {
            for (int y = Math.Min(y1, y2); y <= Math.Max(y1, y2); y++)
            {
                if (x > 0 && x < myWidth && y > 0 && y < myHeight)
                {
                    myMap[x][y] = '.';
                }
            }
        }

        /// <summary>
        /// Returns a string representation of the generated map.
        /// Each row of the map is followed by a newline.
        /// </summary>
        /// <returns>a string representation of the dungeon layout</returns>
        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < myWidth; i++)
            {
                for (int j = 0; j < myHeight; j++)
                {
                    sb.Append(myMap[i][j]);
                }
                sb.Append('\n');
            }
            return sb.ToString();
        }
    }
}
