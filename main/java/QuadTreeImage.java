import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Poorvi on 4/17/2016.
 */
public class QuadTreeImage {
    BufferedImage image;
    double ullon;
    double ullat;
    double lrlon;
    double lrlat;
    int depth;
    boolean query = true;
    int height;
    int width;

    public QuadTreeImage (double ullon, double ullat, double lrlon, double lrlat, int depth, boolean query, BufferedImage image, int height, int width) {
        this.image = image;
        this.ullon = ullon;
        this.ullat = ullat;
        this.lrlon = lrlon;
        this.lrlat = lrlat;
        this.depth = depth;
        this.query = true;
        this.image = image;
        this.height = height;
        this.width = width;
    }
}
