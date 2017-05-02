import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;

/**
 * Created by Poorvi on 4/14/2016.
 */
public class QTreeNode {

    QTreeNode root;
    QTreeNode upperLeft;
    QTreeNode upperRight;
    QTreeNode lowerLeft;
    QTreeNode lowerRight;

    double ullon;
    double ullat;
    double lrlon;
    double lrlat;


    int filename;
    int depth;


    public QTreeNode() {//initialize the empty QTreeNode

    }

    public QTreeNode(QTreeNode parent) {
        this.root = parent;
        this.depth = parent.depth + 1;
    }

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
