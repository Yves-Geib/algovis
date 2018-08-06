package AnimalGraph;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.*;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.adjoint.Matrix;

import java.awt.*;
import java.util.*;

public class AnimalGraph_Test1 implements Generator {

    private Language lang;
    private Text header;
    Text name1, name2;
    Rect rect;
    TextProperties introProp;
    SourceCodeProperties srcProp;
    private Graph graph;

    AnimalSubset testAnimalSet = new AnimalSubset();

    int v; // number of vertices (nodes)
    int e; // number of edges ACHTUNG wurde für Beispiel geändert
    AnimalEdge[] edgeArray; // array to store all edges
    protected int cutEdges;
    protected int startContract;
    protected int endContract;
    int y = 0;


    int[][] testMatrix = {
            {0, 1, 1, 0},
            {0, 0, 0, 1},
            {0, 0, 0, 1},
            {0, 0, 0, 0},
    };



    // hier muss noch v und e dynamisch gemacht werden, v ist testMatrix.length und e muss gezählt werden


    public AnimalGraph_Test1(int v, int e) {
        this.v = v;
        this.e = e;
        edgeArray = new AnimalEdge[e];

        // Filling the edgeArray with the Matrix Input to let Kargers Execute with the Input Graph
        for (int i = 0; i < testMatrix.length; i++) {
            for (int j = 0; j < testMatrix.length; j++) {
                if (testMatrix[i][j] == 1) {
                    edgeArray[y] = new AnimalEdge(i, j);
                    y++;
                }
            }
        }
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
        cutEdges = 0;

        for (int i = 0; i < nrOfEdges; i++) {
            int subset1 = testAnimalSet.find(subset, edgeArray[i].src);
            int subset2 = testAnimalSet.find(subset, edgeArray[i].dest);
            if (subset1 != subset2) {
                cutEdges++;
            }
        }
        return cutEdges;
    }


    /*
    ###########################################
    ############### MAIN METHOD ###############
    ###########################################
    */
    public static void main(String[] args) {

        Generator generator = new AnimalGraph_Test1(4, 4);
        Animal.startGeneratorWindow(generator);
        //Language lang = new AnimalScript("Karger's Minimal Cut", "Hannah Drews, Yves Geib", 640, 480);
        //Animal.startAnimationFromAnimalScriptCode(lang.toString());

        /*
        0A------3C
        |       |
        |       |
        |       |
        1B------4D
        */

        AnimalGraph_Test1 testGraph = new AnimalGraph_Test1(4, 4);
        System.out.println("Kargers Min Cut for Given Graph is: " + testGraph.kargersMinCut(testGraph));


        Language lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,"Kargers Minimum Cut", "Hannah Drews, Yves Geib", 640, 480);
        lang.setStepMode(true);

        TextProperties tp = new TextProperties();
        tp.set("color", Color.BLACK);
        CircleProperties cp = new CircleProperties();
        cp.set("color", Color.BLACK);
        PolylineProperties pp = new PolylineProperties();
        pp.set("color", Color.BLACK);

        String[] nodeNames = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};


        int[][] testMatrix = {
                {0, 1, 1, 0},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {0, 1, 1, 0},
        };

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


        //Erstelle Platz für Primitives
        Text[] textArray = new Text[testMatrix.length];
        Circle[] circleArray = new Circle[testMatrix.length];

        //Erstelle Polyline Matrix, um Polylines an korrekten Stellen zu zeichnen
        Polyline[][] polyMatrix = new Polyline[testMatrix.length][testMatrix.length];

        int i; //for for-schleife
        int j; //for for-schleife
        int[] rememberedNodes = new int[testMatrix.length]; //Erstelle int-Array für das Speichern aller Nodes, die an dem contracteten Node dranhängen. Damit können nachher die neuen Lines gezeichnet werden.


        //Create Graph dynamically
        for (i = 0; i < testMatrix.length; i++) {

            /*
            create Text of node
               0A    1B

               2C    3D
            */



            if(i < testMatrix.length/2)
                textArray[i] = lang.newText(new Coordinates(100 + 100*i, 100), nodeNames[i], "node " + nodeNames[i], null, tp);
            else
                textArray[i] = lang.newText(new Coordinates(100 + 100 * (i - testMatrix.length/2), 100 + 100), nodeNames[i], "node " + nodeNames[i], null, tp);


            //create Circle around Text

            circleArray[i] = lang.newCircle(new Offset(0, 0, textArray[i], AnimalScript.DIRECTION_C),30,"Circle " + nodeNames[i],null, cp);
        }

        // create Polylines as Edges between Nodes as Text an Circles

        /*
        //polyMatrix wird nur an Stellen gezeichnet, an denen in testMatrix[i][j] eine 1 steht
        for (i = 0; i < testMatrix.length; i++) {
            for (j = 0; j < testMatrix.length; j++) {
                if (testMatrix[i][j] == 1) {
                    // wir können verhindern, dass wir nicht mehr Wissen welche Edge zu welchen Nodes gehört, indem wir einfach das Array mit Null füllen wo keine Edges sind dann ist Anzahl Plätze in der Matrix = Länge des Arrays
                    polyMatrix[i][j] = lang.newPolyline(new Node[] { new Offset( 0, 0, circleArray[i], AnimalScript.DIRECTION_C), new Offset( 0, 0, circleArray[j], AnimalScript.DIRECTION_C)}, "Edge " + textArray[i] + "-" + textArray[j] + "after being contracted", null);
                    System.out.println(textArray[i].getText() + " " +  textArray[j].getText());

                }
                else
                    polyMatrix[i][j] = null;
            }
        }
        */

        //polyMatrix wird an allen Stellen gezeichnet und direkt mit hide an den Stellen versehen, an denen in testMatrix[i][j] eine 0 steht.
        for (i = 0; i < testMatrix.length; i++) {
            for (j = 0; j < testMatrix.length; j++) {
                // wir können verhindern, dass wir nicht mehr Wissen welche Edge zu welchen Nodes gehört, indem wir einfach das Array mit Null füllen wo keine Edges sind dann ist Anzahl Plätze in der Matrix = Länge des Arrays
                polyMatrix[i][j] = lang.newPolyline(new Node[] { new Offset( 0, 0, circleArray[i], AnimalScript.DIRECTION_C), new Offset( 0, 0, circleArray[j], AnimalScript.DIRECTION_C)}, "Edge " + textArray[i] + "-" + textArray[j] + "after being contracted", null);
                System.out.println(textArray[i].getText() + " " +  textArray[j].getText());
                if(testMatrix[i][j] == 0) {
                    polyMatrix[i][j].hide();
                }
            }
        }


        System.out.println(testMatrix.length);
        System.out.println(circleArray[testGraph.endContract].getName());
        lang.nextStep();


        //Highlighten der Polyline, die entfernt werden soll. Hier doppelt, da die Matrix symmetrisch ist.
        //TODO
        //Die Farbe muss noch dynamisch übergeben werden.
        polyMatrix[testGraph.startContract][testGraph.endContract].changeColor("color", Color.RED, null, null);
        polyMatrix[testGraph.endContract][testGraph.startContract].changeColor("color", Color.RED, null, null);

        lang.nextStep("Highlighten");

        // highlighten der beiden Nodes, die zusammengeführt werden, allerdings aktuell nur für den zweiten Cut
        // TODO
        // dynamisch machen, damit alle Cuts gehighlightet werden (Das geschieht dann mit Verschieben des Codes in die whileSchleife(ganz oben)
        textArray[testGraph.startContract].changeColor("color", Color.RED, null,null);
        circleArray[testGraph.startContract].changeColor("color", Color.RED, null, null);
        textArray[testGraph.endContract].changeColor("color", Color.RED, null, null);
        circleArray[testGraph.endContract].changeColor("color", Color.RED, null, null);

        lang.nextStep("Highlighten");

        /*
        //Färbe alle Polylines rot, die an endContract dranhängen (hier der Wert j in der polyMatrix)
        for (j = 0; j < testMatrix.length; j++) {
            polyMatrix[testGraph.endContract][j].changeColor("color", Color.RED, null, null);
            polyMatrix[j][testGraph.endContract].changeColor("color", Color.RED, null, null);
        }
        */

        //Hide den betroffenen Node
        textArray[testGraph.endContract].hide();
        circleArray[testGraph.endContract].hide();

        //Ausgelagert: Benennung des Nodes
        String nodename = textArray[testGraph.startContract].getText() + " " + textArray[testGraph.endContract].getText();
        System.out.println("cut: " + nodename);

        //Färbe den contracteten Node wieder schwarz und füge den entfernten Node hinzu (Also Node A wird zu Node A B)
        textArray[testGraph.startContract].setText(nodename, null, null);
        //circleArray[testGraph.startContract].hide();
        //circleArray[testGraph.startContract] = lang.newCircle(new Offset(0, 0, textArray[testGraph.startContract], AnimalScript.DIRECTION_C),20,"Circle " + nodename,null, cp);
        textArray[testGraph.startContract].changeColor("color", Color.BLACK, null,null);
        circleArray[testGraph.startContract].changeColor("color", Color.BLACK, null, null);

        //Erstelle neues Polyline Array, um die neu gezeichneten Polylines zu speichern
        Polyline[] newLine = new Polyline[testMatrix.length];

        //Hide alle Polylines, die an endContract dranhängen (hier der Wert j in der polyMatrix)
        for (j = 0; j < testMatrix.length; j++) {
            polyMatrix[testGraph.endContract][j].hide();
            polyMatrix[j][testGraph.endContract].hide();

            //Befülle rememberedNodes mit den Nodes, die an dem contracteten Node dranhängen, damit später die Polylines zu diesen Nodes gezeichnet werden können.
            if(testGraph.startContract != j && testMatrix[testGraph.endContract][j] == 1) { //Schaue nur die Nodes an, die an dem contracteten Node dranhängen. Alle anderen werden ignoriert.
                rememberedNodes[j] = j;
                System.out.println(Arrays.toString(rememberedNodes));
            }

            //Zeichne neue Polyline für den contracteten Node und den startNode (Das ist die Line, die im Algorithmus selbst eigentlich übrig bleibt und verschoben wird)
            newLine[testGraph.startContract] = lang.newPolyline(new Node[] { new Offset( 0, 0, circleArray[testGraph.startContract], AnimalScript.DIRECTION_C), new Offset( 0, 0, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C)}, "newLine", null);
        }
        lang.nextStep();

        // Start animal to view visualization
        Animal.startAnimationFromAnimalScriptCode(lang.toString());

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



        //this.createGraph();
        //this.lang.finalizeGeneration();
        return null;

        //return this.lang.toString();

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

}