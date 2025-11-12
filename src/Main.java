import GenerationMethods.BinarySpacePartition;
import GenerationMethods.SimpleRoomPlacement;

public class Main {
    public static void main(String[] args) {
        SimpleRoomPlacement SRP = new SimpleRoomPlacement(50, 50);
        SRP.GenerateMap();
        System.out.println(SRP);

        BinarySpacePartition BSP = new BinarySpacePartition(50, 50);
        BSP.generateMap();
        System.out.println(BSP);
    }
}