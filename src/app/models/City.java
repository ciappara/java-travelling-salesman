package app.models;
import java.awt.geom.Point2D;

public class City {

    public int x;
    public int y;
    public int id;

    public City(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    // calculate distance between this city and the param city provided
    public double calculateDistance(City nextCity) {
        return Point2D.distance(this.x, this.y, nextCity.x, nextCity.y);
    }
}