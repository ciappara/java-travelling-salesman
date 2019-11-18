package app.data;
import java.util.*;
import app.Helper;
import app.models.*;

public class CityPoints {

    private int minX = 0;
    private int minY = 0;
    private int maxX = 0;
    private int maxY = 0;

    private ArrayList<City> points;
    private ArrayList<City> bestEver;

    private int surfaceWidth;
    private int surfaceHeight;

    public CityPoints() {
        surfaceWidth = 800;
        surfaceHeight = 600;
    }

    ///////////////////////////////////////
    // imports city points from test files
    ///////////////////////////////////////
    public void importCityPoints(String filename) {

        // reset cities array
        points = new ArrayList<>();

        String[] lines = Helper.readFromFile(filename).split("\n");
        
        for(String line : lines) {
            String[] words = line.trim().replaceAll("\\s{2,}", " ").split(" ");

            int id = (int) Double.parseDouble(words[0]);
            int x = (int) Double.parseDouble(words[1]);
            int y = (int) Double.parseDouble(words[2]);

            if(x > )

            points.add(new City(x, y, id));
        }
    }

    ///////////////////////////////////////
    // generates random city points
    ///////////////////////////////////////
    public void generateCityPoints(int citiesQty) {

        // reset cities array
        points = new ArrayList<>();

        for(int i = 0; i < citiesQty; i++) {
            int x = Helper.random().nextInt(surfaceWidth);
            int y = Helper.random().nextInt(surfaceHeight);
            
            addCityPoints(x, y, i);
        }
    }

    ///////////////////////////////////////
    // set max/min points for visualization
    // and add xy coordinates as new city
    ///////////////////////////////////////
    public void addCityPoints(int x, int y, int id) {

        // set max/min points
        maxX = x > maxX ? x : maxX;
        maxY = y > maxY ? y : maxY;
        minY = x < minX ? x : minX;
        minY = y < minY ? y : minY;

        // add new city
        points.add(new City(x, y, id));
    }
    
}