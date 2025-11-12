package assets;

public class Node {
    int x, y, width, height;
    Node left, right;
    Room room;

    public Node(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getX() {
        return x;
    }
    public int getHeight() {
        return height;
    }
    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}
