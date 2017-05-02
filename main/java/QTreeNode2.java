import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Comparator;

/**
 * Created by Poorvi on 4/14/2016.
 */
public class QTreeNode2 {

    double ullon;
    double ullat;
    double lrlon;
    double lrlat;


    File img;
    int depth;
    int id;


    public QTreeNode2() {//initialize the empty QTreeNode
    }

    public QTreeNode2(double ullon, double ullat, double lrlon, double lrlat, File img, int depth, int id) {
        this.ullon = ullon;
        this.ullat = ullat;
        this.lrlon = lrlon;
        this.lrlat = lrlat;
        this.img = img;
        this.depth = depth;
        this.id = id;
    }

//    public QTreeNode(QTreeNode parent) {
//        this.root = parent;
//        this.depth = parent.depth + 1;
//    }

    public double dpp() {
//        return ((lrlon - ullon) * 288200) / 256;
        return ((lrlon - ullon)/256);
    }

    double width = this.lrlon - this.ullon;
    double height = this.ullat - this.lrlat;

    Rectangle2D.Double box = new Rectangle2D.Double(this.ullon, this.ullat, width, height);

//    public boolean intersecting(double x3, double x4, double y3, double y4) {
//        if (x3 > this.ullon || y3 > this.ullat || this.lrlon > x4 || this.lrlat > y4) {
//            return false;
//        }
//        return true;
//    }

    public boolean intersecting(double x3, double x4, double y3, double y4) {
        width = x3 - x4;
        height = y4 - y3;
        Rectangle2D.Double querybox = new Rectangle2D.Double(x4, y4, width, height);
        return box.intersects(querybox) || box.contains(querybox);
    }

    public boolean satisfiesDepthOrIsLeaf(double queryDPP) {
        return this.dpp() <= queryDPP;
    }


}
