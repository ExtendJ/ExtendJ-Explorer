package jastaddad.tasks;

import jastaddad.api.JastAddAdAPI;

import java.awt.image.BufferedImage;

/**
 * Created by gda10jth on 11/20/15.
 */
public class JastAddAdImage extends DecoratorTask {

    public JastAddAdImage(Object root){
        super(root);
    }

    public JastAddAdImage(JastAddAdAPI api){
        super(api);
    }

    @Override
    protected void runThisTask() {
        /*api
        // Create the buffered image
        BufferedImage image = (BufferedImage) vis.getImage(
                new Point2D.Double(vv.getGraphLayout().getSize().getWidth() / 2,
                        vv.getGraphLayout().getSize().getHeight() / 2),
                new Dimension(vv.getGraphLayout().getSize()));

        // Write image to a png file
        File outputfile = new File("graph.png");

        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            // Exception handling
        }*/
    }
}
