package jastaddadui;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.util.TestGraphs;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import jastaddad.Node;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.Stage;

public class JastAddAdUi extends Application {
    private int id;
    public JastAddAdUi( Node root) {
        launch(new String[0]);
    }

        private static final int CIRCLE_SIZE = 15; // default circle size

        @Override
        public void start (Stage stage){
            // setup up the scene.
            Group root = new Group();
            Scene scene = new Scene(root, 800, 400, Color.WHITE);

            // create two groups, one for each visualization
            Group viz1 = new Group();
            Group viz2 = new Group();

            // create a sample graph using JUNG's TestGraphs class.
            Graph graph1 = TestGraphs.getOneComponentGraph();

            // define the layout we want to use for the graph
            // The layout will be modified by the VisualizationModel
            Layout circleLayout = new CircleLayout(graph1);

        /*
         * Define the visualization model. This is how JUNG calculates the layout
         * for the graph. It updates the layout object passed in.
         */
            VisualizationModel vm1 = new DefaultVisualizationModel(circleLayout, new Dimension(400, 400));

            // draw the graph
            renderGraph(graph1, circleLayout, viz1);


            // Generate a second JUNG sample graph
            Graph graph2 = TestGraphs.getOneComponentGraph();

            // This time use an Isometric layout.
            Layout lay2 = new ISOMLayout(graph2);

            // Generate the actual layout
            VisualizationModel vm2 = new DefaultVisualizationModel(lay2, new Dimension(400, 400));

            // draw the graph
            renderGraph(graph2, lay2, viz2);

            // move the second viz to beside the first.
            viz2.translateXProperty().set(400);

            root.getChildren().add(viz1);
            root.getChildren().add(viz2);

            stage.setTitle("Displaying Two JUNG Graphs");
            stage.setScene(scene);
            stage.show();

        }

        /**
         * Render a graph to a particular Group
         * @param graph
         * @param layout
         * @param viz
         */
    private void renderGraph(Graph<String, Number> graph, Layout layout, Group viz) {
        // draw the vertices in the graph
        for (String v : graph.getVertices()) {
            // Get the position of the vertex
            Point2D p = (Point2D) layout.transform(v);

            // draw the vertex as a circle
            Circle circle = CircleBuilder.create()
                    .centerX(p.getX())
                    .centerY(p.getY())
                    .radius(CIRCLE_SIZE)
                    .build();

            // add it to the group, so it is shown on screen
            viz.getChildren().add(circle);
        }

        // draw the edges
        for (Number n : graph.getEdges()) {
            // get the end points of the edge
            Pair endpoints = graph.getEndpoints(n);

            // Get the end points as Point2D objects so we can use them in the
            // builder
            Point2D pStart = (Point2D) layout.transform(endpoints.getFirst());
            Point2D pEnd = (Point2D) layout.transform(endpoints.getSecond());

            // Draw the line
            Line line = LineBuilder.create()
                    .startX(pStart.getX())
                    .startY(pStart.getY())
                    .endX(pEnd.getX())
                    .endY(pEnd.getY())
                    .build();
            // add the edges to the screen
            viz.getChildren().add(line);
        }
    }
}