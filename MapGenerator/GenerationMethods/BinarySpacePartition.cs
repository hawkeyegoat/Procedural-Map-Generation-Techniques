using System;
using System.Collections.Generic;
using System.Text;
using Assets;

namespace GenerationMethods
{
    /// <summary>
    /// Implements the Binary Space Partitioning (BSP) dungeon generation algorithm.
    /// 
    /// <para>The <see cref="BinarySpacePartition"/> class recursively divides the map into
    /// smaller subspaces, creating rooms within each final leaf node and connecting
    /// them with corridors. This approach is widely used in roguelike and procedural
    /// generation systems to produce varied and structured dungeon layouts.</para>
    /// 
    /// <h3>Algorithm Summary:</h3>
    /// <list type="number">
    ///     <item><description>Start with a solid map filled with walls.</description></item>
    ///     <item><description>Recursively divide the map into smaller regions using BSP.</description></item>
    ///     <item><description>Create a room in each leaf node (with optional gutter spacing).</description></item>
    ///     <item><description>Connect the rooms with simple L-shaped (dogleg) corridors.</description></item>
    /// </list>
    /// 
    /// <para>This implementation supports randomly biased splits based on region
    /// proportions to avoid overly uniform maps.</para>
    /// 
    /// <h3>Limitations:</h3>
    /// <list type="bullet">
    ///     <item><description>May produce clustered rooms if split thresholds are not tuned.</description></item>
    ///     <item><description>Corridors are always straight or L-shaped.</description></item>
    /// </list>
    /// 
    /// <para>Author: Logan Atkinson</para>
    /// </summary>
    public class BinarySpacePartition
    {
        /// <summary>
        /// Total width of the generated map.
        /// </summary>
        private readonly int width;
        
        /// <summary>
        /// Total height of the generated map.
        /// </summary>
        private readonly int height;
        
        /// <summary>
        /// 2D array representing the map grid.
        /// </summary>
        private readonly char[][] map;
        
        /// <summary>
        /// Random number generator used for splits and placements.
        /// </summary>
        private readonly Random rand = new Random();
        
        /// <summary>
        /// List of rooms created within the BSP tree.
        /// </summary>
        private readonly List<Room> rooms = new List<Room>();

        /// <summary>
        /// Constructs a new <see cref="BinarySpacePartition"/> generator.
        /// </summary>
        /// <param name="width">the total width of the map</param>
        /// <param name="height">the total height of the map</param>
        public BinarySpacePartition(int width, int height)
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
        /// Generates a dungeon map using Binary Space Partitioning.
        /// </summary>
        /// <returns>a 2D <see cref="char"/> array representing the generated map</returns>
        public char[][] GenerateMap()
        {
            // Fill with walls
            foreach (char[] row in map)
            {
                Array.Fill(row, '#');
            }

            Node root = new Node(0, 0, width, height);
            SplitSpace(root, 6); // 6 = minimum room size threshold
            CreateRooms(root);
            ConnectRooms(root);

            return map;
        }

        /// <summary>
        /// Recursively divides the given space into subregions until the minimum size is reached.
        /// </summary>
        /// <param name="node">the current node to split</param>
        /// <param name="minSize">the minimum width/height before stopping division</param>
        private void SplitSpace(Node node, int minSize)
        {
            if (node == null) return;

            // Stop splitting when node is too small
            if (node.Width <= minSize * 2 && node.Height <= minSize * 2) return;

            bool splitVertically = rand.Next(2) == 0;

            // Bias toward splitting along the larger axis
            if (node.Width > node.Height && node.Width / (float)node.Height >= 1.25f)
            {
                splitVertically = true;
            }
            else if (node.Height > node.Width && node.Height / (float)node.Width >= 1.25f)
            {
                splitVertically = false;
            }

            int max = (splitVertically ? node.Width : node.Height) - minSize;
            if (max <= minSize) return;

            int split = rand.Next(max - minSize + 1) + minSize;

            if (splitVertically)
            {
                if (split < node.Width - 1)
                {
                    node.Left = new Node(node.X, node.Y, split, node.Height);
                    node.Right = (new Node(node.X + split, node.Y, node.Width - split, node.Height));
                }
            }
            else
            {
                if (split < node.Height - 1)
                {
                    node.Left = new Node(node.X, node.Y, node.Width, split);
                    node.Right = (new Node(node.X, node.Y + split, node.Width, node.Height - split));
                }
            }

            if (node.Left != null) SplitSpace(node.Left, minSize);
            if (node.Right != null) SplitSpace(node.Right, minSize);
        }

        /// <summary>
        /// Recursively creates rooms within each leaf node of the BSP tree.
        /// </summary>
        /// <param name="node">the current node being processed</param>
        private void CreateRooms(Node node)
        {
            if (node.IsLeaf())
            {
                int gutter = 1;
                int roomWidth = rand.Next(node.Width - gutter * 2 - 3) + 3;
                int roomHeight = rand.Next(node.Height - gutter * 2 - 3) + 3;
                int roomX = node.X + gutter + rand.Next(Math.Max(1, node.Width - roomWidth - gutter * 2));
                int roomY = node.Y + gutter + rand.Next(Math.Max(1, node.Height - roomHeight - gutter * 2));

                Room room = new Room(roomX, roomY, roomWidth, roomHeight);
                CarveRoom(room);
                node.Room =(room);
                rooms.Add(room);
            }
            else
            {
                if (node.Left != null) CreateRooms(node.Left);
                if (node.Right != null) CreateRooms(node.Right);
            }
        }

        /// <summary>
        /// Converts a rectangular region of the map into open floor tiles.
        /// </summary>
        /// <param name="r">the room to carve</param>
        private void CarveRoom(Room r)
        {
            for (int i = r.X; i < r.X + r.Width; i++)
            {
                for (int j = r.Y; j < r.Y + r.Height; j++)
                {
                    if (i >= 0 && i < width && j >= 0 && j < height)
                    {
                        map[i][j] = '.';
                    }
                }
            }
        }

        /// <summary>
        /// Connects rooms in the BSP tree using L-shaped corridors.
        /// </summary>
        /// <param name="node">the current BSP node</param>
        private void ConnectRooms(Node node)
        {
            if (node.Left == null || node.Right == null) return;

            ConnectRooms(node.Left);
            ConnectRooms(node.Right);

            Room roomA = GetRoom(node.Left);
            Room roomB = GetRoom(node.Right);
            if (roomA != null && roomB != null)
            {
                CarveCorridor(roomA, roomB);
            }
        }

        /// <summary>
        /// Traverses the BSP tree to find a valid room within the given node.
        /// </summary>
        /// <param name="node">the node to search</param>
        /// <returns>the first non-null room found, or <c>null</c> if none exist</returns>
        private Room GetRoom(Node node)
        {
            if (node == null) return null;
            if (node.Room != null) return node.Room;
            Room leftRoom = GetRoom(node.Left);
            if (leftRoom != null) return leftRoom;
            return GetRoom(node.Right);
        }

        /// <summary>
        /// Creates an L-shaped corridor between two rooms.
        /// </summary>
        /// <param name="a">the first room</param>
        /// <param name="b">the second room</param>
        private void CarveCorridor(Room a, Room b)
        {
            int x1 = a.CenterX();
            int y1 = a.CenterY();
            int x2 = b.CenterX();
            int y2 = b.CenterY();

            if (rand.Next(2) == 0)
            {
                CarveHorizontalTunnel(x1, x2, y1);
                CarveVerticalTunnel(y1, y2, x2);
            }
            else
            {
                CarveVerticalTunnel(y1, y2, x1);
                CarveHorizontalTunnel(x1, x2, y2);
            }
        }

        /// <summary>
        /// Carves a horizontal tunnel between two x-coordinates at a given y position.
        /// </summary>
        private void CarveHorizontalTunnel(int x1, int x2, int y)
        {
            for (int x = Math.Min(x1, x2); x <= Math.Max(x1, x2); x++)
            {
                if (x > 0 && x < width && y > 0 && y < height) map[x][y] = '.';
            }
        }

        /// <summary>
        /// Carves a vertical tunnel between two y-coordinates at a given x position.
        /// </summary>
        private void CarveVerticalTunnel(int y1, int y2, int x)
        {
            for (int y = Math.Min(y1, y2); y <= Math.Max(y1, y2); y++)
            {
                if (x > 0 && x < width && y > 0 && y < height) map[x][y] = '.';
            }
        }

        /// <summary>
        /// Returns a string representation of the generated map, formatted for printing.
        /// </summary>
        /// <returns>a multi-line string representing the dungeon layout</returns>
        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < height; j++)
            {
                for (int i = 0; i < width; i++)
                {
                    sb.Append(map[i][j]);
                }
                sb.Append('\n');
            }
            return sb.ToString();
        }
    }
}
