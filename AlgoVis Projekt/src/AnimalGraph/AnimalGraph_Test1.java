package AnimalGraph;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.*;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

public class AnimalGraph_Test1 implements Generator {

    private Language lang;
    private Text header;
    Text name1, name2;
    Rect rect;
    TextProperties introProp;
    SourceCodeProperties srcProp;

    AnimalSubset testAnimalSet = new AnimalSubset();

    int v; // number of vertices (nodes)
    int e; // number of edges
    AnimalEdge[] edgeArray; // array to store all edges
    int startContract;
    int endContract;


    public AnimalGraph_Test1(int v, int e) {
        this.v = v;
        this.e = e;
        edgeArray = new AnimalEdge[e];
        for (int i = 0; i < e; i++) {
            edgeArray[i] = new AnimalEdge(); // for number of edges e create a new edge and store it in edgeArray
        }
    }


    /*
    ###########################################
    ############### MAIN METHOD ###############
    ###########################################
    */
    public static void main(String[] args) {

        //Generator generator = new AnimalGraph();
        //Animal.startGeneratorWindow(generator);
        //Language lang = new AnimalScript("Karger's Minimal Cut", "Hannah Drews, Yves Geib", 640, 480);
        //Animal.startAnimationFromAnimalScriptCode(lang.toString());

        int x = 4;
        int y = 4;
        AnimalGraph_Test1 testGraph = new AnimalGraph_Test1(x, y);
        for(int i = 0; i < x-1; i++) {
            for (int j = 0; j < x-1; j++) {
                testGraph.edgeArray[i].src = j;
                testGraph.edgeArray[i].dest = i + 1;
            }
        }


        System.out.println("Kargers minimum cut for given graph is:" + testGraph.kargersMinCut(testGraph));

        /*
        0A------1B
        |       |
        |       |
        |       |
        2C------3D

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
        //lang.setStepMode(true);

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
        Node[] testarr = {startA, startB};



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
            }
        }
        // Start animal to view visualization
        Animal.startAnimationFromAnimalScriptCode(lang.toString());
        */
    }

    @Override
    public String generate(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) {
        this.lang.setStepMode(true);
        this.lang.setInteractionType(1024);
        TextProperties var3 = (TextProperties) animationPropertiesContainer.getPropertiesByName("headerProp");
        RectProperties var4 = (RectProperties) animationPropertiesContainer.getPropertiesByName("headerGraphProp");
        this.srcProp = (SourceCodeProperties) animationPropertiesContainer.getPropertiesByName("sourceCodeProp");

        var3.set("font", new Font( "SansSerif", 1, 24));
        this.header = this.lang.newText(new Coordinates(20, 50), this.getName(), "header", null, var3);
        this.rect = this.lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5, 5, "header", "SE"), "hRect", null, var4);
        this.lang.nextStep("Introduction");
        this.introProp = new TextProperties();
        this.introProp.set("font", new Font("SansSerif", 0, 16));        Text var5 = this.lang.newText(new Offset(0, 100, this.header, "SW"), "Der Vergleich von Elementen fordert Komplexität. So fordert ein vergleichender Sortieralgorithmus minimal eine Komplexität von n * log(n), meist ist sie aber quadratisch.", "Introduction1", (DisplayOptions)null, this.introProp);
        Text var6 = this.lang.newText(new Offset(0, 10, var5, "SW"), "Daher wäre ein Sortieralgorithmus optimal, der Elemente gar nicht erst vergleicht, sondern Vorwissen verwendet, um eine Liste zu sortieren.", "Introduction2", (DisplayOptions)null, this.introProp);
        Text var7 = this.lang.newText(new Offset(0, 10, var6, "SW"), "Flashsort ist solch ein Algorithmus. Als Vorwissen wird das Wissen über die Verteilung der Daten genutzt.", "Introduction3", (DisplayOptions)null, this.introProp);
        Text var8 = this.lang.newText(new Offset(0, 10, var7, "SW"), "Am besten funktioniert das bei gleichverteilten Daten. Das heißt, alle Daten kommen etwa gleich oft vor und es gibt keine Häufungen.", "Introduction5", (DisplayOptions)null, this.introProp);
        Text var9 = this.lang.newText(new Offset(0, 10, var8, "SW"), "Stell dir vor, es soll eine Liste sortiert werden, die 10 Elemente von 0 bis 20 enthält.", "Introduction6", (DisplayOptions)null, this.introProp);
        Text var10 = this.lang.newText(new Offset(0, 10, var9, "SW"), "Bekommst du jetzt eine 10 gezeigt, ohne die restlichen Elemente zu kennen, wirst du sie logischerweise mittig einsortieren.", "Introduction7", (DisplayOptions)null, this.introProp);
        Text var11 = this.lang.newText(new Offset(0, 10, var10, "SW"), "Das ist auch die Grundidee von Flash Sort. Die Liste wird in Bereiche eingeteilt, hier Klassen genannt. Im ersten Bereich sind die n kleinsten Elemente. Im letzten die n größten und so weiter.", "Introduction8", (DisplayOptions)null, this.introProp);
        Text var12 = this.lang.newText(new Offset(0, 10, var11, "SW"), "Durch eine Variable m kann die Klassenanzahl festgelegt werden. Je mehr Klassen es gibt, desto kleiner sind diese und der Algorithmus kann genauer vorsortieren. Je größer sie sind, desto schneller arbeitet Flash Sort.", "Introduction9", (DisplayOptions)null, this.introProp);
        Text var13 = this.lang.newText(new Offset(0, 10, var12, "SW"), "Wo eine Klasse anfängt und endet, merkt sich Flash Sort durch ein Hilfsarray. Darin liegen die Indizes zum letzten freien Platz einer Klasse. Jedes Mal, wenn ein Element hineingelegt wird, wird dieser Index verkleinert.", "Introduction12", (DisplayOptions)null, this.introProp);
        Text var14 = this.lang.newText(new Offset(0, 10, var13, "SW"), "Eine Klasse ist voll, wenn der Zeiger auf das Klassenende der vorherigen Klasse zeigt. Für jedes Element wird die Klasse berechnet. Damit lässt sich der Algorithmus auch auf andere Verteilungen anpassen,", "Introduction13", (DisplayOptions)null, this.introProp);
        Text var15 = this.lang.newText(new Offset(0, 10, var14, "SW"), "etwa auf eine Normalverteilung. Hier wird die Formel passend zu einer Gleichverteilung verwendet.", "Introduction13teil2", (DisplayOptions)null, this.introProp);
        Text var16 = this.lang.newText(new Offset(0, 10, var15, "SW"), "Nachdem festgestellt wurde, in welche Klasse ein Element gehört, wird es einfach an die nächste freie Stelle in dieser gelegt. Das bedeutet, dass die Elemente innerhalb einer Klasse nicht sortiert sind.", "Introduction10", (DisplayOptions)null, this.introProp);
        Text var17 = this.lang.newText(new Offset(0, 10, var16, "SW"), "Dafür läuft ganz zum Schluss ein Insertion Sort Algorithhmus über die Liste und sortiert die Elemente innerhalb der Klasse.", "Introduction11", (DisplayOptions)null, this.introProp);
        Text var18 = this.lang.newText(new Offset(0, 10, var17, "SW"), "Insertion Sort ist schneller, je besser die Liste schon vorsortiert ist. Bei einer perfekt sortierten Liste entsteht beispielsweise eine lineare Komplexität.", "Introduction12", (DisplayOptions)null, this.introProp);
        Text var19 = this.lang.newText(new Offset(0, 10, var18, "SW"), "Aber bei einer komplett unsortierten Liste ist Insertion Sort mit einer quadratischen Komplexität recht langsam. Deswegen ergänzen sich die beiden Algorithmen so gut.", "Introduction13", (DisplayOptions)null, this.introProp);
        this.lang.nextStep();



        this.createGraph();
        this.lang.finalizeGeneration();

        return this.lang.toString();

    }

    @Override
    public String getAlgorithmName() {
        return "Karger's Minimal cut (getAlgorithmName)";
    }

    @Override
    public String getAnimationAuthor() {
        return "Hannah Drews, Yves Geib";
    }

    @Override
    public String getCodeExample() {
        return "getCodeExample";
    }

    @Override
    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    @Override
    public String getDescription() {
        return "Karger's Minimal Cut ist Algorithmus mit einer Beschreibung.";
    }

    @Override
    public String getFileExtension() {
        return "asu";
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(8);
    }

    @Override
    public String getName() {
        return "Karger's Minimal Cut (getName)";
    }

    @Override
    public String getOutputLanguage() {
        return "Java";
    }

    @Override
    public void init() {
        this.lang = new AnimalScript("Karger's Minimal Cut", "Hannah Drews, Yves Geib", 640, 480);
        this.lang.setStepMode(true);
    }

    public void createGraph() {

        TextProperties tp = new TextProperties();
        //tp.set("color", Color.BLACK);
        CircleProperties cp = new CircleProperties();
        //cp.set("color", Color.BLACK);
        PolylineProperties pp = new PolylineProperties();
        //pp.set("color", Color.BLACK);

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

        // Edge between node A and B
        Polyline edgeAB = lang.newPolyline(new Node[] { new Offset(20,0, circleA, AnimalScript.DIRECTION_C), new Offset(-20,0, circleB, AnimalScript.DIRECTION_C)}, "EdgeAB", null, pp);
        Polyline edgeAC = lang.newPolyline(new Node[] { new Offset(0,20, circleA, AnimalScript.DIRECTION_C), new Offset(0,-20, circleC, AnimalScript.DIRECTION_C)}, "EdgeAC", null, pp);
        Polyline edgeBD = lang.newPolyline(new Node[] { new Offset(0,20, circleB, AnimalScript.DIRECTION_C), new Offset(0,-20, circleD, AnimalScript.DIRECTION_C)}, "EdgeBD", null, pp);
        Polyline edgeCD = lang.newPolyline(new Node[] { new Offset(20,0, circleC, AnimalScript.DIRECTION_C), new Offset(-20,0, circleD, AnimalScript.DIRECTION_C)}, "EdgeCD", null, pp);


        /*Node startA = new Offset(20,0, circleA, AnimalScript.DIRECTION_C);
        Node startB = new Offset(-20,0, circleB, AnimalScript.DIRECTION_C);
        Node[] testarr = {startA, startB};
        */


    }

    public int kargersMinCut(AnimalGraph_Test1 graph) {

        // get given Graph
        int nrOfVertices = graph.v;
        int nrOfEdges = graph.e;
        // here should be a single edge...
        Random r = new Random();

        // allocate memory for creating v subsets
        AnimalSubset[] subset = new AnimalSubset[nrOfVertices];

        // create v subsets of single elements
        for (int v = 0; v < nrOfVertices; v++) {
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

}