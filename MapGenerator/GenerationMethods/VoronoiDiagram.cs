using System;
using System.Collections.Generic;
using System.Text;
using Assets;

namespace GenerationMethods
{
    /// <summary>
    /// Generates a 2D Voronoi diagram using different distance metrics.
    /// <para>
    /// Each cell in the map is assigned to the nearest seed point, which defines
    /// distinct regions represented by unique symbols. Supports Euclidean,
    /// Manhattan, and Chebyshev distance types.
    /// </para>
    /// </summary>
    public class VoronoiDiagram
    {
        /// <summary>
        /// The width of the generated map.
        /// </summary>
        private readonly int width;

        /// <summary>
        /// The height of the generated map.
        /// </summary>
        private readonly int height;

        /// <summary>
        /// The 2D array representing the generated Voronoi map.
        /// </summary>
        private readonly char[][] map;

        /// <summary>
        /// Random number generator used for seed placement.
        /// </summary>
        private readonly Random rand = new Random();

        /// <summary>
        /// Symbols used to represent distinct regions on the map.
        /// </summary>
        private readonly char[] regionSymbols = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };

        /// <summary>
        /// Constructs a new VoronoiDiagram generator with the specified dimensions.
        /// </summary>
        /// <param name="width">the width of the map</param>
        /// <param name="height">the height of the map</param>
        public VoronoiDiagram(int width, int height)
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
        /// Generates a Voronoi diagram with a specified number of seed points and distance metric.
        /// <para>
        /// The algorithm:
        /// <list type="number">
        ///     <item>Randomly places <paramref name="numSeeds"/> points across the map.</item>
        ///     <item>For each map cell, calculates the distance to all seed points using the selected metric.</item>
        ///     <item>Assigns the cell to the region of the closest seed, represented by a unique symbol.</item>
        /// </list>
        /// Supported distance types:
        /// <list type="bullet">
        ///     <item><b>Euclidean</b> – straight-line distance (default)</item>
        ///     <item><b>Manhattan</b> – grid-based distance</item>
        ///     <item><b>Chebyshev</b> – king's move distance</item>
        /// </list>
        /// </para>
        /// </summary>
        /// <param name="numSeeds">the number of seed points to generate</param>
        /// <param name="distanceType">the distance type ("euclidean", "manhattan", or "chebyshev")</param>
        /// <returns>a 2D character array representing the generated Voronoi map</returns>
        public char[][] GenerateMap(int numSeeds, string distanceType)
        {
            // Step 1: Place seed points
            List<Point> seeds = new List<Point>();
            for (int i = 0; i < numSeeds; i++)
            {
                int x = rand.Next(width);
                int y = rand.Next(height);
                seeds.Add(new Point(x, y, regionSymbols[i % regionSymbols.Length]));
            }

            // Step 2: Assign each cell to the closest seed
            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    Point closest = null;
                    double closestDist = double.MaxValue;

                    foreach (Point seed in seeds)
                    {
                        double dist = distanceType.ToLower() switch
                        {
                            "manhattan" => ManhattanDistance(x, y, seed.X, seed.Y),
                            "chebyshev" => ChebyshevDistance(x, y, seed.X, seed.Y),
                            _ => EuclideanDistance(x, y, seed.X, seed.Y)
                        };

                        if (dist < closestDist)
                        {
                            closestDist = dist;
                            closest = seed;
                        }
                    }

                    map[x][y] = closest.Symbol;
                }
            }

            return map;
        }

        /// <summary>
        /// Calculates the Euclidean (straight-line) distance between two points.
        /// </summary>
        /// <param name="x1">the x-coordinate of the first point</param>
        /// <param name="y1">the y-coordinate of the first point</param>
        /// <param name="x2">the x-coordinate of the second point</param>
        /// <param name="y2">the y-coordinate of the second point</param>
        /// <returns>the Euclidean distance</returns>
        private double EuclideanDistance(int x1, int y1, int x2, int y2)
        {
            int dx = x1 - x2;
            int dy = y1 - y2;
            return Math.Sqrt(dx * dx + dy * dy);
        }

        /// <summary>
        /// Calculates the Manhattan (grid-based) distance between two points.
        /// </summary>
        /// <param name="x1">the x-coordinate of the first point</param>
        /// <param name="y1">the y-coordinate of the first point</param>
        /// <param name="x2">the x-coordinate of the second point</param>
        /// <param name="y2">the y-coordinate of the second point</param>
        /// <returns>the Manhattan distance</returns>
        private double ManhattanDistance(int x1, int y1, int x2, int y2)
        {
            return Math.Abs(x1 - x2) + Math.Abs(y1 - y2);
        }

        /// <summary>
        /// Calculates the Chebyshev (king's move) distance between two points.
        /// </summary>
        /// <param name="x1">the x-coordinate of the first point</param>
        /// <param name="y1">the y-coordinate of the first point</param>
        /// <param name="x2">the x-coordinate of the second point</param>
        /// <param name="y2">the y-coordinate of the second point</param>
        /// <returns>the Chebyshev distance</returns>
        private double ChebyshevDistance(int x1, int y1, int x2, int y2)
        {
            return Math.Max(Math.Abs(x1 - x2), Math.Abs(y1 - y2));
        }

        /// <summary>
        /// Returns a string representation of the generated Voronoi map.
        /// Each row is followed by a newline character.
        /// </summary>
        /// <returns>a string representation of the Voronoi diagram</returns>
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
