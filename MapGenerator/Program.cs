using System;
using System.IO;
using GenerationMethods;
using Assets;

namespace MapGenerator
{
    /// <summary>
    /// Entry point for the map generation demo.
    /// Generates multiple procedural maps using various algorithms and
    /// outputs the results to a text file.
    /// </summary>
    public class Program
    {
        public static void Main(string[] args)
        {
            try
            {
                using (var outStream = new FileStream("output.txt", FileMode.Create, FileAccess.Write))
                using (var writer = new StreamWriter(outStream))
                {
                    Console.SetOut(writer);

                    // Simple Room Placement
                    var srp = new SimpleRoomPlacement(50, 50);
                    srp.GenerateMap();
                    Console.WriteLine("Simple room placement");
                    Console.WriteLine(srp);

                    // Binary Space Partition
                    var bsp = new BinarySpacePartition(50, 50);
                    bsp.GenerateMap();
                    Console.WriteLine("Binary space partition");
                    Console.WriteLine(bsp);

                    // Cellular Automata
                    var ca = new CellularAutomata(50, 50);
                    ca.GenerateMap(5);
                    Console.WriteLine("Cellular Automata");
                    Console.WriteLine(ca);

                    // Drunkard's Walk
                    var dw = new DrunkardsWalk(50, 50);
                    dw.GenerateMap(0.33, 200);
                    // dw.GenerateMap(0.10, 50);
                    // dw.GenerateMap(0.75, 500);
                    Console.WriteLine("Drunkards Walk");
                    Console.WriteLine(dw);

                    // Diffusion Limited Aggregation
                    var dla = new DiffusionLimitedAggregation(50, 50);
                    dla.GenerateMap(3, 3000);
                    Console.WriteLine("Diffusion Limited Aggregation");
                    Console.WriteLine(dla);

                    // Voronoi Diagram
                    var vd = new VoronoiDiagram(50, 50);
                    vd.GenerateMap(13, "manhattan");
                    Console.WriteLine("Voronoi Diagram");
                    Console.WriteLine(vd);

                    // Perlin and Simplex Noise
                    var psn = new PerlinAndSimplexNoise(50, 50);
                    psn.GenerateMap(4, 4.0, 0.5, 2.0);
                    Console.WriteLine("Perlin and Simplex noise");
                    Console.WriteLine(psn);
                }
            }
            catch (FileNotFoundException)
            {
                CreateFile.NewFile();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }
        }
    }
}
