package drast.views.gui.graph;

import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * NOTE from DrAST developers
 * We did not implement this class. We found this class on this page:
 * <p>
 * https://tips4java.wordpress.com/2008/10/13/screen-image/
 */

/*
 *  Convenience class to create and optionally save to a file a
 *  BufferedImage of an area on the screen. Generally there are
 *  four different scenarios. Create an image of:
 *
 *  a) an entire component
 *  b) a region of the component
 *  c) the entire desktop
 *  d) a region of the desktop
 *
 *  The first two use the Swing paint() method to draw the
 *  component image to the BufferedImage. The latter two use the
 *  AWT Robot to create the BufferedImage.
 *
 *	The created image can then be saved to a file by using the
 *  writeImage(...) method. The type of file must be supported by the
 *  ImageIO write method.
 *
 *  Although this class was originally designed to create an image of a
 *  component on the screen it can be used to create an image of components
 *  not displayed on a GUI. Behind the scenes the component will be given a
 *  size and the component will be laid out. The default size will be the
 *  preferred size of the component although you can invoke the setSize()
 *  method on the component before invoking a createImage(...) method. The
 *  default functionality should work in most cases. However the only
 *  foolproof way to get a image to is make sure the component has been
 *  added to a realized window with code something like the following:
 *
 *  JFrame frame = new JFrame();
 *  frame.setContentPane( someComponent );
 *  frame.pack();
 *  ScreenImage.createImage( someComponent );
 *
 */
public class ScreenImage {

  /*
   *  Create a BufferedImage for Swing components.
   *  The entire component will be captured to an image.
   *
   *  @param  component Swing component to create image from
   *  @return	image the image for the given region
  */
  public static BufferedImage createImage(JComponent component) {
    Dimension d = component.getSize();

    if (d.width == 0 || d.height == 0) {
      d = component.getPreferredSize();
      component.setSize(d);
    }

    Rectangle region = new Rectangle(0, 0, d.width, d.height);
    return ScreenImage.createImage(component, region);
  }

  /*
   *  Create a BufferedImage for Swing components.
   *  All or part of the component can be captured to an image.
   *
   *  @param  component Swing component to create image from
   *  @param  region The region of the component to be captured to an image
   *  @return	image the image for the given region
  */
  private static BufferedImage createImage(JComponent component, Rectangle region) {
    //  Make sure the component has a size and has been layed out.
    //  (necessary check for components not added to a realized frame)

    if (!component.isDisplayable()) {
      Dimension d = component.getSize();

      if (d.width == 0 || d.height == 0) {
        d = component.getPreferredSize();
        component.setSize(d);
      }

      layoutComponent(component);
    }

    BufferedImage image =
        new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();

    //  Paint a background for non-opaque components,
    //  otherwise the background will be black

    if (!component.isOpaque()) {
      g2d.setColor(component.getBackground());
      g2d.fillRect(region.x, region.y, region.width, region.height);
    }

    g2d.translate(-region.x, -region.y);
    component.paint(g2d);
    g2d.dispose();
    return image;
  }

  private static void layoutComponent(Component component) {
    synchronized (component.getTreeLock()) {
      component.doLayout();

      if (component instanceof Container) {
        for (Component child : ((Container) component).getComponents()) {
          layoutComponent(child);
        }
      }
    }
  }
}
