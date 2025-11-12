import GenerationMethods.*;
import assets.CreateFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) {
        try {
            PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(out);
        } catch (FileNotFoundException e) {
            CreateFile.newFile();
        }
        SimpleRoomPlacement SRP = new SimpleRoomPlacement(50, 50);
        SRP.GenerateMap();
        System.out.println("Simple room placement");
        System.out.println(SRP);

        BinarySpacePartition BSP = new BinarySpacePartition(50, 50);
        BSP.generateMap();
        System.out.println("Binary space partition");
        System.out.println(BSP);

        CellularAutomata CA = new CellularAutomata(50, 50);
        CA.generateMap(5);
        System.out.println("Cellular Automata");
        System.out.println(CA);

        DrunkardsWalk DW = new DrunkardsWalk(50, 50);
        DW.generateMap(0.33, 200);
        //DW.generateMap(0.10, 50);
        //DW.generateMap(0.75, 500);
        System.out.println("Drunkards Walk");
        System.out.println(DW);

        DiffusionLimitedAggregation DLA = new DiffusionLimitedAggregation(50, 50);
        DLA.generateMap(2, 8000);
        System.out.println("Diffusion Limited Aggregation");
        System.out.println(DLA);

        VoronoiDiagram VD = new VoronoiDiagram(50, 50);
        VD.generateMap(13, "manhattan");
        System.out.println("Voronoi Diagram");
        System.out.println(VD);

        PerlinAndSimplexNoise PSN = new PerlinAndSimplexNoise(50, 50);
        PSN.generateMap(4, 4.0, 0.5, 2.0);
        System.out.println("Perlin and Simplex noise");
        System.out.println(PSN);
    }

}