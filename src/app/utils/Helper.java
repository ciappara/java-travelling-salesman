package app.utils;
import app.models.*;
import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;

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
    // swaps city values in array
    ////////////////////////////////
    public static void swap(ArrayList<City> list, int i, int j) {
        City temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    ////////////////////////////////
    // swaps int values in array
    ////////////////////////////////
    public static void swap(Integer[] list, int i, int j) {
        int temp = list[i];
        list[i] = list[j];
        list[j] = temp;
    }

    public static int indexOf(Integer[] list, int value) {
        for(int i = 0; i < list.length; i++){
            if(list[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static Integer[] clone(Integer[] list) {
        Integer[] cloned = new Integer[list.length];
        for(int i = 0; i < list.length; i++){
            cloned[i] = list[i];
        }
        return cloned;
    }

        // Function to insert x in arr at position pos 
        public static Integer[] splice(int n, Integer[] arr, int x, int pos) 
        { 
            int i; 

            // create a new array of size n+1 
            Integer[] newarr = new Integer[n]; 

            // insert the elements from 
            // the old array into the new array 
            // insert all elements till pos 
            // then insert x at pos 
            // then insert rest of the elements 
            for (i = 1; i < n; i++) { 
                if (i < pos - 1)
                    newarr[i] = arr[i];
                else if (i == pos - 1) 
                    newarr[i] = x; 
                else
                    newarr[i] = arr[i - 1]; 
            } 
            return newarr; 
        } 

    ////////////////////////////////
    // remaps value to an equivalent
    // from-to range
    ////////////////////////////////
    public static float remap (float value, float from1, float to1, float from2, float to2) {
        return (value - from1) / (to1 - from1) * (to2 - from2) + from2;
    }
    
    ////////////////////////////////
    // remaps value to an equivalent
    // from-to range
    ////////////////////////////////
    public static int remap (int value, int from1, int to1, int from2, int to2) {
        return (int) remap((float) value, (float) from1, (float) to1, (float) from2, (float) to2);
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
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            FileWriter fw = new FileWriter(getFilesPath() + "random-" + formatter.format(date) + ".txt");
            fw.write(text);
            fw.close();
        }
        catch(IOException ioex) {
            System.out.println(ioex.getMessage());
        }
    }
    
    // Helper class testing
    public static void main(String[] args) {

        Integer[] parent1Chromosome = new Integer[] { 1, 8, 2, 7, 3, 6, 4, 5, 0};
        Integer[] parent2Chromosome = new Integer[] { 2, 4, 6, 8, 0, 1, 3, 5, 7};


        System.out.println(Arrays.toString(parent1Chromosome));
        parent1Chromosome = splice(parent1Chromosome.length, parent1Chromosome, 0, 5 + 1);
        System.out.println(Arrays.toString(parent1Chromosome));
    }
}