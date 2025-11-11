package GenerationMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import assets.Room;

public class SimpleRoomPlacement {
    private int myWidth;
    private int myHeight;
    private char myMap[][];
    private final Random random = new Random();
    private final List<Room> rooms = new ArrayList<>();
    public SimpleRoomPlacement(final int theWidth, final int theHeight) {
        myWidth = theWidth;
        myHeight = theHeight;
        myMap = new char[theWidth][theHeight];
    }
    //Start with a solid map (all walls).
    public char[][] GenerateMap() {
        //set room conditions
        int roomCount = 0;
        int maxRooms = 10;
        int minSize = 4;
        int maxSize = 8;
        //Start with a solid map (all walls).
        Arrays.stream(myMap).forEach(a -> Arrays.fill(a, '#'));
        //Pick a random rectangle for a room.
        while (roomCount < maxRooms) {
            int w = random.nextInt(maxSize - minSize + 1) + minSize;
            int h = random.nextInt(maxSize - minSize + 1) + minSize;
            int x = random.nextInt(myWidth - w - 1) + 1;
            int y = random.nextInt(myHeight - h - 1) + 1;

            Room newRoom = new Room(x, y, w, h);
            //Check for overlap
            boolean overlap = false;

            for (Room other : rooms) {
                if (newRoom.intersects(other)) {
                    overlap = true;
                    break;
                }
            }
            //If there is no overlap, carve out the room
            if (!overlap) {
                carveRoom(newRoom);
                if (!rooms.isEmpty()) {
                    // Connect to previous room with a corridor
                    Room prev = rooms.get(rooms.size() - 1);
                    createDoglegCorridor(prev, newRoom);
                }
                rooms.add(newRoom);
                roomCount++;
            }
        }
        return myMap;
    }

    private void carveRoom(Room r) {
        for (int i = r.getX(); i < r.getX() + r.getWidth(); i++) {
            for (int j = r.getY(); j < r.getY() + r.getHeight(); j++) {
                myMap[i][j] = '.';
            }
        }
    }

    private void createDoglegCorridor(Room r1, Room r2) {
        int x1 = r1.centerX();
        int y1 = r1.centerY();
        int x2 = r2.centerX();
        int y2 = r2.centerY();

        if (random.nextBoolean()) {
            carveHorizontalTunnel(x1, x2, y1);
            carveVerticalTunnel(y1, y2, x2);
        } else {
            carveVerticalTunnel(y1, y2, x1);
            carveHorizontalTunnel(x1, x2, y2);
        }
    }

    private void carveHorizontalTunnel(int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (x > 0 && x < myWidth && y > 0 && y < myHeight)
                myMap[x][y] = '.';
        }
    }

    private void carveVerticalTunnel(int y1, int y2, int x) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (x > 0 && x < myWidth && y > 0 && y < myHeight)
                myMap[x][y] = '.';
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < myWidth; i++) {
            for (int j = 0; j < myHeight; j++) {
                sb.append(myMap[i][j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
