using System;
using System.Text;
using Assets;

namespace GenerationMethods
{
    /// <summary>
    /// The <c>DrunkardsWalk</c> class implements a procedural map generation algorithm
    /// that simulates a "drunkard" wandering randomly and carving out paths in a solid grid.
    /// <para>
    /// This algorithm is often used for generating cave-like maps or organic dungeon layouts.
    /// It begins with a solid map, places a random walker that moves randomly while carving
    /// open spaces, and repeats this process until a desired percentage of the map is open.
    /// </para>
    /// </summary>
    /// <remarks>
    /// <b>Algorithm Summary:</b>
    /// <list type="number">
    ///     <item><description>Initialize a solid map filled with wall tiles ('#').</description></item>
    ///     <item><description>Choose a random starting point for the drunkard.</description></item>
    ///     <item><description>Move randomly in one of four directions, carving open tiles ('.') as it goes.</description></item>
    ///     <item><description>After a set number of steps, spawn a new drunkard within an open space.</description></item>
    ///     <item><description>Repeat until the target open-space percentage is reached.</description></item>
    /// </list>
    /// <para>
    /// Typical use cases include cave generation, dungeon layouts, or any map that benefits
    /// from irregular organic shapes.
    /// </para>
    /// </remarks>
    public class DrunkardsWalk
    {
        private readonly int width;
        private readonly int height;
        private readonly char[][] map;
        private readonly Random rand = new Random();

        /// <summary>
        /// Constructs a <c>DrunkardsWalk</c> generator with the given map dimensions.
        /// </summary>
        /// <param name="width">the width of the map</param>
        /// <param name="height">the height of the map</param>
        public DrunkardsWalk(int width, int height)
        {
            this.width = width;
            this.height = height;
            this.map = new char[width][];
            for (int i = 0; i < width; i++)
            {
                this.map[i] = new char[height];
            }
        }

        /// <summary>
        /// Generates a map using the Drunkard's Walk algorithm.
        /// </summary>
        /// <param name="openPercent">the fraction of the map to be opened (e.g., 0.33 for 33%)</param>
        /// <param name="maxSteps">the maximum number of steps each drunkard takes before stopping</param>
        /// <returns>a 2D character array representing the generated map,
        ///         where '#' indicates walls and '.' indicates open space</returns>
        public char[][] GenerateMap(double openPercent, int maxSteps)
        {
            // Step 1: Start with a solid map
            for (int i = 0; i < width; i++)
            {
                for (int j = 0; j < height; j++)
                {
                    map[i][j] = '#';
                }
            }

            // Step 2: Choose a random starting point for the drunkard
            int x = rand.Next(width);
            int y = rand.Next(height);

            // Total number of tiles to open based on desired open percentage
            int totalTiles = width * height;
            int targetOpen = (int)(totalTiles * openPercent);
            int openCount = 0;

            // Step 3–5: Drunkard walks, carving until enough open space is created
            while (openCount < targetOpen)
            {
                int steps = 0;

                // Each drunkard gets a limited number of steps before "passing out"
                while (steps < maxSteps)
                {
                    // Carve the current tile if it's still a wall
                    if (map[x][y] == '#')
                    {
                        map[x][y] = '.';
                        openCount++;
                    }

                    // If we've carved enough, stop early
                    if (openCount >= targetOpen) break;

                    // Choose a random direction
                    int dir = rand.Next(4);
                    switch (dir)
                    {
                        case 0:
                            x = Math.Max(1, x - 1);           // left
                            break;
                        case 1:
                            x = Math.Min(width - 2, x + 1);    // right
                            break;
                        case 2:
                            y = Math.Max(1, y - 1);            // up
                            break;
                        case 3:
                            y = Math.Min(height - 2, y + 1);   // down
                            break;
                    }

                    steps++;
                }

                // Step 5: Spawn new drunkard somewhere in an open area
                int newX, newY;
                do
                {
                    newX = rand.Next(width);
                    newY = rand.Next(height);
                } while (map[newX][newY] != '.'); // ensure we start inside carved space

                x = newX;
                y = newY;
            }

            return map;
        }

        /// <summary>
        /// Returns a string representation of the generated map.
        /// <para>
        /// Each row of the map is placed on a new line.
        /// Walls are represented by '#' and open spaces by '.'.
        /// </para>
        /// </summary>
        /// <returns>a string-formatted view of the generated map</returns>
        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    sb.Append(map[x][y]);
                }
                sb.Append('\n');
            }
            return sb.ToString();
        }
    }
}
