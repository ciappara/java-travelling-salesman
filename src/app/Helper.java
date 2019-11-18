package app;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

class Helper {

    static Random rand;

    // create a singleton static random var to use throught the solution
    public static Random Rand() {
        if(rand == null) {
            rand = new Random();
        }
        return rand;
    }
    
    // swaps two values in an array
    public static void Swap(ArrayList<City> list, int i, int j) {
        City temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // remaps a value to equivalent from-to range
    public static float Remap (float value, float from1, float to1, float from2, float to2) {
        return (value - from1) / (to1 - from1) * (to2 - from2) + from2;
    }

    // gets the current path
    public static String getCurrentPath() {
        return new File("").getAbsolutePath();
    }

    public static void readFromFile()
    {
        try
        {
            FileReader fr = new FileReader(getCurrentPath() + "/src/files/read.txt");
            
            int i;
            while((i = fr.read()) != -1) {
                System.out.println((char)i);
            }
            fr.close();
        }
        catch(FileNotFoundException fnfex)
        {
            System.out.println("File not found!");
        }
        catch(IOException ioex)
        {
            System.out.println(ioex.getMessage());
        }
    }

    public static void writeToFile(String text)
    {
        try
        {
            FileWriter fw = new FileWriter(getCurrentPath() + "/src/files/write.txt");
    
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            System.out.println(formatter.format(date));
    
            fw.write(text + " - " + formatter.format(date));
            fw.close();
        }
        catch(IOException ioex)
        {
            System.out.println(ioex.getMessage());
        }
    }
}