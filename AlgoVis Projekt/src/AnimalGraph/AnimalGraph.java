package AnimalGraph;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import animal.main.Animation;
import animal.vhdl.logic.test;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.machineLearning.CoordinateSystem1D;

import java.awt.*;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

public class AnimalGraph {

    private Language lang;

    AnimalSubset testAnimalSet = new AnimalSubset();

    int v; // number of vertices (nodes)
    int e; // number of edges
    AnimalEdge[] edgeArray; // array to store all edges
    int startContract;
    int endContract;

    public AnimalGraph(int v, int e) {
        this.v = v;
        this.e = e;
        edgeArray = new AnimalEdge[e];
        for (int i = 0; i < e; i++) {
            edgeArray[i] = new AnimalEdge(); // for number of edges e create a new edge and store it in edgeArray
        }
    }

    // Kargers Min Cut Algorithm
    int kargersMinCut(AnimalGraph graph) {

        // get given Graph
        int nrOfVertices = graph.v;
        int nrOfEdges = graph.e;
        // here should be a single edge...
        Random r = new Random();

        // allocate memory for creating v subsets
        AnimalSubset[] subset = new AnimalSubset[nrOfVertices];

        // create v subsets of single elements
        for (int v = 0; v < nrOfVertices; ++v) {
            subset[v] = new AnimalSubset(0, 0);
            subset[v].parent = v;
            subset[v].rank = 0;
        }

        // initially there are nrOfVertices in given Graph
        int vertices = nrOfVertices;

        // graph is contracted until there are two vertices left
        while (vertices > 2) {
            // generates a random int between 0 and nrOfEdges
            int i = r.nextInt(nrOfEdges);

            // find vertices (sets) of current randomly picked edge
            int subset1 = testAnimalSet.find(subset, edgeArray[i].src);
            int subset2 = testAnimalSet.find(subset, edgeArray[i].dest);

            // if the vertices belong to the same subset, this edge is not considered
            if (subset1 == subset2) {
                continue;
            }
            // else contract the edges (combine the subsets and combine
            // the node of the edge into one)
            else {
                System.out.println("Contracting edge" + edgeArray[i].src + edgeArray[i].dest);
                startContract = edgeArray[i].src;
                endContract = edgeArray[i].dest;
                // number of Vertices is one less
                vertices--;
                testAnimalSet.union(subset, subset1, subset2);
            }
        }

        // there are now two subsets left in the contracted graph
        // so the results are the edges between the components
        int cutEdges = 0;

        for (int i = 0; i < nrOfEdges; i++) {
            int subset1 = testAnimalSet.find(subset, edgeArray[i].src);
            int subset2 = testAnimalSet.find(subset, edgeArray[i].dest);
            if (subset1 != subset2) {
                cutEdges++;
            }
        }
        return cutEdges;
    }

    public static void main(String[] args) {

        /*
        0A------1B
        |       |
        |       |
        |       |
        2C------3D
         */

        AnimalGraph testGraph = new AnimalGraph(4, 4);

        // add edge 0-1
        testGraph.edgeArray[0].src = 0;
        testGraph.edgeArray[0].dest = 1;

        // add edge 0-2
        testGraph.edgeArray[1].src = 0;
        testGraph.edgeArray[1].dest = 2;


        // add edge 1-2
        //testGraph.edgeArray[2].src = 1;
        //testGraph.edgeArray[2].dest = 2;

        // add edge 1-3
        testGraph.edgeArray[2].src = 1;
        testGraph.edgeArray[2].dest = 3;

        // add edge 2-3
        testGraph.edgeArray[3].src = 2;
        testGraph.edgeArray[3].dest = 3;

        System.out.println("Kargers minimum cut for given graph is:" + testGraph.kargersMinCut(testGraph));

        Language lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,"Kargers Minimum Cut", "Hannah Drews, Yves Geib", 680, 450);
        lang.setStepMode(true);

        TextProperties tp = new TextProperties();
        tp.set("color", Color.BLACK);
        CircleProperties cp = new CircleProperties();
        cp.set("color", Color.BLACK);
        PolylineProperties pp = new PolylineProperties();
        pp.set("color", Color.BLACK);

        // Create Node A
        Text nodeA = lang.newText(new Coordinates(100,100),"A", "nodeA", null, tp);
        //text.setText();
        Circle circleA = lang.newCircle(new Offset(0,0,nodeA, AnimalScript.DIRECTION_C),20,"circleA",null, cp);

        // Create Node B
        Text nodeB = lang.newText(new Coordinates(200,100),"B", "nodeB", null, tp);
        //text.setText();
        Circle circleB = lang.newCircle(new Offset(0,0,nodeB, AnimalScript.DIRECTION_C),20,"circleB",null, cp);

        // Create Node C
        Text nodeC = lang.newText(new Coordinates(100,200),"C", "nodeC", null, tp);
        //text.setText();
        Circle circleC = lang.newCircle(new Offset(0,0,nodeC, AnimalScript.DIRECTION_C),20,"circleC",null, cp);

        // Create Node D
        Text nodeD = lang.newText(new Coordinates(200,200),"D", "nodeD", null, tp);
        //text.setText();
        Circle circleD = lang.newCircle(new Offset(0,0, nodeD, AnimalScript.DIRECTION_C),20,"circleD",null, cp);

       /* Node startA = new Offset(20,0, circleA, AnimalScript.DIRECTION_C);
        Node startB = new Offset(-20,0, circleB, AnimalScript.DIRECTION_C);
        Node[] testarr = {startA, startB};*/


        // Edge between node A and B
        Polyline edgeAB = lang.newPolyline(new Node[] { new Offset(20,0, circleA, AnimalScript.DIRECTION_C), new Offset(-20,0, circleB, AnimalScript.DIRECTION_C)}, "EdgeAB", null, pp);
        Polyline edgeAC = lang.newPolyline(new Node[] { new Offset(0,20, circleA, AnimalScript.DIRECTION_C), new Offset(0,-20, circleC, AnimalScript.DIRECTION_C)}, "EdgeAC", null, pp);
        Polyline edgeBD = lang.newPolyline(new Node[] { new Offset(0,20, circleB, AnimalScript.DIRECTION_C), new Offset(0,-20, circleD, AnimalScript.DIRECTION_C)}, "EdgeBD", null, pp);
        Polyline edgeCD = lang.newPolyline(new Node[] { new Offset(20,0, circleC, AnimalScript.DIRECTION_C), new Offset(-20,0, circleD, AnimalScript.DIRECTION_C)}, "EdgeCD", null, pp);

        // if its 1 than the corresponding Node in Animal is B (Edge0-1)
        if (testGraph.endContract == 1) {
            if (testGraph.startContract == 0) {
                lang.nextStep();
                nodeA.changeColor("color", Color.RED,null, null);
                circleA.changeColor("color", Color.RED, null, null);
                nodeB.changeColor("color", Color.RED, null, null);
                circleB.changeColor("color", Color.RED, null, null);
                edgeAB.changeColor("color", Color.GREEN, null, null);

                lang.nextStep();
                nodeB.hide();
                circleB.hide();
                nodeA.setText("A,B", null,null);
                edgeAB.hide();
                edgeBD.hide();
                Polyline Edge1 = lang.newPolyline(new Node[] { new Offset( 20, 0, circleA, AnimalScript.DIRECTION_C), new Offset( 0, -20, circleD, AnimalScript.DIRECTION_C)}, "EdgeADAfterContracted", null);
            }

            /*if (testGraph.startContract == 3) {
                lang.nextStep();
                nodeD.changeColor("color", Color.RED,null, null);
                circleD.changeColor("color", Color.RED, null, null);
                nodeB.changeColor("color", Color.RED, null, null);
                circleB.changeColor("color", Color.RED, null, null);
                edgeBD.changeColor("color", Color.GREEN, null, null);

                lang.nextStep();
                nodeB.hide();
                circleB.hide();
                nodeD.setText("D,B", null,null);
                edgeAB.hide();
                edgeBD.hide();
                Polyline Edge2 = lang.newPolyline(new Node[] { new Offset( 20, 0, circleA, AnimalScript.DIRECTION_C), new Offset( 0, -20, circleD, AnimalScript.DIRECTION_C)}, "EdgeADAfterContracted", null);
            }
        }

        // if its 2 than the corresponding Node in Animal is C (Edge0-2)
        if (testGraph.endContract == 2) {
            if (testGraph.startContract == 0) {
                lang.nextStep();
                nodeA.changeColor("color", Color.RED,null, null);
                circleA.changeColor("color", Color.RED, null, null);
                nodeC.changeColor("color", Color.RED, null, null);
                circleC.changeColor("color", Color.RED, null, null);
                edgeAC.changeColor("color", Color.GREEN, null, null);


                lang.nextStep();
                nodeC.hide();
                circleC.hide();
                nodeA.setText("A,C", null,null);
                edgeAC.hide();
                edgeCD.hide();
                Polyline Edge3 = lang.newPolyline(new Node[] { new Offset( 0, 20, circleA, AnimalScript.DIRECTION_C), new Offset( 0, -20, circleD, AnimalScript.DIRECTION_C)}, "EdgeADAfterContracted", null);
            }
            if (testGraph.startContract == 3) {
                lang.nextStep();
                nodeC.changeColor("color", Color.RED,null, null);
                circleC.changeColor("color", Color.RED, null, null);
                nodeD.changeColor("color", Color.RED, null, null);
                circleD.changeColor("color", Color.RED, null, null);
                edgeCD.changeColor("color", Color.GREEN, null, null);


                lang.nextStep();
                nodeC.hide();
                circleC.hide();
                nodeD.setText("D,C", null,null);
                edgeAC.hide();
                edgeCD.hide();
                Polyline Edge4 = lang.newPolyline(new Node[] { new Offset( 0, 20, circleA, AnimalScript.DIRECTION_C), new Offset( 0, -20, circleD, AnimalScript.DIRECTION_C)}, "EdgeADAfterContracted", null);
            }*/
        }
        // Start animal to view visualization
        Animal.startAnimationFromAnimalScriptCode(lang.toString());
    }
}