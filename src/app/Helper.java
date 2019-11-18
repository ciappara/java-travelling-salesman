package app;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;
import app.models.*;

public class Helper {

    private static Random rand;

    ////////////////////////////////
    // create singleton random var
    // to use throughout solution
    ////////////////////////////////
    public static Random random() {
        if(rand == null) {
            rand = new Random();
        }
        return rand;
    }
    
    ////////////////////////////////
    // swaps two values in an array
    ////////////////////////////////
    public static void swap(ArrayList<City> list, int i, int j) {
        City temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    ////////////////////////////////
    // remaps value to an equivalent
    // from-to range
    ////////////////////////////////
    public static float remap (float value, float from1, float to1, float from2, float to2) {
        return (value - from1) / (to1 - from1) * (to2 - from2) + from2;
    }

    ////////////////////////////////
    // gets 'files' directory path
    ////////////////////////////////
    public static String getFilesPath() {
        return new File("").getAbsolutePath() + "/src/files/";
    }

    ////////////////////////////////
    // check if file exists
    ////////////////////////////////
    public static Boolean fileExists(String filename) {
        if(filename == null || filename.isEmpty()) {
            return false;
        }
        return new File(getFilesPath() + filename).exists();
    }

    ////////////////////////////////
    // read from file
    ////////////////////////////////
    public static String readFromFile(String filename) {
        String reading = "";
        
        try {
            FileReader fr = new FileReader(getFilesPath() + filename);
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            
            while((line = br.readLine()) != null) {
                reading += line + "\n";  
            }

            fr.close();
        }
        catch(FileNotFoundException fnfex) {
            System.out.println("File not found!");
        }
        catch(IOException ioex) {
            System.out.println(ioex.getMessage());
        }

        return reading;
    }

    ////////////////////////////////
    // write to file
    ////////////////////////////////
    public static void writeToFile(String text) {
        try {
            FileWriter fw = new FileWriter(getFilesPath() + "write.txt");
    
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            System.out.println(formatter.format(date));
    
            fw.write(text + " - " + formatter.format(date));
            fw.close();
        }
        catch(IOException ioex) {
            System.out.println(ioex.getMessage());
        }
    }
}