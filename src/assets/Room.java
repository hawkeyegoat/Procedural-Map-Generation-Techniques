package assets;

public class Room {
        private int x, y, width, height;
        public Room(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        public boolean intersects(Room other) {
            return (x <= other.x + other.width && x + width >= other.x &&
                    y <= other.y + other.height && y + height >= other.y);
        }
        public int centerX() {
            return x + width / 2;
        }

        public int centerY() {
            return y + height / 2;
        }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
