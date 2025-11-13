using System;
using System.Text;
using Assets;

namespace GenerationMethods
{
    /// <summary>
    /// The <c>PerlinAndSimplexNoise</c> class provides a simple implementation
    /// of Perlin noise for 2D procedural generation.
    /// <para>
    /// This generator can produce smooth, continuous noise maps suitable for terrain
    /// heightmaps, texture synthesis, or any other application requiring natural-looking
    /// random variation.
    /// </para>
    /// </summary>
    /// <remarks>
    /// <para><b>Algorithm Summary:</b></para>
    /// <list type="number">
    ///     <item>
    ///         <description>Generate a random permutation table for gradient lookup.</description>
    ///     </item>
    ///     <item>
    ///         <description>Compute Perlin noise at each point in the 2D grid, using multiple octaves
    ///         for increased detail and complexity.</description>
    ///     </item>
    ///     <item>
    ///         <description>Normalize the final noise values to the range [0, 1].</description>
    ///     </item>
    /// </list>
    /// <para>
    /// This implementation supports multiple configurable parameters:
    /// </para>
    /// <list type="bullet">
    ///     <item>
    ///         <description><b>Octaves:</b> number of layers of detail (higher values increase complexity).</description>
    ///     </item>
    ///     <item>
    ///         <description><b>Frequency:</b> initial scale of noise variation.</description>
    ///     </item>
    ///     <item>
    ///         <description><b>Gain:</b> amplitude multiplier between octaves.</description>
    ///     </item>
    ///     <item>
    ///         <description><b>Lacunarity:</b> frequency multiplier between octaves.</description>
    ///     </item>
    /// </list>
    /// <para><b>Note:</b> This implementation currently uses a Perlin-style gradient algorithm
    /// and does not include Simplex optimization, despite the class name.</para>
    /// </remarks>
    public class PerlinAndSimplexNoise
    {
        private readonly int width;
        private readonly int height;
        private readonly double[][] noiseMap;
        private readonly Random rand = new Random();

        /// <summary>
        /// Constructs a <c>PerlinAndSimplexNoise</c> generator for a map of the given size.
        /// </summary>
        /// <param name="width">the width of the map in cells</param>
        /// <param name="height">the height of the map in cells</param>
        public PerlinAndSimplexNoise(int width, int height)
        {
            this.width = width;
            this.height = height;
            this.noiseMap = new double[width][];
            for (int i = 0; i < width; i++)
            {
                this.noiseMap[i] = new double[height];
            }
        }

        /// <summary>
        /// Generates a Perlin noise map using the provided parameters.
        /// </summary>
        /// <param name="octaves">the number of noise layers to combine</param>
        /// <param name="frequency">the initial frequency of the noise</param>
        /// <param name="gain">the amplitude multiplier applied after each octave</param>
        /// <param name="lacunarity">the frequency multiplier applied after each octave</param>
        /// <returns>a 2D array of normalized noise values ranging from 0 to 1</returns>
        public double[][] GenerateMap(int octaves, double frequency, double gain, double lacunarity)
        {
            // Step 1: Generate random permutation for gradients
            int[] permutation = new int[512];
            int[] p = new int[256];
            for (int i = 0; i < 256; i++) p[i] = i;
            for (int i = 0; i < 256; i++)
            {
                int j = rand.Next(256);
                int tmp = p[i];
                p[i] = p[j];
                p[j] = tmp;
            }
            for (int i = 0; i < 512; i++) permutation[i] = p[i & 255];

            // Step 2: Generate Perlin noise for each tile
            double min = double.MaxValue, max = double.MinValue;

            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    double amplitude = 1.0;
                    double frequencyAcc = frequency;
                    double noiseValue = 0.0;

                    for (int o = 0; o < octaves; o++)
                    {
                        double sampleX = x * frequencyAcc / width;
                        double sampleY = y * frequencyAcc / height;
                        double n = Perlin(sampleX, sampleY, permutation);
                        noiseValue += n * amplitude;

                        amplitude *= gain;
                        frequencyAcc *= lacunarity;
                    }

                    noiseMap[x][y] = noiseValue;
                    if (noiseValue < min) min = noiseValue;
                    if (noiseValue > max) max = noiseValue;
                }
            }

            // Step 3: Normalize to [0,1]
            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    noiseMap[x][y] = (noiseMap[x][y] - min) / (max - min);
                }
            }

            return noiseMap;
        }

        /// <summary>
        /// Computes 2D Perlin noise for a given coordinate using the provided permutation table.
        /// </summary>
        /// <param name="x">the x-coordinate in noise space</param>
        /// <param name="y">the y-coordinate in noise space</param>
        /// <param name="permutation">the gradient lookup permutation table</param>
        /// <returns>the noise value at the specified coordinate (typically in range [-1, 1])</returns>
        private double Perlin(double x, double y, int[] permutation)
        {
            int xi = (int)Math.Floor(x) & 255;
            int yi = (int)Math.Floor(y) & 255;

            double xf = x - Math.Floor(x);
            double yf = y - Math.Floor(y);

            double u = Fade(xf);
            double v = Fade(yf);

            int aa = permutation[permutation[xi] + yi];
            int ab = permutation[permutation[xi] + yi + 1];
            int ba = permutation[permutation[xi + 1] + yi];
            int bb = permutation[permutation[xi + 1] + yi + 1];

            double x1, x2;
            x1 = Lerp(Grad(aa, xf, yf), Grad(ba, xf - 1, yf), u);
            x2 = Lerp(Grad(ab, xf, yf - 1), Grad(bb, xf - 1, yf - 1), u);
            return Lerp(x1, x2, v);
        }

        /// <summary>
        /// Smooths the interpolation curve for Perlin noise using the function 6t⁵ - 15t⁴ + 10t³.
        /// </summary>
        /// <param name="t">the interpolation factor</param>
        /// <returns>the smoothed interpolation value</returns>
        private double Fade(double t)
        {
            return t * t * t * (t * (t * 6 - 15) + 10);
        }

        /// <summary>
        /// Performs linear interpolation between two values.
        /// </summary>
        /// <param name="a">the start value</param>
        /// <param name="b">the end value</param>
        /// <param name="t">the interpolation factor (0–1)</param>
        /// <returns>the interpolated value</returns>
        private double Lerp(double a, double b, double t)
        {
            return a + t * (b - a);
        }

        /// <summary>
        /// Calculates a gradient vector based on the hash value and the input coordinates.
        /// </summary>
        /// <param name="hash">the hashed gradient index</param>
        /// <param name="x">the x-coordinate distance</param>
        /// <param name="y">the y-coordinate distance</param>
        /// <returns>the dot product of the gradient and the distance vector</returns>
        private double Grad(int hash, double x, double y)
        {
            switch (hash & 3)
            {
                case 0: return x + y;
                case 1: return -x + y;
                case 2: return x - y;
                case 3: return -x - y;
                default: return 0; // unreachable
            }
        }

        /// <summary>
        /// Returns a visual representation of the generated noise map.
        /// <para>
        /// Each cell is represented by an ASCII character based on its height value:
        /// </para>
        /// </summary>
        /// <remarks>
        /// <list type="bullet">
        ///     <item>
        ///         <description><c>'~'</c> → water (low elevation)</description>
        ///     </item>
        ///     <item>
        ///         <description><c>'.'</c> → plains</description>
        ///     </item>
        ///     <item>
        ///         <description><c>'^'</c> → hills</description>
        ///     </item>
        ///     <item>
        ///         <description><c>'A'</c> → peaks (high elevation)</description>
        ///     </item>
        /// </list>
        /// </remarks>
        /// <returns>a string-formatted map visualization</returns>
        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    double n = noiseMap[x][y];
                    char c;
                    if (n < 0.3) c = '~';      // water
                    else if (n < 0.45) c = '.';
                    else if (n < 0.7) c = '^'; // hills
                    else c = 'A';              // peaks
                    sb.Append(c);
                }
                sb.Append('\n');
            }
            return sb.ToString();
        }
    }
}
