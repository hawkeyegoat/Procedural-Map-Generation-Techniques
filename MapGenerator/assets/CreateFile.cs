using System;
using System.IO;

namespace Assets
{
    public class CreateFile
    {
        // Function To Make New File
        public static void NewFile()
        {
            string strPath, strName;

            try
            {
                // Reading File name
                strName = Console.ReadLine();
                // Reading File Path
                strPath = Console.ReadLine();

                // Creating File Object and creating blank file
                string fullPath = Path.Combine(strPath, strName + ".txt");
                using (FileStream fs = File.Create(fullPath))
                {
                }
            }
            catch (Exception)
            {
                Console.Write("Failed to create a file.");
            }
        }
    }
}