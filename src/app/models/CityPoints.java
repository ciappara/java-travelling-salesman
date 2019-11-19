package app.models;
import java.util.*;
import app.TravelChromosome;
import app.utils.Helper;

public class CityPoints {

    public int minX = 0;
    public int minY = 0;
    public int maxX = 0;
    public int maxY = 0;

    public ArrayList<City> points;
    public ArrayList<City> bestEver;

    private int surfaceWidth = 0;
    private int surfaceHeight = 0;
    private int randomPointsQty = 0;

    // notes: sort by x and y


    public CityPoints() {
        this(800, 600, 20);
    }

    public CityPoints(int surfaceWidth, int surfaceHeight, int randomPointsQty) {
        this.surfaceWidth = surfaceWidth;
        this.surfaceHeight = surfaceHeight;
        this.randomPointsQty = randomPointsQty;
    }

    ///////////////////////////////////////
    // import or generate city points
    ///////////////////////////////////////
    public void getCityPoints(String filename) {

        if(Helper.fileExists(filename)) {
            importCityPoints(filename);
        }
        else {
            generateCityPoints(randomPointsQty);
        }
    }

    ///////////////////////////////////////
    // imports city points from test files
    ///////////////////////////////////////
    private void importCityPoints(String filename) {

        // reset cities array
        points = new ArrayList<>();

        String[] lines = Helper.readFromFile(filename).split("\n");
        
        for(String line : lines) {
            String[] words = line.trim().replaceAll("\\s{2,}", " ").split(" ");

            int id = (int) Double.parseDouble(words[0]);
            int x = (int) Double.parseDouble(words[1]);
            int y = (int) Double.parseDouble(words[2]);

            addCityPoints(x, y, id);
        }
    }

    ///////////////////////////////////////
    // generates random city points
    ///////////////////////////////////////
    private void generateCityPoints(int citiesQty) {

        // reset cities array
        points = new ArrayList<>();

        for(int i = 0; i < citiesQty; i++) {
            int x = Helper.random().nextInt(surfaceWidth);
            int y = Helper.random().nextInt(surfaceHeight);
            
            addCityPoints(x, y, i);
        }


        StringBuilder sb = new StringBuilder();
        for (City city : points) {
            sb.append(city.id);
            sb.append(" ");
            sb.append(city.x);
            sb.append(" ");
            sb.append(city.y);
            sb.append(System.getProperty("line.separator"));
            //sb.append("\n");
        }
        Helper.writeToFile(sb.toString());
    }

    ///////////////////////////////////////
    // set max/min points for visualization
    // and add xy coordinates as new city
    ///////////////////////////////////////
    private void addCityPoints(int x, int y, int id) {

        // set max/min points
        maxX = x > maxX ? x : maxX;
        maxY = y > maxY ? y : maxY;
        minY = x < minX ? x : minX;
        minY = y < minY ? y : minY;

        // add new city
        points.add(new City(x, y, id));
    }

    ///////////////////////////////////////
    // order cities according to the path
    // in the chromosome
    ///////////////////////////////////////
    public ArrayList<City> getPathFromChromosome(TravelChromosome chromosome) {
        
        ArrayList<City> citiesPath = new ArrayList<City>();

        for(int gene : chromosome.getOrderChromosome()) {
            City city = points.get(gene);
            citiesPath.add(new City(city.x, city.y, city.id));
        }

        return citiesPath;
    }
}