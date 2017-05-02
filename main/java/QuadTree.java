/**
 * Created by Poorvi on 4/13/2016.
 */

import java.util.Map;
import java.util.ArrayList;


public class QuadTree extends QTreeNode{

    QTreeNode root;

    public QuadTree(QTreeNode node) {
        root = node;
    }

    public QuadTree(QTreeNode node, int depth) {
        if (depth < 8) {
            buildQuadTree(node, depth);
        }
    }

    private void buildQuadTree(QTreeNode parent, int depth) {
        if (depth > 0) {
            int newDepth = depth - 1;

            parent.upperLeft = new QTreeNode(parent);
            setUpperLeft(parent.upperLeft);
            buildQuadTree(parent.upperLeft, newDepth);

            parent.upperRight = new QTreeNode(parent);
            setUpperRight(parent.upperRight);
            buildQuadTree(parent.upperRight, newDepth);

            parent.lowerLeft = new QTreeNode(parent);
            setLowerLeft(parent.lowerLeft);
            buildQuadTree(parent.lowerLeft, newDepth);

            parent.lowerRight = new QTreeNode(parent);
            setLowerRight(parent.lowerRight);
            buildQuadTree(parent.lowerRight, newDepth);
        }
    }

    private void setUpperLeft(QTreeNode node) {
        if (node.root != null) {
            QTreeNode parent = node.root;
            node.ullon = parent.ullon;
            node.ullat = parent.ullat;
            node.lrlon = 0.5 * (parent.ullon + parent.lrlon);
            node.lrlat = 0.5 * (parent.ullat + parent.lrlat);
            node.filename = parent.filename * 10 + 1;
        }
    }

    private void setUpperRight(QTreeNode node) {
        if (node.root != null) {
            QTreeNode parent = node.root;
            node.ullon = 0.5 * (parent.ullon + parent.lrlon);
            node.ullat = parent.ullat;
            node.lrlon = parent.lrlon;
            node.lrlat = 0.5 * (parent.ullat + parent.lrlat);
            node.filename = parent.filename * 10 + 2;
        }
    }

    private void setLowerLeft(QTreeNode node) {
        if (node.root != null) {
            QTreeNode parent = node.root;
            node.ullon = parent.ullon;
            node.ullat = 0.5 * (parent.ullat + parent.lrlat);
            node.lrlon = 0.5 * (parent.ullon + parent.lrlon);
            node.lrlat = parent.lrlat;
            node.filename = parent.filename * 10 + 3;
        }
    }

    private void setLowerRight(QTreeNode node) {
        if (node.root != null) {
            QTreeNode parent = node.root;
            node.ullon = 0.5 * (parent.ullon + parent.lrlon);
            node.ullat = 0.5 * (parent.ullat + parent.lrlat);
            node.lrlon = parent.lrlon;
            node.lrlat = parent.lrlat;
            node.filename = parent.filename * 10 + 4;
        }
    }

    public static void print(QTreeNode rt) {
        System.out.println(rt.upperLeft.lowerRight.upperRight.toString());
    }

    public ArrayList<QTreeNode> findNode(Map<String, Double> params) {
        ArrayList<QTreeNode> lst = new ArrayList<>();
        double ullon = params.get("raster_ul_lon");
        double ullat = params.get("raster_ul_lat");
        double lrlon = params.get("raster_lr_lon");
        double lrlat = params.get("raster_lr_lat");

        return lst;
    }

//    public QuadTree traverse (int depth. int lat, int lon) {
//        int i = depth;
//        while (i > 0) {
//            if (this.root.ullat == lat && this.root.ullon == lon) {
//
//            }
//        }
//    }
//

    public void levelOrder(QTreeNode q) {
        for (int i = 0; i < 8; i ++) {
            visitLevel(q, i);
        }
    }
    public void visitLevel(QTreeNode q, int depth) {
        System.out.println(q.filename);
        if (q == null)
        { return; }
        if (depth == 0)
        { System.out.println(q.filename); }
        else {
            visitLevel(q.root.upperLeft, depth - 1);
            visitLevel(q.root.upperRight, depth - 1);
            visitLevel(q.root.lowerRight, depth - 1);
            visitLevel(q.root.lowerLeft, depth - 1);
        }
    }

    public static void main(String[] args) {
        QTreeNode root = new QTreeNode();
        root.filename = 0;
        root.ullon = 0;
        root.ullat = 0;
        root.lrlon = 32;
        root.lrlat = 32;
        QuadTree t1 = new QuadTree(root, 7);
        //First level of nodes
        System.out.println(t1.upperLeft.filename);
        System.out.println(t1.upperRight.filename);
        System.out.println(t1.lowerLeft.filename);
        System.out.println(t1.lowerRight.filename);

        //Second level of nodes
        System.out.println(t1.upperLeft.upperLeft.filename);
        System.out.println(t1.upperLeft.upperRight.filename);
        System.out.println(t1.upperLeft.lowerLeft.filename);
        System.out.println(t1.upperLeft.lowerRight.filename);

        System.out.println(t1.upperRight.upperLeft.filename);
        System.out.println(t1.upperRight.upperRight.filename);
        System.out.println(t1.upperRight.lowerLeft.filename);
        System.out.println(t1.upperRight.lowerRight.filename);

        System.out.println(t1.lowerLeft.upperLeft.filename);
        System.out.println(t1.lowerLeft.upperRight.filename);
        System.out.println(t1.lowerLeft.lowerLeft.filename);
        System.out.println(t1.lowerLeft.lowerRight.filename);

        System.out.println(t1.lowerRight.upperLeft.filename);
        System.out.println(t1.lowerRight.upperRight.filename);
        System.out.println(t1.lowerRight.lowerLeft.filename);
        System.out.println(t1.lowerRight.lowerRight.filename);

        //print(root);
        t1.levelOrder(root.root);

    }


}
