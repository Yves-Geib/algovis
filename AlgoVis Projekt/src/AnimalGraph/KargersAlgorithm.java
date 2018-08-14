/*
 * KargersAlgorithm.java
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
import interactionsupport.models.MultipleChoiceQuestionModel;

public class KargersAlgorithm implements Generator {
    private Language lang;
    private TextProperties headerProps;
    private TextProperties textProps;
    private CircleProperties circleProps;
    private PolylineProperties polylineProps;
    private SourceCodeProperties sourceCodeProps;
    private RectProperties rectProps;
    private SourceCode code;
    private Graph graph;

    Node[] nodeList;
    private int coordinateX, coordinateY; //Coordinates of the user-input graph to translate them into our graph

    AnimalSubset testAnimalSet = new AnimalSubset();

    //int v; // number of vertices (nodes)
    //int e; // number of edges ACHTUNG wurde für Beispiel geändert
    private AnimalEdge[] edgeArray; // array to store all edges
    private int cutEdges;
    private int startContract;
    private int endContract;

    public KargersAlgorithm() {

    }

    public void init(){
        this.lang = new AnimalScript("Karger's algorithm for Minimum Cut", "Hannah Drews, Yves Geib", 800, 600);
        this.lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {


        this.lang.setInteractionType(1024);
        //this.createGraph();

        this.headerProps = (TextProperties)props.getPropertiesByName("textProps");
        this.circleProps = (CircleProperties)props.getPropertiesByName("circleProps");
        this.polylineProps = (PolylineProperties)props.getPropertiesByName("polylineProps");
        this.sourceCodeProps = new SourceCodeProperties();
        this.sourceCodeProps.set("font", new Font("Monospaced", 0, 12));
        this.sourceCodeProps.set("highlightColor", Color.RED);
        this.sourceCodeProps.set("color", Color.BLACK);
        this.graph = (Graph)primitives.get("graph");

        this.intro();
        this.kargersMinCut();
        this.lang.finalizeGeneration();
        return this.lang.toString();
    }

    private void intro() {
        this.headerProps = new TextProperties();
        this.headerProps.set("font", new Font("SansSerif", 1, 24));
        Text header = this.lang.newText(new Coordinates(200, 50), "Karger's algorithm for Minimum Cut", "header", (DisplayOptions)null, headerProps);
        this.rectProps = new RectProperties();
        this.rectProps.set("fillColor", Color.CYAN);
        this.rectProps.set("filled", true);
        this.rectProps.set("depth", 2);
        this.lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5, 5, header, "SE"), "hrect", (DisplayOptions)null, rectProps);
        this.sourceCodeProps = new SourceCodeProperties();
        this.sourceCodeProps.set("font", new Font("SansSerif", 0, 16));
        this.sourceCodeProps.set("color", Color.BLACK);
        this.code = this.lang.newSourceCode(new Offset(-150, 30, header, "SW"), "intro", (DisplayOptions)null, sourceCodeProps);
        this.code.addCodeLine("Karger's algorithm for Minimum Cut sucht in einem ungerichteten zusammenhängenden Graphen den minimalen Schnitt. ", (String)null, 0, (Timing)null);
        this.code.addCodeLine("Er kontrahiert dabei so lange zufällig ausgewählte Kanten, bis nur noch zwei Knoten übrig sind. ", (String)null, 0, (Timing)null);
        this.code.addCodeLine("", (String)null, 0, (Timing)null); //Absatz im Text
        this.code.addCodeLine("Kontrahieren bedeutet, dass zwei Knoten vereinigt werden und die Kante dazwischen entfernt wird. ", (String)null, 0, (Timing)null);
        this.code.addCodeLine("Alle Self-Loops, die hierbei entstehen, werden entfernt. Alle verschobenen Kanten bleiben bestehen. ", (String)null, 0, (Timing)null);
        this.code.addCodeLine("", (String)null, 0, (Timing)null); //Absatz im Text
        this.code.addCodeLine("Die übrigen Kanten, die an dem entfernten Knoten dranhingen, gehen nun von dem neuen, vereinigten Knoten aus. ", (String)null, 0, (Timing)null);
        this.code.addCodeLine("Die Summe der übrig gebliebenen Kanten zwischen den letzten zwei Knoten wird als Schnitt bezeichnet. ", (String)null, 0, (Timing)null);
        this.code.addCodeLine("", (String)null, 0, (Timing)null); //Absatz im Text
        this.code.addCodeLine("Anwendung findet der Algorithmus zum Beispiel beim Testen von Netzwerken, um eventuelle Schwächen zu beheben und deren Sicherheit zu erhöhen. ", (String)null, 0, (Timing)null);
        this.code.addCodeLine("Ein weiteres Anwendungsbeispiel ist die Bildverarbeitung. Hier wird der Algorithmus zur Segmentierung des Bildes verwendet, ", (String)null, 0, (Timing)null);
        this.code.addCodeLine("um benachbarte Pixel mit denselben Eigenschaften (Farbe, Textur, Dichte) zusammenzufassen, ", (String)null, 0, (Timing)null);
        this.code.addCodeLine("damit die Weiterverarbeitung und Analyse des Bildes einfacher und effizienter wird. ", (String)null, 0, (Timing)null);
        this.lang.nextStep("Intro");
        this.code.hide();

    }

    public int kargersMinCut() {
        int i; //for for-loop
        int j; //for for-loop

        this.nodeList = this.graph.getNodes();

        int firstNodePosition = this.graph.getPositionForNode(nodeList[0]);
        this.sourceCodeProps = new SourceCodeProperties();
        this.sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        this.sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
        this.sourceCodeProps.set("highlightColor", Color.RED);
        this.sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        this.code = lang.newSourceCode(new Coordinates(firstNodePosition + 500, 200), "sourceCode", null, this.sourceCodeProps);
        this.code.addCodeLine("while (nodes > 2)", null, 1, null);
        this.code.addCodeLine("choose random edge (u,v) from graph", null, 2, null);
        this.code.addCodeLine("merge u and v", null, 3, null);
        this.code.addCodeLine("reattach other edges", null, 3, null);
        this.code.addCodeLine("remove self-loops", null, 3, null);
        this.code.addCodeLine("return cut represented by two nodes", null, 2, null);

        this.code.highlight(0);

        int[][] inputMatrix = this.graph.getAdjacencyMatrix();
        int[][] testMatrix = new int[inputMatrix.length][inputMatrix.length];
        int[][] transponierteMatrix = new int[inputMatrix.length][inputMatrix.length];
        int edgeCounter = 0; //this stores the value of edges in the input-Graph

        //Printe testMatrix, die vom User in Animal übergeben wurde
        //TODO bisher noch XML Datei, noch nicht dynamisch
        for(i = 0; i < inputMatrix.length; i++) {
            for (j = 0; j < inputMatrix.length; j++) {
                if(inputMatrix[i][j] == 1)
                    edgeCounter++;
                System.out.print(inputMatrix[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("Anzahl Einsen in der Matrix = Anzahl Edges = " + edgeCounter);


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


        // get given Graph
        //int inputMatrixLength = this.graph.getAdjacencyMatrix().length;
        int nrOfVertices = this.graph.getSize();
        int nrOfEdges = edgeCounter;

        //fill edgeArray with given Graph
        edgeArray = new AnimalEdge[nrOfEdges];
        int y = 0; //edgeArray Counter
        // Filling the edgeArray with the Matrix Input to let KargersAlgorithm execute with the Input Graph
        for (i = 0; i < inputMatrix.length; i++) {
            for (j = 0; j < inputMatrix.length; j++) {

                if (inputMatrix[i][j] == 1) {
                    edgeArray[y] = new AnimalEdge(i, j);
                    y++;
                }
            }
        }


        TextProperties tp = new TextProperties();
        tp.set("color", Color.BLACK);
        CircleProperties cp = new CircleProperties();
        cp.set("color", Color.BLACK);
        cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        cp.set("depth", 2);
        PolylineProperties pp = new PolylineProperties();
        pp.set("color", Color.BLACK);
        pp.set("depth", 3);

        //Erstelle Platz für Primitives
        Text[] textArray = new Text[inputMatrix.length];
        Circle[] circleArray = new Circle[inputMatrix.length];

        //Erstelle Polyline Matrix, um Polylines an korrekten Stellen zu zeichnen
        Polyline[][] polyMatrix = new Polyline[inputMatrix.length][inputMatrix.length];

        /*Create Graph with coordinates and nodeLabels from user input
          0A 1B
          2C 3D
        */
        // this.nodeList = this.graph.getNodes(); //this gets done above because of Pseudo-Code-Position, but is mainly used in this place.
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
        //polyMatrix wird an allen Stellen der testMatrix gezeichnet  versehen, an denen in testMatrix[i][j] eine 0 steht.
        for (i = 0; i < testMatrix.length; i++) {
            for (j = 0; j < testMatrix.length; j++) {
                if (testMatrix[i][j] == 1) {
                    // wir können verhindern, dass wir nicht mehr Wissen welche Edge zu welchen Nodes gehört, indem wir einfach das Array mit Null füllen wo keine Edges sind dann ist Anzahl Plätze in der Matrix = Länge des Arrays
                    polyMatrix[i][j] = lang.newPolyline(new Node[]{new Offset(0, 0, textArray[i], AnimalScript.DIRECTION_C), new Offset(0, 0, textArray[j], AnimalScript.DIRECTION_C)}, "Edge " + textArray[i] + "-" + textArray[j] + "after being contracted", null, pp);
                    System.out.println(textArray[i].getText() + " " + textArray[j].getText());
                }
            }
        }
        lang.nextStep("Drawing Nodes & Polylines");

        // allocate memory for creating i subsets
        AnimalSubset[] subset = new AnimalSubset[nrOfVertices];

        // create i subsets of single elements
        for (i = 0; i < nrOfVertices; i++) {
            subset[i] = new AnimalSubset();
            subset[i].parent = i;
            subset[i].rank = 0;
        }

        // here should be a single edge...
        Random r = new Random();

        // initially there are nrOfVertices in given Graph
        int vertices = nrOfVertices;

        // graph is contracted until there are two vertices left
        while (vertices > 2) {
            // generates a random int between 0 and nrOfEdges
            int x = r.nextInt(nrOfEdges);

            // find vertices (sets) of current randomly picked edge
            int subset1 = testAnimalSet.find(subset, edgeArray[x].src);
            int subset2 = testAnimalSet.find(subset, edgeArray[x].dest);
            startContract = subset[edgeArray[x].src].parent;
            endContract = subset[edgeArray[x].dest].parent;
            this.code.unhighlight(0);
            this.code.highlight(1);

            // if the vertices belong to the same subset, this edge is not considered
            // also if the vertices are already contracted, they should not be considered. If they do, it's
            if (subset1 == subset2
                    || (testMatrix[startContract][endContract] == 1 && polyMatrix[startContract][endContract] == null)
                    || (testMatrix[startContract][startContract] == 1 && polyMatrix[endContract][startContract] == null)
                    || (testMatrix[endContract][startContract] == 1 &&polyMatrix[startContract][endContract] == null)
                    || (testMatrix[endContract][startContract] == 1 && polyMatrix[endContract][startContract] == null)) {
                continue;
            }
            // else contract the edge (combine the subsets and combine the nodes of the edge into one)
            else {
                System.out.println("Contracting edge" + edgeArray[x].src + edgeArray[x].dest);

                System.out.println("startContract: " + startContract + " endContract: " + endContract);

                //Highlighten der Polyline, die entfernt werden soll.
                //TODO Die Farbe muss noch dynamisch übergeben werden.
                //Wenn an beiden Stellen in der Matrix eine Eins steht, highlighte nur eine Edge, ansonsten highlighte die Edge, die in der Matrix vorhanden ist.
                if (polyMatrix[startContract][endContract] != null && polyMatrix[endContract][startContract] != null)
                    polyMatrix[startContract][endContract].changeColor("color", Color.RED, null, null);
                if (polyMatrix[startContract][endContract] != null)
                    polyMatrix[startContract][endContract].changeColor("color", Color.RED, null, null);
                if (polyMatrix[endContract][startContract] != null)
                    polyMatrix[endContract][startContract].changeColor("color", Color.RED, null, null);

                lang.nextStep("Highlighten Polyline");

                // highlighten der beiden Nodes, die zusammengeführt werden, allerdings aktuell nur für den zweiten Cut
                // TODO
                // dynamisch machen, damit alle Cuts gehighlightet werden (Das geschieht dann mit Verschieben des Codes in die whileSchleife der kargersMinCut Methode
                textArray[startContract].changeColor("color", Color.RED, null, null);
                circleArray[startContract].changeColor("color", Color.RED, null, null);
                textArray[endContract].changeColor("color", Color.RED, null, null);
                circleArray[endContract].changeColor("color", Color.RED, null, null);

                lang.nextStep("Highlighten Node");

                this.code.unhighlight(1);
                this.code.highlight(2);
                this.code.highlight(3);
                this.code.highlight(4);

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

                //Hide all lines that are attached to endContract (here the value j in polyMatrix)
                for (j = 0; j < polyMatrix.length; j++) {
                    int[] rememberedNodes = new int[polyMatrix.length]; //Erstelle int-Array für das Speichern aller Nodes, die an dem contracteten Node dranhängen. Damit können nachher die neuen Lines gezeichnet werden.

                    //Befülle rememberedNodes mit den Nodes, die an dem contracteten Node dranhängen, damit später die Polylines zu diesen Nodes gezeichnet werden können.
                    if (startContract != j) { //Schaue nur die Nodes an, die an dem contracteten Node dranhängen. Alle anderen werden ignoriert.

                        // Hide and delete last line which could still be visible from previous iteration.
                        if (polyMatrix[startContract][endContract] != null) {
                            polyMatrix[startContract][endContract].hide();
                            polyMatrix[startContract][endContract] = null;
                            System.out.println("Hier lösche ich die rote Line oberes Dreieck");
                        }
                        if (polyMatrix[endContract][startContract] != null) {
                            polyMatrix[endContract][startContract].hide();
                            polyMatrix[endContract][startContract] = null;
                            System.out.println("Hier lösche ich die rote Line unteres Dreieck");
                        }

                        //Hier geschieht das Hiden, oberes Dreieck der Matrix
                        if (polyMatrix[endContract][j] != null) {
                            rememberedNodes[j] = j; //Merken der Knoten, zu denen die Lines gezeichnet werden.
                            polyMatrix[endContract][j].hide();
                            polyMatrix[endContract][j] = null;
                            if (polyMatrix[startContract][rememberedNodes[j]] != null) {
                                polyMatrix[startContract][rememberedNodes[j]].changeColor("color", Color.GREEN, null, null);
                            }
                            else polyMatrix[startContract][rememberedNodes[j]] = lang.newPolyline(new Node[]{new Offset(5, 5, circleArray[startContract], AnimalScript.DIRECTION_C),
                                        new Offset(5, 5, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C)}, "newLine", null, pp);

                            System.out.println(Arrays.toString(rememberedNodes));
                            System.out.println("Wert j a: " + j);

                        }
                        //Unteres Dreieck der Matrix
                        if (polyMatrix[j][endContract] != null) {
                            rememberedNodes[j] = j; //Merken der Knoten, zu denen die Lines gezeichnet werden.
                            polyMatrix[j][endContract].hide();
                            polyMatrix[j][endContract] = null;
                            if (polyMatrix[rememberedNodes[j]][startContract] != null) {
                                polyMatrix[rememberedNodes[j]][startContract].changeColor("color", Color.GREEN, null, null);
                            }
                            else polyMatrix[rememberedNodes[j]][startContract] = lang.newPolyline(new Node[]{new Offset(5, 5, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C),
                                        new Offset(5, 5, circleArray[startContract], AnimalScript.DIRECTION_C)}, "newLine", null, pp);

                            System.out.println(Arrays.toString(rememberedNodes));
                            System.out.println("Wert j b:" + j);
                        }
                    }
                }
                lang.nextStep("Hiden_& Neuzeichnen und den Algorithmus einen Schritt weiterführen");

                this.code.unhighlight(2);
                this.code.unhighlight(3);
                this.code.unhighlight(4);

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
            System.out.println();
            if (subset1 != subset2) {
                cutEdges++;
            }
        }
        System.out.println("FINAL: cutedges = " + cutEdges);

        lang.nextStep("Endergebnis ausgeben und letzte CodeLine highlighten");
        this.code.highlight(5);
        this.rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
        rectProps.set("depth", 1);
        this.textProps = new TextProperties();
        textProps.set("color", Color.BLACK);
        textProps.set("depth", 0);
        textProps.set("font", new Font("SansSerif", 3, 12));
        Text rest = lang.newText(new Offset(0, 50, code, "SW"), "Karger's Minimum Cut ist: " + cutEdges, "scr", (DisplayOptions)null, textProps);
        Rect resrect = lang.newRect(new Offset(-5, -5, rest, "NW"), new Offset(5, 5, rest, "SE"), "resrect", (DisplayOptions)null, rectProps);
        Text explain = lang.newText(new Offset( 0, 10, rest, "SW"), "Das bedeutet, auf der letzten Kante, die hier zu sehen ist, liegen " + cutEdges +" Kanten aufeinander.", "rest", null, tp);

        lang.nextStep("MultipleChoice");
        MultipleChoiceQuestionModel firstmulti = new MultipleChoiceQuestionModel("first");
        firstmulti.setPrompt("Warum kann es sein, dass Karger’s Algorithmus nicht immer den minimalen Schnitt berechnet?");
        firstmulti.addAnswer("Weil die Kanten zufällig ausgewählt werden.", 1, "Richtig. Weil die Kanten zufällig ausgewählt werden, kann nicht immer ein minimales Ergebnis garantiert werden.");
        firstmulti.addAnswer("Weil falsche Kanten ausgewählt werden können.", 0, "Falsch. Die ausgewählten Kanten sind nicht falsch, sondern lediglich zufällig.");
        firstmulti.addAnswer("Weil der Algorithmus nicht alle Kanten berücksichtigt.", 0, "Falsch. Der Algorithmus berücksichtigt alle Kanten, er wählt jedoch zufällig die nächste Kante aus.");
        firstmulti.addAnswer("Weil die Kanten nicht zufällig ausgewählt werden.", 0, "Falsch. Gerade weil die Kanten zufällig ausgewählt werden, kann es sein, dass das Ergebnis nicht immer minimal ist.");
        this.lang.addMCQuestion(firstmulti);

        lang.nextStep("next MultipleChoice");

        MultipleChoiceQuestionModel secmulti = new MultipleChoiceQuestionModel("second");
        secmulti.setPrompt("Wie wird ein Graph genannt, bei dem zwischen zwei Knoten mehrere Kanten liegen können?");
        secmulti.addAnswer("Wald", 0, "Falsch. Ein solcher Graph wird Multigraph genannt.");
        secmulti.addAnswer("Multigraph", 1, "Richtig!");
        secmulti.addAnswer("Tree", 0, "Falsch. Ein solcher Graph wird Multigraph genannt.");
        secmulti.addAnswer("zusammenhängender Graph", 0, "Falsch. Ein solcher Graph wird Multigraph genannt. Ein zusammenhängender Graph ist das, was Karger's Algorithmus als Input bekommt.");
        this.lang.addMCQuestion(secmulti);

        lang.nextStep("last MultipleChoice");
        MultipleChoiceQuestionModel lastmulti = new MultipleChoiceQuestionModel("last");
        lastmulti.setPrompt("Wie wird ein Algorithmus genannt, der mit einer gewissen Wahrscheinlichkeit falsche Ergebnisse liefert?");
        lastmulti.addAnswer("Las Vegas", 0, "Falsch. Ein solcher Algorithmus wird Monte Carlo Algorithmus genannt.");
        lastmulti.addAnswer("Monte Carlo", 1, "Richtig!");
        lastmulti.addAnswer("Atlantic City", 0, "Falsch. Ein solcher Algorithmus wird Monte Carlo Algorithmus genannt.");
        lastmulti.addAnswer("San Antonio", 0, "Falsch. Ein solcher Algorithmus wird Monte Carlo Algorithmus genannt.");
        this.lang.addMCQuestion(lastmulti);

        return cutEdges;

    }

    /*public void showSourceCode(){
        SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
        sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        SourceCode scr = lang.newSourceCode(new Coordinates(450, 150), "sourceCode", null, sourceCodeProps);
        scr.addCodeLine("while (nodes > 2)", null, 1, null);
        scr.addCodeLine("choose random edge (u,v) from graph", null, 2, null);
        scr.addCodeLine("merge u and v", null, 3, null);
        scr.addCodeLine("remove self-loops", null, 4, null);
        scr.addCodeLine("return cut represented by two nodes", null, 5, null);
    }*/

    public String getName() {
        return "Karger's algorithm for Minimum Cut";
    }

    public String getAlgorithmName() {
        return "Karger's algorithm for Minimum Cut, by David Karger, published in 1993";
    }

    public String getAnimationAuthor() {
        return "Hannah Drews, Yves Geib";
    }

    public String getDescription(){
        return "Karger’s Algorithmus erzeugt zufällig den minimalen Schnitt eines zusammenhängenden, ungerichteten Graphen. "
                + "Er basiert auf der Überlegung der Kontraktion von zwei Knoten u und v in einen Knoten. "
                + "Dadurch wird die gesamte Anzahl der Knoten nach und nach reduziert, bis nur noch zwei Knoten übrig sind. "
                + "Die Kanten der kontrahierten Knoten werden dabei vom Ergebnisknoten zu den restlichen Knoten neu angefügt. "
                + "Dabei können auch mehrere Kanten zwischen zwei Knoten liegen, wobei solch ein Graph Multigraph genannt wird. "
                + "Karger’s Algorithmus kontrahiert so lange zufällig ausgewählte Kanten, bis nur noch zwei Knoten übrig sind. "
                + "Die Summe der übrig gebliebenen Kanten zwischen den letzten zwei Knoten wird als Schnitt bezeichnet.\n"
                + "\n" + "Karger’s Algorithmus arbeitet dabei mit zufällig ausgewählten Kanten, weshalb das Ergebnis nicht immer der minimale Schnitt sein muss. "
                + "Diese Art von Algorithmen wird auch Monte Carlo Algorithmus genannt.\n";
    }

    public String getCodeExample(){
        return "while (nodes > 2)\n"
        + "\tchoose random edge (u,v) from graph\n"
        + "\tmerge u and v\n"
        + "\tremove self-loops\n"
        + "return cut represented by two nodes\n";
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

    public static void main(String[] args) {
        KargersAlgorithm karg = new KargersAlgorithm();
        Animal.startGeneratorWindow(karg);

        System.out.println("Finished.");
    }

}