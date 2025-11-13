using System;
using System.Text;
using Assets;

namespace GenerationMethods
{
    /// <summary>
    /// The <see cref="DiffusionLimitedAggregation"/> class implements a cave and structure generation
    /// algorithm inspired by natural diffusion-limited growth processes, such as
    /// crystal formation or coral-like patterns.
    /// <para>
    /// Algorithm Summary:
    /// <list type="number">
    ///     <item><description>Start with a solid map filled with walls.</description></item>
    ///     <item><description>Place an initial "seed" of open tiles at the map's center.</description></item>
    ///     <item><description>Spawn random particles along the map's edges.</description></item>
    ///     <item><description>Each particle performs a random walk until it becomes adjacent to an
    ///     existing open tile, at which point it solidifies (carves open space).</description></item>
    ///     <item><description>Repeat the process until the desired number of particles have attached.</description></item>
    /// </list>
    /// </para>
    /// <para>
    /// The result is a branching, organic cave structure resembling dendritic growth
    /// patterns observed in physics and nature.
    /// </para>
    /// </summary>
    /// <author>Logan Atkinson</author>
    public class DiffusionLimitedAggregation
    {
        /// <summary>The total width of the map.</summary>
        private readonly int width;

        /// <summary>The total height of the map.</summary>
        private readonly int height;

        /// <summary>2D array representing the map grid (walls and open tiles).</summary>
        private readonly char[][] map;

        /// <summary>Random number generator for particle movement and placement.</summary>
        private readonly Random rand = new Random();

        /// <summary>
        /// Constructs a <see cref="DiffusionLimitedAggregation"/> generator with a given map size.
        /// </summary>
        /// <param name="width">the width of the map</param>
        /// <param name="height">the height of the map</param>
        public DiffusionLimitedAggregation(int width, int height)
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
        /// Generates a cave-like structure using diffusion-limited aggregation.
        /// </summary>
        /// <param name="seedSize">the radius of the initial open seed in the map center</param>
        /// <param name="maxParticles">the number of diffusing particles to simulate</param>
        /// <returns>a 2D character array representing the generated map</returns>
        public char[][] GenerateMap(int seedSize, int maxParticles)
        {
            // --- Step 1: Initialize map as solid walls ---
            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    map[x][y] = '#';
                }
            }

            // --- Step 2: Create an open seed region at the map center ---
            int centerX = width / 2;
            int centerY = height / 2;
            for (int x = -seedSize; x <= seedSize; x++)
            {
                for (int y = -seedSize; y <= seedSize; y++)
                {
                    int nx = centerX + x;
                    int ny = centerY + y;
                    if (nx > 0 && ny > 0 && nx < width && ny < height)
                    {
                        map[nx][ny] = '.';
                    }
                }
            }

            // --- Step 3: Spawn and diffuse particles until they attach ---
            int particles = 0;
            while (particles < maxParticles)
            {
                // Start at a random map edge
                int x, y;
                if (rand.Next(2) == 0)
                {
                    x = rand.Next(2) == 0 ? 0 : width - 1;
                    y = rand.Next(height);
                }
                else
                {
                    x = rand.Next(width);
                    y = rand.Next(2) == 0 ? 0 : height - 1;
                }

                // Step 4: Particle random walk
                bool stuck = false;
                while (!stuck)
                {
                    int dx = rand.Next(3) - 1; // -1, 0, or 1
                    int dy = rand.Next(3) - 1;
                    if (dx == 0 && dy == 0) continue; // skip stationary move

                    int newX = x + dx;
                    int newY = y + dy;

                    // If particle moves outside map bounds, discard it
                    if (newX <= 0 || newY <= 0 || newX >= width - 1 || newY >= height - 1)
                    {
                        break;
                    }

                    x = newX;
                    y = newY;

                    // Check adjacency to open area
                    if (IsAdjacentToOpen(x, y))
                    {
                        map[x][y] = '.'; // carve open space
                        stuck = true;
                    }
                }

                particles++;
            }

            return map;
        }

        /// <summary>
        /// Determines if a given coordinate is adjacent to an open tile.
        /// </summary>
        /// <param name="x">the x-coordinate to test</param>
        /// <param name="y">the y-coordinate to test</param>
        /// <returns><c>true</c> if the tile is next to an open space ('.'); <c>false</c> otherwise</returns>
        private bool IsAdjacentToOpen(int x, int y)
        {
            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    if (i == 0 && j == 0) continue;
                    int nx = x + i, ny = y + j;
                    if (nx > 0 && ny > 0 && nx < width && ny < height)
                    {
                        if (map[nx][ny] == '.') return true;
                    }
                }
            }
            return false;
        }

        /// <summary>
        /// Returns a formatted string representation of the generated map.
        /// </summary>
        /// <returns>a multi-line string showing the current state of the map</returns>
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
