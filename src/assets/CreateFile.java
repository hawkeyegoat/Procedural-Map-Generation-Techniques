package assets;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
public class CreateFile {
    // Function To Make New File
    public static void newFile()
    {
        String strPath , strName ;

        try {

            // Creating BufferedReadered object
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the file name : ");

            // Reading File name
            strName = br.readLine();
            System.out.print("Enter the file path : ");

            // Reading File Path
            strPath = br.readLine();

            // Creating File Object
            File file1 = new File(strPath + "\\" + strName + ".txt");

            // Method createNewFile() method creates blank file.
            file1.createNewFile();
        }

        catch (Exception ex1) {
            System.out.print("Failed to create a file.");
        }
    }
}