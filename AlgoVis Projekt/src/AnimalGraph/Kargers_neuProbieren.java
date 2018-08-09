/*
 * Kargers_neuProbieren.java
 * Hannah Drews, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package AnimalGraph;

import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.*;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import java.util.Random;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class Kargers_neuProbieren implements Generator {
    private Language lang;
    private TextProperties textProps;
    private CircleProperties circleProps;
    private PolylineProperties polylineProps;
    private SourceCodeProperties sourceCodeProps;
    private Graph graph;

    Node[] nodeList;
    private int coordinateX, coordinateY; //Coordinates of the user-input graph to translate them into our graph

    AnimalSubset testAnimalSet = new AnimalSubset();

    //int v; // number of vertices (nodes)
    //int e; // number of edges ACHTUNG wurde für Beispiel geändert
    private AnimalEdge[] edgeArray; // array to store all edges
    protected int cutEdges;
    protected int startContract;
    protected int endContract;

    public Kargers_neuProbieren() {

    }

    public void init(){
        this.lang = new AnimalScript("Karger's Minimal Cut", "Hannah Drews", 800, 600);
        this.lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {


        this.lang.setInteractionType(1024);
        //this.createGraph();

        this.textProps = (TextProperties)props.getPropertiesByName("textProps");
        this.circleProps = (CircleProperties)props.getPropertiesByName("circleProps");
        this.polylineProps = (PolylineProperties)props.getPropertiesByName("polylineProps");
        this.sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        this.graph = (Graph)primitives.get("graph");

        this.intro();
        this.kargersMinCut();
        this.lang.finalizeGeneration();
        return this.lang.toString();
    }

    private void intro() {
        TextProperties var1 = new TextProperties();
        var1.set("font", new Font("SansSerif", 1, 24));
        Text var2 = this.lang.newText(new Coordinates(200, 50), "Kargers", "header", (DisplayOptions)null, var1);
        RectProperties var3 = new RectProperties();
        var3.set("fillColor", Color.YELLOW);
        var3.set("filled", true);
        var3.set("depth", 2);
        this.lang.newRect(new Offset(-5, -5, var2, "NW"), new Offset(5, 5, var2, "SE"), "hrect", (DisplayOptions)null, var3);
        SourceCodeProperties var4 = new SourceCodeProperties();
        var4.set("font", new Font("SansSerif", 0, 18));
        var4.set("color", Color.BLACK);
        SourceCode var5 = this.lang.newSourceCode(new Offset(-150, 30, var2, "SW"), "intro", (DisplayOptions)null, var4);
        var5.addCodeLine("Der Kargers_neuProbieren Minimal Cut Algorithmus dient dazu, innerhalb eines Graphen von einem gegebenen", (String)null, 0, (Timing)null);
        var5.addCodeLine("Knoten aus die kuerzeste Route zu allen anderen Knoten zu finden. Der Graph kann", (String)null, 0, (Timing)null);
        var5.addCodeLine("hierbei sowohl gerichtet als auch ungerichet sein. Als Entfernung zwischen zwei", (String)null, 0, (Timing)null);
        var5.addCodeLine("Knoten wird in der Regel das Gewicht der auf der Route liegenden Kanten gewählt.", (String)null, 0, (Timing)null);
        var5.addCodeLine("Bei einem ungewichteten Graphen wird als Entfernung die Anzahl der dazwischen", (String)null, 0, (Timing)null);
        var5.addCodeLine("liegenden Knoten genommen. Es darf jedoch niemals ein negatives Gewicht existieren", (String)null, 0, (Timing)null);
        var5.addCodeLine("", (String)null, 0, (Timing)null);
        var5.addCodeLine("Der Algorithmus arbeitet indem er sich zu jeden Knoten merkt wie weit er vom", (String)null, 0, (Timing)null);
        var5.addCodeLine("Start entfernt ist, wer sein Vorgänger auf der Route ist und ob er bereits", (String)null, 0, (Timing)null);
        var5.addCodeLine("bearbeitet wurde. Wird ein Knoten bearbeitet so wird er als besucht markiert,", (String)null, 0, (Timing)null);
        var5.addCodeLine("für seine Nachbarn wird berechnet wie weit sie entfernt sind sollte die Route", (String)null, 0, (Timing)null);
        var5.addCodeLine("über den aktuell bearbeiteten Knoten gehen. Ist die neu berechnete Entfernung ", (String)null, 0, (Timing)null);
        var5.addCodeLine("kürzer als die bereits gemerkte, so wird diese als Entfernung für den Nachbarn", (String)null, 0, (Timing)null);
        var5.addCodeLine("eingetragen. Als Vorgänger dieses Nachbarns wird der bearbeitete Knoten eingetragen", (String)null, 0, (Timing)null);
        var5.addCodeLine("und der Nachbar wird als unbesucht markiert.", (String)null, 0, (Timing)null);
        var5.addCodeLine("Anfangs gild jeder Knoten als unbesucht ihr Vorgänger ist unbekannt und ihre Entfernung", (String)null, 0, (Timing)null);
        var5.addCodeLine("ist unendlich. Für den Startknoten wird dann die Entfernung auf 0 gesetzt.", (String)null, 0, (Timing)null);
        this.lang.nextStep("Intro");
        var5.hide();


    }
    /*
        //Testmatrix mit 6 Nodes
        int[][] testMatrix = {
                {0, 1, 1, 0, 0, 0},
                {1, 0, 0, 1, 0, 0},
                {1, 0, 0, 1, 1, 1},
                {0, 1, 1, 0, 1, 0},
                {0, 0, 1, 1, 0, 1},
                {0, 0, 1, 0, 1, 0}
        };
     */


    public int kargersMinCut() {
        //AnimalGraph1 testGraph = new AnimalGraph1(4, 4);
        //this.lang = new AnimalScript("Karger's Minimal Cut", "Hannah Drews", 800, 600);
        //this.lang.setStepMode(true);
        //this.lang.setInteractionType(1024);
        //Kargers_neuProbieren testGraph = graph; //Just for the sake of the correct variable.

        int i; //for for-loop
        int j; //for for-loop

        /*
        int[][] testMatrix = {
                {0, 1, 1, 0},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {0, 1, 1, 0},
        };
        */



        int[][] inputMatrix = this.graph.getAdjacencyMatrix();
        int[][] testMatrix = new int[inputMatrix.length][inputMatrix.length];
        int[][] transponierteMatrix = new int[testMatrix.length][testMatrix.length];
        int matrixCounter = 0;

        //Printe testMatrix, die vom User in Animal übergeben wurde
        //TODO bisher noch XML Datei, noch nicht dynamisch
        System.out.println("Übergebene inputMatrix: Länge = " + testMatrix.length);
        for(i = 0; i < inputMatrix.length; i++) {
            for (j = 0; j < inputMatrix.length; j++) {
                if(inputMatrix[i][j] == 1)
                    matrixCounter++;
                System.out.print(inputMatrix[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("Anzahl Einsen in der Matrix = Anzahl Edges = " + matrixCounter);


        //Transponiere testMatrix
        for(i = 0; i < inputMatrix.length; i++) {
            for (j = 0; j < inputMatrix.length; j++) {
                transponierteMatrix[j][i] = inputMatrix[i][j];
            }
        }
        //Printe transponierte Matrix
        System.out.println("\nTransponierte Matrix: ");
        for(i = 0; i < transponierteMatrix.length; i++) {
            for (j = 0; j < transponierteMatrix.length; j++) {
                System.out.print(transponierteMatrix[i][j] + " ");
            }
            System.out.println("");
        }

        //Füge transponierte Matrix in testMatrix ein
        for(i = 0; i < testMatrix.length; i++) {
            for (j = 0; j < testMatrix.length; j++) {
                testMatrix[i][j] = inputMatrix[i][j] + transponierteMatrix[i][j];
            }
        }

        //Gib die gesamte Matrix aus
        System.out.println("\ntestMatrix PLUS transponierte Matrix: ");
        for(i = 0; i < testMatrix.length; i++) {
            for (j = 0; j < testMatrix.length; j++) {
                System.out.print(testMatrix[i][j] + " ");
            }
            System.out.println("");
        }



        TextProperties tp = new TextProperties();
        tp.set("color", Color.BLACK);
        CircleProperties cp = new CircleProperties();
        cp.set("color", Color.BLACK);
        PolylineProperties pp = new PolylineProperties();
        pp.set("color", Color.BLACK);

        //Erstelle Platz für Primitives
        Text[] textArray = new Text[inputMatrix.length];
        Circle[] circleArray = new Circle[inputMatrix.length];

        //Erstelle Polyline Matrix, um Polylines an korrekten Stellen zu zeichnen
        Polyline[][] polyMatrix = new Polyline[inputMatrix.length][inputMatrix.length];

        /*Create Graph TODO dynamically from Animal User Input
          0A 1B
          2C 3D
        */
        this.nodeList = this.graph.getNodes();
        int z = 0;
        for (Node n : nodeList) {

            this.coordinateX = ((Coordinates) n).getX();
            this.coordinateY = ((Coordinates) n).getY();

            //create Text of node
            textArray[z] = lang.newText(new Coordinates(coordinateX, coordinateY), this.graph.getNodeLabel(n), "node " + this.graph.getNodeLabel(n), null, tp);

            //create Circle around Text
            circleArray[z] = lang.newCircle(new Offset(0, 0, textArray[z], AnimalScript.DIRECTION_C), 30, "circle " + this.graph.getNodeLabel(n), null, cp);
            z++;
        }

        System.out.print("\nNodes verbunden mit Edges:\n");
        //polyMatrix wird an allen Stellen gezeichnet und direkt mit hide an den Stellen versehen, an denen in testMatrix[i][j] eine 0 steht.
        for (i = 0; i < testMatrix.length; i++) {
            for (j = 0; j < testMatrix.length; j++) {
                if (inputMatrix[i][j] == 1) {
                    // wir können verhindern, dass wir nicht mehr Wissen welche Edge zu welchen Nodes gehört, indem wir einfach das Array mit Null füllen wo keine Edges sind dann ist Anzahl Plätze in der Matrix = Länge des Arrays
                    polyMatrix[i][j] = lang.newPolyline(new Node[]{new Offset(0, 0, textArray[i], AnimalScript.DIRECTION_C), new Offset(0, 0, textArray[j], AnimalScript.DIRECTION_C)}, "Edge " + textArray[i] + "-" + textArray[j] + "after being contracted", null);
                    System.out.println(textArray[i].getText() + " " + textArray[j].getText());
                    //Da nur eine obere Dreiecksmatrix übergeben wird, wird hier nur die Diagonale versteckt. Alle anderen Felder, die doppelt vorkommen, sind ja eh null.
                }
            }
        }


        lang.nextStep("Drawing Nodes & Polylines");

        System.out.println("\nGraphSize: " + this.graph.getSize());

        // get given Graph
        int inputMatrixLength = this.graph.getAdjacencyMatrix().length;
        int nrOfVertices = this.graph.getSize();
        int nrOfEdges = matrixCounter;

        //fill edgeArray with given Graph
        edgeArray = new AnimalEdge[nrOfEdges];
        int y = 0; //edgeArray Counter
        // Filling the edgeArray with the Matrix Input to let Kargers_neuProbieren Execute with the Input Graph
        for (i = 0; i < inputMatrixLength; i++) {
            for (j = 0; j < inputMatrixLength; j++) {

                if (inputMatrix[i][j] == 1) {
                    edgeArray[y] = new AnimalEdge(i, j);
                    y++;
                }
            }
        }

        // here should be a single edge...
        Random r = new Random();

        // allocate memory for creating i subsets
        AnimalSubset[] subset = new AnimalSubset[nrOfVertices];

        // create i subsets of single elements
        for (i = 0; i < nrOfVertices; i++) {
            subset[i] = new AnimalSubset(0, 0);
            subset[i].parent = i;
            subset[i].rank = 0;
        }

        // initially there are nrOfVertices in given Graph
        int vertices = nrOfVertices;

        // graph is contracted until there are two vertices left
        while (vertices > 2) {
            int count = vertices;
            System.out.println("Countdown: " + count);
            count--;
            // generates a random int between 0 and nrOfEdges
            int x = r.nextInt(nrOfEdges);

            // find vertices (sets) of current randomly picked edge
            int subset1 = testAnimalSet.find(subset, edgeArray[x].src);
            int subset2 = testAnimalSet.find(subset, edgeArray[x].dest);

            // if the vertices belong to the same subset, this edge is not considered
            if (subset1 == subset2) {
                continue;
            }
            // else contract the edges (combine the subsets and combine
            // the node of the edge into one)
            else {
                System.out.println("\nContracting edge" + edgeArray[x].src + edgeArray[x].dest);
                startContract = subset[edgeArray[x].src].parent;
                endContract = subset[edgeArray[x].dest].parent;


                System.out.println("length of inputMatrix: " + inputMatrix.length);
                System.out.println("length of testMatrix: " + testMatrix.length);
                System.out.println("startContract: " + startContract + " endContract: " + endContract);
                System.out.println("node that gets deleted after cutting the edge: " + textArray[endContract].getName());

                //Highlighten der Polyline, die entfernt werden soll.
                //TODO Die Farbe muss noch dynamisch übergeben werden.
                if (polyMatrix[startContract][endContract] != null)
                    polyMatrix[startContract][endContract].changeColor("color", Color.RED, null, null);

                lang.nextStep("Highlighten Polyline");

                // highlighten der beiden Nodes, die zusammengeführt werden, allerdings aktuell nur für den zweiten Cut
                // TODO
                // dynamisch machen, damit alle Cuts gehighlightet werden (Das geschieht dann mit Verschieben des Codes in die whileSchleife der kargersMinCut Methode
                textArray[startContract].changeColor("color", Color.RED, null, null);
                circleArray[startContract].changeColor("color", Color.RED, null, null);
                textArray[endContract].changeColor("color", Color.RED, null, null);
                circleArray[endContract].changeColor("color", Color.RED, null, null);

                lang.nextStep("Highlighten Node");

                //Hide den betroffenen Node
                textArray[endContract].hide();
                circleArray[endContract].hide();

                //Ausgelagert: Benennung des neuen Nodes (Code-Refactoring)
                String nodename = textArray[startContract].getText() + " " + textArray[endContract].getText();
                System.out.println("cut: " + nodename);

                //Färbe den contracteten Node wieder schwarz und füge den entfernten Node hinzu (Also Node A wird zu Node A B mit .setText)
                textArray[startContract].setText(nodename, null, null);
                textArray[startContract].changeColor("color", Color.BLACK, null, null);
                circleArray[startContract].changeColor("color", Color.BLACK, null, null);

                pp.set("color", Color.GREEN);

                //Hide alle Polylines, die an endContract dranhängen (hier der Wert j in der polyMatrix)
                for (j = 0; j < polyMatrix.length; j++) {
                    int[] rememberedNodes = new int[polyMatrix.length]; //Erstelle int-Array für das Speichern aller Nodes, die an dem contracteten Node dranhängen. Damit können nachher die neuen Lines gezeichnet werden.


                    //Befülle rememberedNodes mit den Nodes, die an dem contracteten Node dranhängen, damit später die Polylines zu diesen Nodes gezeichnet werden können.
                    if (startContract != j) { //Schaue nur die Nodes an, die an dem contracteten Node dranhängen. Alle anderen werden ignoriert.

                        for (i = 0; i < testMatrix.length; i++) {
                            if (testMatrix[j][i] == testMatrix[startContract][endContract] && polyMatrix[startContract][endContract] != null) {
                                polyMatrix[startContract][endContract].hide();
                            }
                        }

                        if (polyMatrix[startContract][endContract] != null) {
                            polyMatrix[startContract][endContract].hide();
                            polyMatrix[startContract][endContract] = null;
                        }
                        //Hier geschieht das Hiden, oberes Dreieck der Matrix
                        if (polyMatrix[endContract][j] != null) {

                            rememberedNodes[j] = j; //Merken der Knoten, zu denen die Lines gezeichnet werden.
                            polyMatrix[endContract][j].hide();
                            polyMatrix[endContract][j] = null;
                            polyMatrix[startContract][rememberedNodes[j]] = lang.newPolyline(new Node[]{new Offset(5, 5, circleArray[startContract], AnimalScript.DIRECTION_C),
                                    new Offset(5, 5, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C)}, "newLine", null, pp);

                            //polyMatrix[startContract][rememberedNodes[j]].show();

                            System.out.println(Arrays.toString(rememberedNodes));
                            System.out.println("Wert j a: " + j);

                        }
                        //Unteres Dreieck der Matrix
                        if (polyMatrix[j][endContract] != null) {
                            rememberedNodes[j] = j; //Merken der Knoten, zu denen die Lines gezeichnet werden.
                            polyMatrix[j][endContract].hide();
                            polyMatrix[j][endContract] = null;
                            polyMatrix[rememberedNodes[j]][startContract] = lang.newPolyline(new Node[]{new Offset(5, 5, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C),
                                    new Offset(5, 5, circleArray[startContract], AnimalScript.DIRECTION_C)}, "newLine", null, pp);

                            //polyMatrix[rememberedNodes[j]][startContract].show();

                            System.out.println(Arrays.toString(rememberedNodes));
                            System.out.println("Wert j b:" + j);

                        }
                        //testmatrix soll die Werte auf null setzen, deren Knoten entfernt wurden, bzw. auch die Edges.
                        //testMatrix[endContract][j] = 0;
                        //testMatrix[j][endContract] = 0;
                        //testMatrix[endContract][startContract] = 0;
                        //testMatrix[startContract][endContract] = 0;

                        /*
                        testmatrix soll die Werte auf eins setzen, deren Lines neu gezeichnet wurden.
                        Wenn A-B zusammengeführt wird, muss die neue Line von A nach D gezeichnet werden.
                        Dies ist hier hinterlegt.
                         */
                        //testMatrix[startContract][rememberedNodes[j]] = 1;
                        //testMatrix[rememberedNodes[j]][startContract] = 1;

                       /*
                        if(testMatrix[startContract][rememberedNodes[j]] == 1 && newLine[j] != null)
                            newLine[j].changeColor("color", Color.RED, null, null);
                        */
                    }
                }

                System.out.println("Volle Matrix mit neu gezeichneteten Edges: ");
                //Neuzeichnen aller neuen Edges
                for(j = 0; j < polyMatrix.length; j++) {

                    for(i = 0; i < polyMatrix.length; i++) {
                        if(polyMatrix[j][i] != null)
                            System.out.print("1 ");
                        else
                            System.out.print("0 ");
                    }
                    System.out.println();
                }

                //Wenn noch eine Linie vom vorherigen Neuzeichnen existiert, wird diese Linie versteckt
                for (i = 0; i < polyMatrix.length; i++) {
                    for (j = 0; j < polyMatrix.length; j++) {
                        //if (testMatrix[i][j] == 1 && polyMatrix[i][j] != null || testMatrix[j][i] == 1 && polyMatrix[j][i] != null)
                        //    polyMatrix[i][j].hide();
                        if (polyMatrix[endContract][j] != null) {
                            polyMatrix[endContract][j].hide();
                            polyMatrix[endContract][j] = null;
                            System.out.println("Hier komme ich noch hin1");
                        }
                        if (polyMatrix[startContract][endContract] != null) {
                            polyMatrix[startContract][endContract].hide();
                            polyMatrix[startContract][endContract] = null;
                            System.out.println("Hier komme ich noch hin2");
                        }
                        if (polyMatrix[j][endContract] != null) {
                            polyMatrix[j][endContract].hide();
                            polyMatrix[j][endContract] = null;
                            System.out.println("Hier komme ich noch hin3");
                        }
                    }
                }


                lang.nextStep("Hiden_& Neuzeichnen und den Algorithmus einen Schritt weiterführen");

                System.out.println("Contracting subsets" + subset[edgeArray[x].src].parent + subset[edgeArray[x].dest].parent + "\n");

                //lang.nextStep();
                // number of Vertices is one less
                vertices--;
                testAnimalSet.union(subset, subset1, subset2);
            }
        }
        // there are now two subsets left in the contracted graph
        // so the results are the edges between the components
        cutEdges = 0;

        for (i = 0; i < nrOfEdges; i++) {
            int subset1 = testAnimalSet.find(subset, edgeArray[i].src);
            int subset2 = testAnimalSet.find(subset, edgeArray[i].dest);
            if (subset1 != subset2) {
                cutEdges++;
            }
        }
        System.out.println("FINAL: cutedges = " + cutEdges);
        //Animal.startAnimationFromAnimalScriptCode(lang.toString());
        return cutEdges;
    }


    public String getName() {
        return "Karger's Minimal Cut";
    }

    public String getAlgorithmName() {
        return "Karger's Minimal Cut (von Karger)";
    }

    public String getAnimationAuthor() {
        return "Hannah Drews";
    }

    public String getDescription(){
        return "Kargers_neuProbieren Min Cut cuttet edges in einem Graphpen.";
    }

    public String getCodeExample(){
        return "1) Initialize Graph"
 +"\n"
 +"2) While there are more than 2 vertices,"
 +"\n"
 +"	a) Pick a random edge (u, v) in the contracted graph"
 +"\n"
 +"	b) Merge (or contract) u and v into a single vertex"
 +"\n"
 +"	c) Remove self-loops"
 +"\n"
 +"3) Return cut represented by 2 vertices";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }


    public class AnimalEdge {
        int src; // source or start point of an edge
        int dest; // destination or end point of an edge

        public AnimalEdge(int src, int dest) {
            this.src = src;
            this.dest = dest;
        }
    }

    public class AnimalSubset {
        int parent;
        int rank;

        public AnimalSubset() {
        }

        // constructor of subset
        public AnimalSubset(int parent, int rank) {
            this.parent = parent;
            this.rank = rank;
        }

        // find the subset of an element i
        // return only the root node of the subset in which the node is in
        int find(AnimalSubset subsetArray[], int i) {
            if (subsetArray[i].parent != i) {
                subsetArray[i].parent = find(subsetArray, subsetArray[i].parent);
            }
            return subsetArray[i].parent;
        }

        // union of two subsets (union by rank means, that the
        // "heavier" one becomes the parent of the other)
        void union(AnimalSubset subsetArray[], int x, int y) {
            int rootOfX = find(subsetArray, x);
            int rootOfY = find(subsetArray, y);

            //do union by rank
            if (subsetArray[rootOfX].rank < subsetArray[rootOfY].rank) {
                subsetArray[rootOfX].parent = rootOfY;
            }
            else if (subsetArray[rootOfX].rank > subsetArray[rootOfX].rank) {
                subsetArray[rootOfY].parent = rootOfX;
            }

            // if ranks are same, choose one as root and increment its rank
            else {
                subsetArray[rootOfY].parent = rootOfX;
                subsetArray[rootOfX].rank++;
            }
        }
    }

    public static void main(String[] args) {
        Kargers_neuProbieren karg = new Kargers_neuProbieren();
        //karg.kargersMinCut(karg);

        Animal.startGeneratorWindow(karg);

        System.out.println("Finished.");

        //System.out.println("Kargers_neuProbieren Min Cut for Given Graph is: " + karg.kargersMinCut(karg));


    }

}