namespace Assets
{
    /// <summary>
    /// Represents a rectangular partition or node used by generation methods,
    /// primarily for Binary Space Partitioning (BSP) map generation.
    ///
    /// Each <see cref="Node"/> defines a rectangular area of the map with coordinates
    /// and dimensions. Nodes can be recursively divided into left and right child
    /// nodes, allowing hierarchical spatial subdivision. Leaf nodes may contain
    /// <see cref="Room"/> objects representing playable rooms within the map.
    ///
    /// This class acts as a fundamental data structure for procedural generation
    /// algorithms that require recursive spatial partitioning, such as dungeon or
    /// maze generation.
    /// </summary>
    public class Node
    {
        /// <summary>
        /// The x position (horizontal coordinate) of the node's top-left corner.
        /// </summary>
        private int x;

        /// <summary>
        /// The y position (vertical coordinate) of the node's top-left corner.
        /// </summary>
        private int y;

        /// <summary>
        /// The total width of this node's area.
        /// </summary>
        private int width;

        /// <summary>
        /// The total height of this node's area.
        /// </summary>
        private int height;

        /// <summary>
        /// Reference to the left child node (first partition).
        /// </summary>
        private Node? left;

        /// <summary>
        /// Reference to the right child node (second partition).
        /// </summary>
        private Node? right;

        /// <summary>
        /// The <see cref="Room"/> contained within this node, if this node is a leaf.
        /// </summary>
        private Room? room;

        /// <summary>
        /// Constructs a new <see cref="Node"/> representing a rectangular area.
        /// </summary>
        /// <param name="x">The x position (horizontal coordinate) of the node</param>
        /// <param name="y">The y position (vertical coordinate) of the node</param>
        /// <param name="width">The width of the node</param>
        /// <param name="height">The height of the node</param>
        public Node(int x, int y, int width, int height)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        /// <summary>
        /// Returns the y position of this node.
        /// </summary>
        public int Y => y;

        /// <summary>
        /// Returns the width of this node.
        /// </summary>
        public int Width => width;

        /// <summary>
        /// Returns the x position of this node.
        /// </summary>
        public int X => x;

        /// <summary>
        /// Returns the height of this node.
        /// </summary>
        public int Height => height;

        /// <summary>
        /// Returns the left child node of this partition.
        /// </summary>
        public Node? Left
        {
            get => left;
            set => left = value;
        }

        /// <summary>
        /// Returns the right child node of this partition.
        /// </summary>
        public Node? Right
        {
            get => right;
            set => right = value;
        }

        /// <summary>
        /// Returns the <see cref="Room"/> contained within this node.
        /// </summary>
        public Room? Room
        {
            get => room;
            set => room = value;
        }

        /// <summary>
        /// Determines whether this node is a leaf node
        /// (i.e., has no child nodes).
        /// </summary>
        public bool IsLeaf()
        {
            return left == null && right == null;
        }
    }
}