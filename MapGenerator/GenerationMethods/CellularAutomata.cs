using System;
using System.Text;
using Assets;

namespace GenerationMethods
{
    /// <summary>
    /// The <see cref="CellularAutomata"/> class implements a procedural generation technique
    /// based on cellular automata rules. It generates natural-looking cave structures
    /// by iteratively smoothing a randomly generated grid of walls and floors.
    /// <para>
    /// Algorithm Summary:
    /// <list type="number">
    ///     <item>Initialize the map with approximately 50% walls and 50% floors.</item>
    ///     <item>Iterate through the grid for a specified number of iterations.</item>
    ///     <item>For each tile, count neighboring wall tiles (including diagonals).</item>
    ///     <item>Apply rules based on wall neighbor count:
    ///         <list type="bullet">
    ///             <item>0 neighbors: Become a wall.</item>
    ///             <item>1–4 neighbors: Become a floor.</item>
    ///             <item>5+ neighbors: Become a wall.</item>
    ///         </list>
    ///     </item>
    /// </list>
    /// This process yields organic cave or cavern-like maps useful for dungeon generation.
    /// </para>
    /// </summary>
    /// <remarks>
    /// Author: Logan Atkinson
    /// </remarks>
    public class CellularAutomata
    {
        /// <summary>The total width of the map.</summary>
        private readonly int width;
        /// <summary>The total height of the map.</summary>
        private readonly int height;
        /// <summary>Random number generator for initialization and rule application.</summary>
        private readonly Random rand = new Random();
        /// <summary>The 2D character array representing the generated map.</summary>
        private readonly char[][] map;

        /// <summary>
        /// Constructs a <see cref="CellularAutomata"/> generator for a map of the given dimensions.
        /// </summary>
        /// <param name="width">the width of the map</param>
        /// <param name="height">the height of the map</param>
        public CellularAutomata(int width, int height)
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
        /// Generates a cave-like map using cellular automata rules.
        /// </summary>
        /// <param name="iterations">the number of smoothing iterations to perform</param>
        /// <returns>a 2D character array representing the generated map</returns>
        public char[][] GenerateMap(int iterations)
        {
            // Initialize the map randomly with walls and floors
            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    map[x][y] = (rand.NextDouble() < 0.5) ? '#' : '.';
                }
            }

            // Run cellular automata smoothing for the specified number of iterations
            for (int i = 0; i < iterations; i++)
            {
                smoothMap();
            }

            return map;
        }

        /// <summary>
        /// Applies one iteration of cellular automata smoothing rules
        /// to the entire map.
        /// </summary>
        private void smoothMap()
        {
            char[][] newMap = new char[width][];
            for (int i = 0; i < width; i++)
            {
                newMap[i] = new char[height];
            }

            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    // Keep edges solid to prevent boundary errors
                    if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
                    {
                        newMap[x][y] = '#';
                        continue;
                    }

                    int wallCount = countWallNeighbors(x, y);

                    // Apply neighbor rules
                    if (wallCount == 0)
                    {
                        newMap[x][y] = '#';
                    }
                    else if (wallCount >= 1 && wallCount <= 4)
                    {
                        newMap[x][y] = '.';
                    }
                    else
                    {
                        newMap[x][y] = '#';
                    }
                }
            }

            // Copy smoothed map back to the main map
            for (int x = 0; x < width; x++)
            {
                Array.Copy(newMap[x], 0, map[x], 0, height);
            }
        }

        /// <summary>
        /// Counts the number of wall tiles surrounding a given coordinate.
        /// Includes diagonals and handles edges as walls.
        /// </summary>
        /// <param name="x">the x-coordinate of the tile</param>
        /// <param name="y">the y-coordinate of the tile</param>
        /// <returns>the number of neighboring wall tiles</returns>
        private int countWallNeighbors(int x, int y)
        {
            int count = 0;

            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    if (i == 0 && j == 0) continue; // skip self

                    int nx = x + i;
                    int ny = y + j;

                    // Out-of-bounds counts as wall
                    if (nx < 0 || ny < 0 || nx >= width || ny >= height)
                    {
                        count++;
                    }
                    else if (map[nx][ny] == '#')
                    {
                        count++;
                    }
                }
            }
            return count;
        }

        /// <summary>
        /// Returns a string representation of the current map.
        /// </summary>
        /// <returns>a multi-line string showing the generated map</returns>
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
