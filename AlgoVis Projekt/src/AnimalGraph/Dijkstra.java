//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package AnimalGraph;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

public class Dijkstra implements Generator {
    private Language lang;
    private GraphProperties graphProp;
    private Graph graph;
    private SourceCodeProperties sourceCode;
    private int start;
    private MatrixProperties matrixProperties;
    private SourceCode code;
    private Text kommentar;
    private StringMatrix stringMatrix;
    private boolean directed;
    private int nextQuestionCount = 0;

    public Dijkstra() {
    }

    public void init() {
        this.lang = new AnimalScript("Dijkstra", "Konstantin Ramig", 800, 800);
        this.lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer var1, Hashtable<String, Object> var2) {
        this.graphProp = (GraphProperties)var1.getPropertiesByName("graphProp");
        this.graph = (Graph)var2.get("graph");
        this.start = (Integer)var2.get("start");
        this.sourceCode = new SourceCodeProperties();
        this.sourceCode.set("font", new Font("Monospaced", 0, 12));
        this.sourceCode.set("highlightColor", Color.RED);
        this.sourceCode.set("color", Color.BLACK);
        this.matrixProperties = new MatrixProperties();
        this.matrixProperties.set("cellHighlight", Color.YELLOW);
        this.matrixProperties.set("elemHighlight", Color.GREEN);
        GraphProperties var3 = this.graph.getProperties();
        this.directed = (Boolean)var3.get("directed");
        this.graphProp.set("directed", this.directed);
        this.lang.setInteractionType(1024);
        this.intro();
        this.showCodeGraph();
        this.initQuestionGroups();
        this.dijkstra();
        this.lang.finalizeGeneration();
        return this.lang.toString();
    }

    private void intro() {
        TextProperties var1 = new TextProperties();
        var1.set("font", new Font("SansSerif", 1, 24));
        Text var2 = this.lang.newText(new Coordinates(200, 50), "Dijkstra", "header", (DisplayOptions)null, var1);
        RectProperties var3 = new RectProperties();
        var3.set("fillColor", Color.YELLOW);
        var3.set("filled", true);
        var3.set("depth", 2);
        this.lang.newRect(new Offset(-5, -5, var2, "NW"), new Offset(5, 5, var2, "SE"), "hrect", (DisplayOptions)null, var3);
        SourceCodeProperties var4 = new SourceCodeProperties();
        var4.set("font", new Font("SansSerif", 0, 18));
        var4.set("color", Color.BLACK);
        SourceCode var5 = this.lang.newSourceCode(new Offset(-150, 30, var2, "SW"), "intro", (DisplayOptions)null, var4);
        var5.addCodeLine("Der Diijkstra Algorithmus dient dazu innerhalb eines Graphen von einem gegebenen", (String)null, 0, (Timing)null);
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

    private void showCodeGraph() {
        Node[] var1 = new Node[this.graph.getSize()];
        String[] var2 = new String[this.graph.getSize()];

        for(int var3 = 0; var3 < this.graph.getSize(); ++var3) {
            System.out.println(Arrays.toString(this.graph.getAdjacencyMatrix()[var3]));
            var1[var3] = this.graph.getNode(var3);
            var2[var3] = this.graph.getNodeLabel(var3);
        }

        this.graph = this.lang.newGraph("graph", this.graph.getAdjacencyMatrix(), var1, var2, (DisplayOptions)null, this.graphProp);

        try {
            this.graph.moveTo("NE", (String)null, new Coordinates(20, 100), (Timing)null, (Timing)null);
        } catch (IllegalDirectionException var4) {
            var4.printStackTrace();
        }

        this.code = this.lang.newSourceCode(new Offset(200, 0, this.graph, "NE"), "code", (DisplayOptions)null, this.sourceCode);
        this.code.addCodeLine("public int[] dijkstra (WeightedGraph G, int s) {", (String)null, 0, (Timing)null);
        this.code.addCodeLine("int[] dist = new int [G.size()];", (String)null, 1, (Timing)null);
        this.code.addCodeLine("int[] pred = new int [G.size()];", (String)null, 1, (Timing)null);
        this.code.addCodeLine("boolean [] visited = new boolean [G.size()];", (String)null, 1, (Timing)null);
        this.code.addCodeLine("", (String)null, 0, (Timing)null);
        this.code.addCodeLine("for (int i=0; i<dist.length; i++) {", (String)null, 1, (Timing)null);
        this.code.addCodeLine("dist[i] = Integer.MAX_VALUE;", (String)null, 2, (Timing)null);
        this.code.addCodeLine("pred[i] = null;", (String)null, 2, (Timing)null);
        this.code.addCodeLine("visited[i] = false;", (String)null, 2, (Timing)null);
        this.code.addCodeLine("}", (String)null, 1, (Timing)null);
        this.code.addCodeLine("dist[s] = 0;", (String)null, 1, (Timing)null);
        this.code.addCodeLine("", (String)null, 0, (Timing)null);
        this.code.addCodeLine("int next;", (String)null, 1, (Timing)null);
        this.code.addCodeLine("while ((next = minVertex(dist, visited)) != -1) {", (String)null, 1, (Timing)null);
        this.code.addCodeLine("visited[next] = true;", (String)null, 2, (Timing)null);
        this.code.addCodeLine("", (String)null, 0, (Timing)null);
        this.code.addCodeLine("int [] n = G.neighbors (next);", (String)null, 2, (Timing)null);
        this.code.addCodeLine("for (int j=0; j<n.length; j++) {", (String)null, 2, (Timing)null);
        this.code.addCodeLine("if(n[j] != 0) {", (String)null, 3, (Timing)null);
        this.code.addCodeLine("int d = dist[next] + n[j];", (String)null, 4, (Timing)null);
        this.code.addCodeLine("if (dist[v] > d) {", (String)null, 4, (Timing)null);
        this.code.addCodeLine("dist[v] = d;", (String)null, 5, (Timing)null);
        this.code.addCodeLine("pred[v] = next;", (String)null, 5, (Timing)null);
        this.code.addCodeLine("visited[v]= false;", (String)null, 5, (Timing)null);
        this.code.addCodeLine("}", (String)null, 4, (Timing)null);
        this.code.addCodeLine("}", (String)null, 3, (Timing)null);
        this.code.addCodeLine("}", (String)null, 2, (Timing)null);
        this.code.addCodeLine("}", (String)null, 1, (Timing)null);
        this.code.addCodeLine("}", (String)null, 0, (Timing)null);
        this.code.registerLabel("//neuer Knoten", 13);
        this.code.registerLabel("//neue Kante", 18);
        TextProperties var5 = new TextProperties();
        var5.set("font", new Font("SansSerif", 1, 12));
        var5.set("color", new Color(0, 150, 0));
        this.kommentar = this.lang.newText(new Offset(-20, 20, this.code, "SW"), "", "kommentar", (DisplayOptions)null, var5);
        this.lang.nextStep();
    }

    private void dijkstra() {
        MsTiming var1 = new MsTiming(200);
        String var2 = "next";
        String var3 = "neighbor";
        boolean[] var4 = new boolean[this.graph.getSize()];
        int[] var5 = new int[this.graph.getSize()];
        int[] var6 = new int[this.graph.getSize()];
        this.code.highlight(0);
        this.kommentar.setText("//Dijkstra auf den Graphen mit Startknoten " + this.graph.getNodeLabel(this.start) + " anwenden", (Timing)null, (Timing)null);
        this.lang.nextStep("Algorithmus Start");
        this.code.unhighlight(0);
        this.code.highlight(1);
        this.code.highlight(2);
        this.code.highlight(3);
        this.kommentar.setText("//Arrays zum speichern der Werte anlegen", (Timing)null, (Timing)null);
        this.stringMatrix = this.lang.newStringMatrix(new Offset(-10, 50, this.graph, "SW"), new String[4][this.graph.getSize() + 1], "data", (DisplayOptions)null, this.matrixProperties);
        byte var7 = 10;
        int var8 = 550;

        int var9;
        for(var9 = 0; var9 < this.graph.getSize(); ++var9) {
            Coordinates var10 = (Coordinates)this.graph.getNode(var9);
            if (var10.getY() > var8 - 10) {
                var8 = var10.getY() + 10;
            }
        }

        try {
            this.stringMatrix.moveTo((String)null, "translate", new Coordinates(var7, var8), (Timing)null, (Timing)null);
        } catch (IllegalDirectionException var17) {
            var17.printStackTrace();
        }

        this.stringMatrix.put(0, 0, "", (Timing)null, var1);
        this.stringMatrix.put(1, 0, "Distanz", (Timing)null, var1);
        this.stringMatrix.put(2, 0, "Vorgaenger", (Timing)null, var1);
        this.stringMatrix.put(3, 0, "Besucht", (Timing)null, var1);

        for(var9 = 0; var9 < this.graph.getSize(); ++var9) {
            String var19 = this.graph.getNodeLabel(var9);
            this.stringMatrix.put(0, var9 + 1, var19, (Timing)null, var1);
        }

        Variables var18 = this.lang.newVariables();
        this.lang.nextStep("Hilfsvariablen anlegen");
        this.code.unhighlight(1);
        this.code.unhighlight(2);
        this.code.unhighlight(3);
        this.code.highlight(4);
        this.code.highlight(5);
        this.code.highlight(6);
        this.code.highlight(7);
        this.code.highlight(8);
        this.code.highlight(9);
        this.kommentar.setText("//Arrays initialisieren", (Timing)null, var1);
        this.stringMatrix.highlightCellColumnRange(1, 1, this.stringMatrix.getNrCols() - 1, (Timing)null, var1);
        this.stringMatrix.highlightCellColumnRange(2, 1, this.stringMatrix.getNrCols() - 1, (Timing)null, var1);
        this.stringMatrix.highlightCellColumnRange(3, 1, this.stringMatrix.getNrCols() - 1, (Timing)null, var1);

        int var20;
        for(var20 = 0; var20 < var6.length; ++var20) {
            var6[var20] = -1;
            this.stringMatrix.put(1, var20 + 1, "" + var6[var20], (Timing)null, var1);
            var5[var20] = -1;
            this.stringMatrix.put(2, var20 + 1, "--", (Timing)null, var1);
            var4[var20] = false;
            this.stringMatrix.put(3, var20 + 1, "" + var4[var20], (Timing)null, var1);
        }

        this.lang.nextStep("Hilfsvariablen initialisieren");
        this.stringMatrix.unhighlightCellColumnRange(1, 1, this.stringMatrix.getNrCols() - 1, (Timing)null, var1);
        this.stringMatrix.unhighlightCellColumnRange(2, 1, this.stringMatrix.getNrCols() - 1, (Timing)null, var1);
        this.stringMatrix.unhighlightCellColumnRange(3, 1, this.stringMatrix.getNrCols() - 1, (Timing)null, var1);
        this.code.unhighlight(4);
        this.code.unhighlight(5);
        this.code.unhighlight(6);
        this.code.unhighlight(7);
        this.code.unhighlight(8);
        this.code.unhighlight(9);
        this.code.highlight(10);
        this.stringMatrix.highlightCellRowRange(0, 3, this.start + 1, (Timing)null, var1);
        this.stringMatrix.put(1, this.start + 1, "0", (Timing)null, var1);
        var6[this.start] = 0;
        this.kommentar.setText("//dist von " + this.graph.getNodeLabel(this.start) + " auf 0 setzten", (Timing)null, var1);
        this.lang.nextStep("Bearbeitung der Knoten");
        this.code.unhighlight(10);
        this.stringMatrix.unhighlightCellRowRange(0, 3, this.start + 1, (Timing)null, var1);
        var20 = 0;
        int var11 = 0;
        var18.declare("int", var2, "", "Aktuell bearbeiteter Knoten");
        var18.setGlobal(var2);
        boolean var13 = true;

        int var12;
        while((var12 = this.minVertex(var6, var4)) != -1) {
            ++var20;
            if (var13) {
                var13 = false;
            } else {
                this.nextNodeQuestion(var6, var4, var12);
            }

            this.code.highlight(13);
            this.kommentar.setText("//Nächsten Knoten beziehen (unbesuchter Knoten mit kleiner Distanz falls vorhaden)", (Timing)null, var1);
            this.lang.nextStep();
            this.graph.highlightNode(var12, (Timing)null, var1);
            var18.set(var2, "" + var12);
            var4[var12] = true;
            this.stringMatrix.put(3, var12 + 1, "" + var4[var12], (Timing)null, var1);
            this.stringMatrix.highlightCellRowRange(0, 3, var12 + 1, (Timing)null, var1);
            this.kommentar.setText("//Knoten " + this.graph.getNodeLabel(var12) + " als bearbeitet/besucht merken", (Timing)null, var1);
            this.code.unhighlight(13);
            this.code.highlight(14);
            this.lang.nextStep();
            this.code.unhighlight(14);
            this.code.highlight(16);
            this.kommentar.setText("//Nachbarn von " + this.graph.getNodeLabel(var12) + " beziehen", (Timing)null, var1);
            int[] var14 = this.getEdgesForNode(var12);

            int var15;
            for(var15 = 0; var15 < var14.length; ++var15) {
                this.graph.highlightEdge(var12, var15, (Timing)null, var1);
            }

            this.lang.nextStep();

            for(var15 = 0; var15 < var14.length; ++var15) {
                this.graph.unhighlightEdge(var12, var15, (Timing)null, var1);
            }

            this.code.unhighlight(16);
            this.code.highlight(17);
            this.code.highlight(18);
            this.kommentar.setText("//über alle Nachbarn( ausgehenden Kanten) iterieren, if zum ignorieren der 0en in der Adjazenzmatrix", (Timing)null, var1);
            this.lang.nextStep();
            this.code.unhighlight(17);
            this.code.unhighlight(18);
            var18.openContext();
            var18.declare("int", var3, "", "Aktuell betrachteter Nachbar");

            for(var15 = 0; var15 < var14.length; ++var15) {
                if (var14[var15] != 0) {
                    var18.set(var3, "" + var15);
                    ++var11;
                    this.kommentar.setText("//Distanz für " + this.graph.getNodeLabel(var15) + " über " + this.graph.getNodeLabel(var12), (Timing)null, var1);
                    this.code.highlight(19);
                    this.graph.highlightEdge(var12, var15, (Timing)null, var1);
                    int var16 = var6[var12] + var14[var15];
                    this.lang.nextStep();
                    this.code.unhighlight(19);
                    this.code.highlight(20);
                    this.code.highlight(24);
                    if (var6[var15] <= var16 && var6[var15] != -1) {
                        this.kommentar.setText("//Werte von " + this.graph.getNodeLabel(var15) + " bleiben unverändert, da " + var16 + ">" + var6[var15], (Timing)null, var1);
                        this.lang.nextStep();
                    } else {
                        this.code.highlight(21);
                        this.code.highlight(22);
                        this.code.highlight(23);
                        this.kommentar.setText("//Werte für " + this.graph.getNodeLabel(var15) + " aktualisieren, da " + var16 + "<" + var6[var15], (Timing)null, var1);
                        var6[var15] = var16;
                        this.stringMatrix.put(1, var15 + 1, "" + var6[var15], (Timing)null, var1);
                        var5[var15] = var12;
                        this.stringMatrix.put(2, var15 + 1, "" + this.graph.getNodeLabel(var12), (Timing)null, var1);
                        var4[var15] = false;
                        this.stringMatrix.put(3, var15 + 1, "" + var4[var15], (Timing)null, var1);
                        this.lang.nextStep();
                        this.code.unhighlight(21);
                        this.code.unhighlight(22);
                        this.code.unhighlight(23);
                    }

                    this.code.unhighlight(20);
                    this.code.unhighlight(24);
                    this.graph.unhighlightEdge(var12, var15, (Timing)null, var1);
                }
            }

            var18.closeContext();
            this.graph.unhighlightNode(var12, (Timing)null, var1);
            this.stringMatrix.unhighlightCellRowRange(0, 3, var12 + 1, (Timing)null, var1);
        }

        this.stringMatrix.highlightCellColumnRange(0, 0, this.stringMatrix.getNrCols() - 1, (Timing)null, var1);
        this.stringMatrix.highlightCellColumnRange(1, 0, this.stringMatrix.getNrCols() - 1, (Timing)null, var1);
        this.stringMatrix.highlightCellColumnRange(2, 0, this.stringMatrix.getNrCols() - 1, (Timing)null, var1);
        this.stringMatrix.highlightCellColumnRange(3, 0, this.stringMatrix.getNrCols() - 1, (Timing)null, var1);

        int var21;
        for(var21 = 0; var21 < 29; ++var21) {
            this.code.highlight(var21);
        }

        for(var21 = 0; var21 < this.graph.getSize(); ++var21) {
            if (var5[var21] != -1 || var21 == this.start) {
                this.graph.highlightNode(var21, (Timing)null, var1);
            }
        }

        this.kommentar.setText("//Fertig, für jeden erreichbaren Knoten wurde ein kürzester Pfad gefunden", (Timing)null, var1);
        this.lang.nextStep("Fazit");
        this.code.hide();

        for(var21 = 0; var21 < var5.length; ++var21) {
            if (var5[var21] != -1) {
                this.graph.highlightEdge(var5[var21], var21, (Timing)null, var1);
            }
        }

        SourceCodeProperties var22 = new SourceCodeProperties();
        var22.set("font", new Font("SansSerif", 0, 18));
        SourceCode var23 = this.lang.newSourceCode(this.code.getUpperLeft(), "end", (DisplayOptions)null, var22);
        var23.addCodeLine("Für das Ergebniss wurden: ", (String)null, 0, (Timing)null);
        var23.addCodeLine(var11 + " Kanten bearbeitet", (String)null, 1, (Timing)null);
        var23.addCodeLine(var20 + " Knoten bearbeitet", (String)null, 1, (Timing)null);
        this.kommentar.hide();
    }

    private int minVertex(int[] var1, boolean[] var2) {
        int var3 = 2147483647;
        int var4 = -1;

        for(int var5 = 0; var5 < var1.length; ++var5) {
            if (!var2[var5] && var1[var5] < var3 && var1[var5] >= 0) {
                var3 = var1[var5];
                var4 = var5;
            }
        }

        return var4;
    }

    private void initQuestionGroups() {
        QuestionGroupModel var1 = new QuestionGroupModel("1", 3);
        this.lang.addQuestionGroup(var1);
        QuestionGroupModel var2 = new QuestionGroupModel("2", 4);
        this.lang.addQuestionGroup(var2);
    }

    private void nextNodeQuestion(int[] var1, boolean[] var2, int var3) {
        MultipleChoiceQuestionModel var4 = new MultipleChoiceQuestionModel("chooseNextNode" + this.nextQuestionCount);
        ++this.nextQuestionCount;
        var4.setPrompt("Welcher Knoten wird als nächstes bearbeitet?");
        boolean var5 = false;

        for(int var6 = 0; var6 < var1.length; ++var6) {
            byte var8 = 0;
            StringBuilder var7 = new StringBuilder("Diese Antwort ist");
            if (var6 == var3) {
                var7.append(" richtig");
                var8 = 10;
            } else if (var1[var6] == var1[var3]) {
                var7.append(" fast richtig. Die Entfernung ist die kleinste, jedoch existiert ein Knoten mi der selben Entfernung der früher gefunden wird.\n");
                var8 = 5;
            } else {
                var7.append(" leider falsch. Die Richtige antwort wäre der Knoten " + this.graph.getNodeLabel(var3) + " gewesen");
            }

            if (var2[var6]) {
                var7.append("\n Außerdem ist der Knoten " + this.graph.getNodeLabel(var6) + " (noch) als bereits bearbeitet markiert");
                var8 = 0;
            }

            var4.addAnswer(this.graph.getNodeLabel(var6), var8, var7.toString());
        }

        var4.setGroupID("1");
        this.lang.addMCQuestion(var4);
    }

    private int[] getEdgesForNode(int var1) {
        int[] var2 = this.graph.getEdgesForNode(var1);
        if (!this.directed) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
                if (var3 != var1) {
                    var2[var3] += this.graph.getAdjacencyMatrix()[var3][var1];
                }
            }
        }

        return var2;
    }

    public String getName() {
        return "Dijkstra";
    }

    public String getAlgorithmName() {
        return "Dijkstra";
    }

    public String getAnimationAuthor() {
        return "Konstantin Ramig";
    }

    public String getDescription() {
        return "Der Algorithmus, Dijkstra, berechnet zu einem gegebenen Knoten\ndie Entfernung zu allen anderen Koten des Graphen\nund merkt sich diesen kuerzesten Pfad. Dazu wird jeweils eine\nDistanz und der Vorgaengerknoten gespeichert. Ausserdem\nmerkt sich der Algorithmus welche Knoten er bereits besucht hat\nNun Iteriert er ueber alle unbesuchten Knoten. Als\nnaechster Knoten wird dabei immer der mit der geringsten Distanz\ngewaehlt.";
    }

    public String getCodeExample() {
        return "public int[] dijkstra (WeightedGraph G, int s) {\n\tint [] dist = new int [G.size()];\n\tint [] pred = new int [G.size()]; \n\tboolean [] visited = new boolean [G.size()];\n\n\tfor (int i=0; i<dist.length; i++) {\n\t\tdist[i] = Integer.MAX_VALUE;\n\t}\n\tdist[s] = 0;\n\n\tfor (int i=0; i<dist.length; i++) {\n\t\tint next = minVertex (dist, visited);\n\t\tvisited[next] = true;\n\n\t\tint [] n = G.neighbors (next);\n\t\tfor (int j=0; j<n.length; j++) {\n\t\t\tint v = n[j];\n\t\t\tint d = dist[next] + G.getWeight(next,v);\n\t\t\tif (dist[v] > d) {\n\t\t\t\tdist[v] = d;\n\t\t\t\tpred[v] = next;\n\t\t\t\tvisited[v]= false;\"\n\t\t\t}\n\t\t}\n\t} \n}";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(8);
    }

    public String getOutputLanguage() {
        return "Java";
    }

    public static void main(String[] args) {
        Generator generator = new Dijkstra();
        Animal.startGeneratorWindow(generator);
    }
}
