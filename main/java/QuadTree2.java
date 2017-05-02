import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class QuadTree2  {
    QTreeNode2 root;


    QuadTree2 lowerLeft;
    QuadTree2 lowerRight;
    QuadTree2 upperRight;
    QuadTree2 upperLeft;

    double dpp;
    Rectangle2D.Double tile;

    public QuadTree2(double ullon, double ullat, double lrlon, double lrlat, File img, int depth, int id) {
//        // System.out.print(ullon + ", ");
//        // System.out.print(ullat + ", ");
//        // System.out.print(lrlon + ", ");
//        // System.out.print(lrlat + ", ");
//        // System.out.print(depth + ", ");
//        // System.out.print(id + ", ");
//        // System.out.println();
        root = new QTreeNode2(ullon, ullat, lrlon, lrlat, img, depth, id);
        String rootbegin = id + "1";
        int file = Integer.parseInt(rootbegin);
        String image1 = "img/" + file + ".png";
        String image2 = "img/" + (file + 1) + ".png";
        String image3 = "img/" + (file + 2) + ".png";
        String image4 = "img/" + (file + 3) + ".png";

//        if (id < 1000)
//            System.out.print(image1 + ", " + image2 + ", " + image3 + ", " + image4 );


        dpp = (lrlon - ullon)/256;

        double width = lrlon - ullon;
        double height = ullat - lrlat;

        tile = new Rectangle2D.Double(ullon, ullat, width, height);

//        double centrelat = lrlat + ((ullat - lrlat)/2);
//        double centrelon = ullon + ((lrlon - ullon)/2);
        double centrelat = 0.5*(ullat + lrlat);
        double centrelon = 0.5*(ullon + lrlon);


        if (depth < 7) {
            upperLeft = new QuadTree2(ullon, ullat, centrelon, centrelat, new File(image1), depth +1, Integer.parseInt(rootbegin));
            upperRight = new QuadTree2(centrelon, ullat, lrlon, centrelat, new File(image2), depth+1, Integer.parseInt(rootbegin) + 1);
            lowerLeft = new QuadTree2(ullon,centrelat, centrelon, lrlat, new File(image3), depth+1, Integer.parseInt(rootbegin) + 2);
            lowerRight = new QuadTree2(centrelon, centrelat, lrlon, lrlat, new File(image4), depth + 1, Integer.parseInt(rootbegin) + 3);
        }
    }

    public QuadTreeImage getImage(double ullon, double ullat, double lrlon, double lrlat, double queryDPP) {
        int cdepth = getCorrectDepth(ullon, ullat, lrlon, lrlat, queryDPP);
        // System.out.println(cdepth);
        ArrayList<QuadTree2> level = visitLevel(this, cdepth, new ArrayList<>());
//        for (QuadTree2 q: level) {
//            System.out.print(q.root.id + ", ");
//        }

        ArrayList<QuadTree2> images = new ArrayList<QuadTree2>();

        for (QuadTree2 q: level) {
            if (q.intersects(q, ullon, ullat, lrlon, lrlat)) {
                images.add(q);
            }
        }

        for (QuadTree2 q: images) {
            System.out.println(q.root.id);
        }

        Map<Double, ArrayList<QuadTree2>> imageMap = new HashMap<>();
        for (QuadTree2 q: images) {
            if (imageMap.containsKey(q.root.ullat)) {
                imageMap.get(q.root.ullat).add(q);
            }

            else {
                ArrayList<QuadTree2> toAdd = new ArrayList<>();
                toAdd.add(q);
                imageMap.put(q.root.ullat, toAdd);
            }
        }



        PriorityQueue<Double> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (double d: imageMap.keySet()) {
            pq.add(d);
        }

        // System.out.println("pq size: " + pq.size());
        double firstlat = images.get(0).root.ullat;
        double firstlon = images.get(0).root.ullon;
        int size = images.size() - 1;
        double lastlat = images.get(size).root.lrlat;
        double lastlon = images.get(size).root.lrlon;

        System.out.println(images.get(0).root.id);
        System.out.println(images.get(size).root.id);

        System.out.println("pq list size = " + imageMap.get(pq.peek()).size());
        System.out.println(" keyset size = " + imageMap.keySet().size());


        int imWidth = 256 * imageMap.get(pq.peek()).size();
        int imHeight = 256 * pq.size();

        BufferedImage image;
        BufferedImage result = new BufferedImage(imWidth, imHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = result.getGraphics();
        int x = 0;
        int y = 0;
        int pqsize = pq.size();
        for (int i = 0; i < pqsize; i++) {
            double lat = pq.remove();
            System.out.println("");
            for (QuadTree2 q: imageMap.get(lat)) {
                try {
                    File img = q.root.img;
                    System.out.println(img.getName() + ", ");
                    image = ImageIO.read(img);
                } catch (IOException e){
                    throw new RuntimeException("Can't find the image!");
                }
                g.drawImage(image, x, y, null);
                x += 256;
                // System.out.println(lat + " " + q.root.img);
            }
            x = 0;
            y += 256;
        }

        return new QuadTreeImage(firstlon, firstlat, lastlon, lastlat, cdepth, true, result, imHeight, imWidth);
     }

    public int getCorrectDepth(double ullon, double ullat, double lrlon, double lrlat, double queryDPP) {
        // System.out.println("Getting depth");
        QuadTree2 q = this;
        int correctdepth = 0;
        boolean found = false;
        while (q.upperLeft!=null && !found) {
            // System.out.println("id: " + q.root.id + " depth: " + q.root.depth);
             if (!q.isCorrectDepth(queryDPP)) {
                 q = q.upperLeft;
                 if (q.root.depth == 7) {
                     correctdepth = 7;
                     break;
                 }
             }
             else {
                 correctdepth = q.root.depth;
                 found = true;
             }
        }
        return correctdepth;
    }

    public boolean isCorrectDepth(double queryDPP) {
        return dpp<=queryDPP;
    }


//    public void levelOrder(QuadTree2 q, int i) {
//        visitLevel(q, i, new ArrayList<QuadTree2>());
//    }

    public ArrayList<QuadTree2> visitLevel(QuadTree2 q, int depth, ArrayList<QuadTree2> visited) {
        //// System.out.println(q.root.id);
        if (q == null)
        {return visited; }
        if (depth == 0)
        {   //System.out.println(q.root.id);
            visited.add(q);
             }
        else {
            visitLevel(q.upperLeft, depth - 1, visited);
            visitLevel(q.upperRight, depth - 1, visited);
            visitLevel(q.lowerLeft, depth - 1, visited);
            visitLevel(q.lowerRight, depth - 1, visited);
        }
        return visited;
    }


//    public boolean intersect(QuadTree2 queryBox) {
//        double width = queryBox.root.lrlon - queryBox.root.ullon;
//        double height = queryBox.root.ullat - queryBox.root.lrlat;
//        Rectangle2D.Double querybox = new Rectangle2D.Double(queryBox.root.ullon, queryBox.root.ullat, width, height);
//        return  tile.intersects(querybox) || tile.contains(querybox);
//    }

    //Takes in a QuadTree and querybox parameters
    public boolean intersects(QuadTree2 q1, double ullon, double ullat, double lrlon, double lrlat) {
        return !((ullon > q1.root.lrlon) || (lrlon < q1.root.ullon) || (ullat < q1.root.lrlat) || (lrlat > q1.root.ullat));
    }

//    public static void main(String[] args) {
//        QuadTree2 q1 = new QuadTree2(32, 32, 0, 0, new File("img/root.png"), 0, 0);
//////        // System.out.println(q1.upperLeft.id);
//////        // System.out.println(q1.upperRight.id);
//////        // System.out.println(q1.lowerLeft.id);
//////        // System.out.println(q1.lowerRight.id);
////
//////        q1.levelOrder(q1);
////        QuadTree2 q1png = new QuadTree2(32, 32, 0, 0, new File("img/1.png"), 0, 0);
////        QuadTree2 q111png = q1png.upperLeft.upperLeft;
////        boolean intersects = q1png.intersects(q111png, q111png.root.ullon,q111png.root.ullat, q111png.root.lrlon, q111png.root.lrlat);
////        // System.out.println(intersects);
//
//        QuadTreeImage test = q1.getImage(16, 16, 16, 16, 3);
//        // System.out.println(test.id);
//    }
}