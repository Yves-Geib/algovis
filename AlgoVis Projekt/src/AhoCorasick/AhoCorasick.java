/*
 * AhoCorasick.java
 * Hannah Drews, Yves Geib, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package ahoCorasick;

import algoanim.primitives.*;
import algoanim.primitives.Point;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.*;

import algoanim.primitives.generators.Language;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class AhoCorasick implements Generator {
    private Language lang;
    private TextProperties textProps;
    private ArrayProperties arrayProps;
    private ArrayMarkerProperties arrayMarkerProps;
    private GraphProperties graphProps;
    private SourceCode failureCode, code, code1, code2, code3;
    private Text patterns, dictionaryText, keywordHe, keywordShe, keywordHis, keywordHers, output;
    private Text he, she, his, hers;
    //private String[] dictionary;
    private SourceCodeProperties sourceCodeProps;
    private Graph graph;
    private StringArray dictionary;
    private Locale locale;
    private HashMap<String, String> text;

    public AhoCorasick() {this(Locale.US);}

    public AhoCorasick(Locale language) {
        this.locale = language;
        this.text = new HashMap();
        if(this.locale == Locale.GERMANY || this.locale == Locale.GERMAN) {
            this.text.put("firstmulti", "Für welchen Unix-Command bildet der Aho-Corasick Algorithmus die Basis?");
            this.text.put("answer0_0", "ls");
            this.text.put("answer0_1", "grep");
            this.text.put("answer0_2", "sed");
            this.text.put("answer0_3", "fgrep");
            this.text.put("feedback0_0", "Leider falsch. Die richtige Antwort wäre fgrep zur Suche nach Wörtern in einem bestimmten Text.");
            this.text.put("feedback0_1", "Leider falsch. Grep benutzt einen etwas langsameren Suchalgorithmus als Aho-Corasick zur Suche.");
            this.text.put("feedback0_2", "Leider falsch. Die richtige Antwort wäre fgrep zur Suche nach Wörtern in einem bestimmten Text.");
            this.text.put("feedback0_3", "Richtig! Das Kommando arbeitet durch die Verwendung des Aho-Corasick Algorithmus sehr effizient und empfiehlt sich immer dann, wenn besonders schnell große Datenmengen durchsucht werden sollen.");

            this.text.put("secondmulti", "Was ist eine Fehler-Funktion?");
            this.text.put("answer1_0", "Der Output, wenn keines der Wörter aus dem Trie (Patterns) im Eingabetext gefunden wurde.");
            this.text.put("answer1_1", "Sie gibt den Zustand im Trie nach einem Missmatch aus.");
            this.text.put("answer1_2", "Sie repräsentiert alle Wörter, die nicht im Eingabetext gefunden wurden. ");
            this.text.put("feedback1_0", "Leider falsch. Versuch es noch einmal!");
            this.text.put("feedback1_1", "Richtig! Die Fehlerfunktion wird im Preprocessing gesetzt und findet für einen Zustand s den längsten richtigen Suffix, der ein richtiges Präfix eines Patterns ist.");
            this.text.put("feedback1_2", "Leider falsch. Versuch es noch einmal!");

            this.text.put("lastmulti", "Welche Laufzeit hat der Algorithmus Aho Corasick?");
            this.text.put("answer2_0", "O(n*k+m)");
            this.text.put("answer2_1", "O(n*m)");
            this.text.put("answer2_2", "O(n+m+z)");
            this.text.put("feedback2_0", "Falsch! Richtig wäre O(n+m+z), also wesentlich schneller.");
            this.text.put("feedback2_1", "Falsch! Richtig wäre O(n+m+z), also wesentlich schneller.");
            this.text.put("feedback2_2", "Richtig! Dabei ist n die Länge des Textes, m das Traversieren des Tries und z die Anzahl der Patterns.");






        }
        else {
            this.text.put("firstmulti", "For which Unix command is the Aho-Corasick algorithm the basis?");
            this.text.put("answer0_0", "ls");
            this.text.put("answer0_1", "grep");
            this.text.put("answer0_2", "sed");
            this.text.put("answer0_3", "fgrep");
            this.text.put("feedback0_0", "Wrong. The correct answer would be fgrep to search for words in a particular text.");
            this.text.put("feedback0_1", "Unfortunately wrong. Grep uses a slightly slower search algorithm than Aho-Corasick.");
            this.text.put("feedback0_2", "Wrong. The correct answer would be fgrep to search for words in a particular text.");
            this.text.put("feedback0_3", "Right! The command works very efficient using the Aho-Corasick algorithm and is recommended whenever large amounts of data need to be scanned very quickly.");

            this.text.put("secondmulti", "What is an error function?");
            this.text.put("answer1_0", "The output, if none of the words from the trie in the input text was found.");
            this.text.put("answer1_1", "It represents the state of the trie after a mismatch.");
            this.text.put("answer1_2", "It represents all of the words that were not found in the input text.");
            this.text.put("feedback1_0", "Unfortunately wrong. Try it again!");
            this.text.put("feedback1_1", "That's right! The error function is set in preprocessing and finds the longest correct suffix for a state s, which is a correct prefix of a pattern.");
            this.text.put("feedback1_2", "Unfortunately wrong. Try it again!");

            this.text.put("lastmulti", "What is the runtime of the algorithm Aho Corasick?");
            this.text.put("answer2_0", "O(n*k+m)");
            this.text.put("answer2_1", "O(n*m)");
            this.text.put("answer2_2", "O(n+m+z)");
            this.text.put("feedback2_0", "Wrong. The correct answer is O(n+m+z), which is much faster.");
            this.text.put("feedback2_1", "Wrong. The correct answer is O(n+m+z), which is much faster.");
            this.text.put("feedback2_2", "Right! n is the length of the text, m is the traversal of the trie and z is the number of patterns.");



        }


    }

    public void init() {
        this.lang = new AnimalScript("Aho-Corasick algorithm", "Hannah Drews, Yves Geib", 800, 600);
        this.lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

        this.lang.setInteractionType(1024);

        this.textProps = (TextProperties)props.getPropertiesByName("textProps");
        this.arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProps");
        this.arrayMarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarkerProps");
        //this.dictionary = (String[])primitives.get("dictionary");
        this.sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        this.graph = (Graph)primitives.get("trie");

        //init and start
        String[] arr = {"he", "she", "hers", "his"};
        String text = "ushers";
        int k = arr.length;

        searchWords(arr, k, text);

        ahoAlgo(this.graph);

        this.lang.finalizeGeneration();
        return lang.toString();
    }

    public void ahoAlgo(Graph graph) {

        //TODO GRAPHPROPS IN XML FOR DYNAMIX
        GraphProperties graphProps = graph.getProperties();
        graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
        graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED); //TODO muss auskommentiert werden, damit es vom User änderbar ist
        //Build Graph with Nodes and Labels
        Node[] nodes = new Node[graph.getSize()];
        String[] nodenames = new String[graph.getSize()];

        for (int i = 0; i < graph.getSize(); i++) {
            System.out.println(Arrays.toString(graph.getAdjacencyMatrix()[i]));
            nodes[i] = graph.getNode(i);
            nodenames[i] = graph.getNodeLabel(i);
        }

        // Graph
        graph = this.lang.newGraph(graph.getName(), graph.getAdjacencyMatrix(), nodes, nodenames, (DisplayOptions) null, graphProps);
        graph.hideEdge(5, 2, null, null); //failed transaction edge
        graph.hideEdge(9, 0, null, null); //failed transaction edge
        graph.hideEdge(3, 0, null, null); //failed transaction edge

        int position = graph.getPositionForNode(graph.getNode(0)); //Position of first node of graph
        System.out.println(position);

        // Search words in array, fixed/not changeable
        textProps.set("color", Color.GRAY);
        textProps.set("font", new Font("SansSerif", 1, 14));
        patterns = this.lang.newText(new Offset(175, 20, graph, "N"), "Patterns with which the graph gets built: ", "patterns", null, textProps);

        textProps.set("color", Color.BLACK);
        textProps.set("font", new Font("SansSerif", 1, 14));
        keywordHe = this.lang.newText(new Offset(0, 5, patterns, "SW"), "he", "keywordHe", null, textProps);
        keywordShe = this.lang.newText(new Offset(33, 0, keywordHe, "N"), "she", "keywordShe", null, textProps);
        keywordHis = this.lang.newText(new Offset(38, 0, keywordShe, "N"), "his", "keywordHis", null, textProps);
        keywordHers = this.lang.newText(new Offset(35, 0, keywordHis, "N"), "hers", "keywordHers", null, textProps);

        //patterns next to the nodes, as in: end state in automaton
        textProps.set("font", new Font("SansSerif", 0, 14));
        textProps.set("color", Color.GRAY);
        he = this.lang.newText(new Offset(20, 0, this.graph.getNode(2), "N"), "{he}", "he", null, textProps);
        she = this.lang.newText(new Offset(-7, 25, this.graph.getNode(5), "SW"), "{she}", "she", null, textProps);
        his = this.lang.newText(new Offset(-5, 25, this.graph.getNode(7), "SW"), "{his}", "his", null, textProps);
        hers = this.lang.newText(new Offset(-10, 25, this.graph.getNode(9), "SW"), "{hers}", "hers", null, textProps);

        //description for the dictionary, since difference between patterns and dictionary is confusing
        textProps.set("color", Color.GRAY);
        textProps.set("font", new Font("SansSerif", 1, 14));
        dictionaryText = this.lang.newText(new Offset(0, 75, patterns, "SW"), "Dictionary in which the patterns get searched: ", "dictionaryText", null, textProps);

        //Dictionary text, shows the input tring TODO changeable by user
        String[] input = {"u", "s", "h", "e", "r", "s",};


        arrayProps.set("fillColor", Color.WHITE);
        arrayProps.set("color", Color.BLACK);
        arrayProps.set("cellHighlight", Color.RED);
        dictionary = this.lang.newStringArray(new Offset(0, 10, dictionaryText, "SW"), input, "dictionary", null, arrayProps);
        //Show the output trings
        textProps.set("color", Color.BLACK);
        textProps.set("font", new Font("SansSerif", 1, 14));
        output = this.lang.newText(new Offset(0, 75, dictionary, "SW"), "Patterns found in dictionary: {} ", "output", null, textProps);

        //Code shown while traversing in graph
        sourceCodeProps.set("color", Color.RED);
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 1, 12));
        code = this.lang.newSourceCode(new Offset (-480, 0, graph.getNode(0), "SW"), "sourceCode", null, sourceCodeProps);

        lang.nextStep("Find letter 'U'");
        dictionary.highlightCell(0, null, null); //Letter U rot
        code.addCodeLine("// code for regular graph traversal/pattern searching", null, 0, null);
        code.changeColor("color", Color.BLACK, null, null);
        code.addCodeLine("look for current letter at children of root", null, 0, null);

        lang.nextStep("current letter not found in children");
        code.changeColor("color", Color.BLACK, null, null);
        code.addCodeLine("if there are none", null, 1, null);
        graph.highlightEdge(0, 1, null, null);
        graph.highlightEdge(0, 3, null, null);

        lang.nextStep("Highlight edge O-O");
        code.addCodeLine("use failed transaction line and restart at root", null, 2, null);
        graph.unhighlightEdge(0, 1, null, null);
        graph.unhighlightEdge(0, 3, null, null);
        graph.highlightEdge(0, 0, null, null); //Edge O-O rot

        lang.nextStep("Find next letter: 'S'");
        graph.unhighlightEdge(0, 0, null, null); //Edge O-O schwarz
        dictionary.unhighlightCell(0, null, null); //Letter U schwarz
        dictionary.highlightCell(1, null, null); //Letter S rot
        code.changeColor("color", Color.BLACK, null, null);
        code1 = this.lang.newSourceCode(new Offset (-480, 0, graph.getNode(0), "SW"), "sourceCode", null, sourceCodeProps);
        code1.addCodeLine("// code for regular graph traversal/pattern searching", null, 0, null);
        code1.changeColor("color", Color.BLACK, null, null);
        code1.addCodeLine("look for current letter at children of root", null, 0, null);

        lang.nextStep("Traverse in graph");
        graph.highlightEdge(0, 3, null, null); //Edge O - S rot
        graph.highlightNode(3, null, null); //Node S rot
        code1.addCodeLine("if there are none", null, 1, null);
        code1.addCodeLine("use failed transaction line and restart at root", null, 2, null);
        code1.changeColor("color", Color.BLACK, null, null);
        code1.addCodeLine("if there is one", null, 1, null);

        lang.nextStep();
        graph.unhighlightEdge(0, 3, null, null); //Edge O-S schwarz
        graph.setNodeHighlightFillColor(3, Color.GREEN, null, null); //Grüne Farbe
        dictionary.setHighlightFillColor(1, Color.GREEN, null, null); //Grüne Farbe
        graph.highlightNode(3, null, null); //Node S grün
        dictionary.highlightCell(1, null, null); //Letter S grün
        code1.changeColor("color", Color.BLACK, null, null);
        code1.addCodeLine("move to that child", "move", 2, null);
        code1.highlight("move");

        lang.nextStep("Find next letter: 'H'");
        graph.unhighlightNode(3, null, null); //Node S schwarz
        dictionary.highlightCell(2, null, null); //Letter H rot
        code1.changeColor("color", Color.BLACK, null, null);
        code1.addCodeLine("look for next letter in current branch", null, 2, null);

        lang.nextStep();

        code1.changeColor("color", Color.BLACK, null, null);
        code1.addCodeLine("if there is none", "failure", 2, null);

        lang.nextStep();
        graph.showEdge(3, 0, null, null); //show failfunction Edge
        graph.setEdgeHighlightPolyColor(3, 0, Color.BLUE, null, null); //switch failfunction Edge highlightColor to blue
        graph.hideEdgeWeight(3, 0, null, null); //hide failfunction EdgeWeight
        graph.highlightEdge(3, 0, null, null); //show failfunction Edge in blue
        graph.hideEdge(0, 3, null, null); //hide original edge so they don't overlap. Needs to be shown later again.
        code1.addCodeLine("/*", null, 3, null);
        code1.addCodeLine("  This is always the case for", null, 3, null);
        code1.addCodeLine("  the direct children of root.", null, 3, null);
        code1.addCodeLine("  But with traversing further", null, 3, null);
        code1.addCodeLine("  into the tree, every node has", null, 3, null);
        code1.addCodeLine("  a so called failed transaction", null, 3, null);
        code1.addCodeLine("  which will appear later on.", null, 3, null);
        code1.addCodeLine("*/", null, 3, null);
        code1.changeColor("color", Color.BLACK, null, null);
        code1.addCodeLine("use failed transaction line back to root", "failure1", 3, null);

        lang.nextStep("Traverse in graph");
        graph.hideEdge(3, 0, null, null);
        graph.showEdge(0, 3, null, null);
        graph.highlightEdge(3, 4, null, null); //Edge S-H rot
        graph.highlightNode(4, null, null); //Node H rot
        code1.changeColor("color", Color.BLACK, null, null);
        code1.addCodeLine("if there is one", null, 2, null);


        lang.nextStep();
        graph.unhighlightEdge(3, 4, null, null); //Edge S-H schwwarz
        graph.setNodeHighlightFillColor(4, Color.GREEN, null, null); //Grüne Farbe
        dictionary.setHighlightFillColor(2, Color.GREEN, null, null); //Grüne Farbe
        graph.highlightNode(4, null, null); //Node H grün
        dictionary.highlightCell(2, null, null); //Letter H grün
        code.hide();
        code1.changeColor("color", Color.BLACK, null, null);
        code1.addCodeLine("move to that child", "move1", 3, null);
        code1.highlight("move1");


        lang.nextStep("Find next letter: 'E'");
        graph.unhighlightNode(4, null, null); //Node H schwarz
        dictionary.highlightCell(3, null, null); //Letter E rot
        //code.unhighlight("move1");
        code1.changeColor("color", Color.BLACK, null, null);
        code2 = this.lang.newSourceCode(new Offset (-480, 0, graph.getNode(0), "SW"), "sourceCode", null, sourceCodeProps);
        code2.addCodeLine("// code for regular graph traversal/pattern searching", null, 0, null);
        code2.addCodeLine("look for current letter at children of root", null, 0, null);
        code2.addCodeLine("if there are none", null, 1, null);
        code2.addCodeLine("use failed transaction line and restart at root", null, 2, null);
        code2.addCodeLine("if there is one", null, 1, null);
        code2.addCodeLine("move to that child", null, 2, null);
        code2.changeColor("color", Color.BLACK, null, null);
        code2.addCodeLine("look for next letter in current branch", null, 2, null);

        lang.nextStep("Traverse in graph");
        graph.highlightEdge(4, 5, null, null); //Edge H-E rot
        graph.highlightNode(5, null, null); //Node E rot
        code2.addCodeLine("if there is none", "failure", 2, null);
        code2.addCodeLine("/*", null, 3, null);
        code2.addCodeLine("  This is always the case for", null, 3, null);
        code2.addCodeLine("  the direct children of root.", null, 3, null);
        code2.addCodeLine("  But with traversing further", null, 3, null);
        code2.addCodeLine("  into the tree, every node has", null, 3, null);
        code2.addCodeLine("  a so called failed transaction", null, 3, null);
        code2.addCodeLine("  which will appear later on.", null, 3, null);
        code2.addCodeLine("*/", null, 3, null);
        code2.addCodeLine("use failed transaction line back to root", null, 3, null);
        code2.changeColor("color", Color.BLACK, null, null);
        code2.addCodeLine("if there is one", null, 2, null);

        lang.nextStep();
        graph.unhighlightEdge(4, 5, null, null); //Edge H-E schwarz
        graph.setNodeHighlightFillColor(5, Color.GREEN, null, null); //Grüne Farbe
        dictionary.setHighlightFillColor(3, Color.GREEN, null, null); //Grüne Farbe
        graph.highlightNode(5, null, null); //Node E grün
        dictionary.highlightCell(3, null, null); //Letter E grün
        code1.hide();
        code2.changeColor("color", Color.BLACK, null, null);
        code2.addCodeLine("move to that child", "move2", 3, null);
        code2.highlight("move2");

        lang.nextStep("Keyword 'she' was found");
        keywordShe.changeColor("color", Color.GREEN, null, null);
        she.changeColor("color", Color.GREEN, null, null);
        textProps.set("color", Color.BLACK);
        textProps.set("font", new Font("SansSerif", 0, 14));
        Text keywordSheFound = this.lang.newText(new Offset(0, 30, dictionary, "SW"), "Word: " + keywordShe.getText() + " appears in dictionary from 1 to 3.", "keywordSheFound", null, textProps);
        code2.addCodeLine("if it is the last letter of a pattern", "patternFound", 3, null);
        code2.highlight("patternFound");



        lang.nextStep("Add 'she' to output Strings");
        textProps.set("color", Color.BLACK);
        textProps.set("font", new Font("SansSerif", 1, 14));
        output.setText("Patterns found in dictionary: {she} ", null, null);
        code2.addCodeLine("add the pattern to the output", "addPattern", 4, null);
        code2.highlight("addPattern");

        lang.nextStep("Continue with algorithm");
        keywordSheFound.hide();
        keywordShe.changeColor("color", Color.BLACK, null, null);
        she.changeColor("color", Color.GRAY, null, null);
        dictionary.highlightCell(4, null, null); //Letter R rot

        code2.changeColor("color", Color.BLACK, null, null);
        code3 = this.lang.newSourceCode(new Offset (-480, 0, graph.getNode(0), "SW"), "sourceCode", null, sourceCodeProps);
        code3.addCodeLine("// code for regular graph traversal/pattern searching", null, 0, null);
        code3.changeColor("color", Color.BLACK, null, null);
        code3.addCodeLine("look for current letter at children of root", null, 0, null);
        code3.addCodeLine("if there are none", null, 1, null);
        code3.addCodeLine("use failed transaction line and restart at root", null, 2, null);
        code3.addCodeLine("if there is one", null, 1, null);
        code3.addCodeLine("move to that child", null, 2, null);
        code3.changeColor("color", Color.BLACK, null, null);
        code3.addCodeLine("look for next letter in current branch", null, 2, null);

        lang.nextStep("second MultipleChoice");
        MultipleChoiceQuestionModel secondmulti = new MultipleChoiceQuestionModel("Failed transactions");
        secondmulti.setPrompt((String)this.text.get("secondmulti"));
        secondmulti.addAnswer((String)this.text.get("answer1_0"), 1, (String)this.text.get("feedback1_0"));
        secondmulti.addAnswer((String)this.text.get("answer1_1"), 1, (String)this.text.get("feedback1_1"));
        secondmulti.addAnswer((String)this.text.get("answer1_2"), 1, (String)this.text.get("feedback1_2"));
        this.lang.addMCQuestion(secondmulti);

        //FEHLERFUNKTION
        lang.nextStep("Fail function");
        code2.hide();
        code3.hide();
        graph.showEdge(5, 2, null, null); //show failfunction Edge
        graph.setEdgeHighlightPolyColor(5, 2, Color.BLUE, null, null); //switch failfunction Edge highlightColor to blue
        graph.hideEdgeWeight(5, 2, null, null); //hide failfunction EdgeWeight
        graph.highlightEdge(5, 2, null, null); //show failfunction Edge in blue

        //code shown while in failed transaction
        sourceCodeProps.set("color", Color.BLUE);
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 1, 12));
        failureCode = this.lang.newSourceCode(new Offset (-480, 0, graph.getNode(0), "SW"), "sourceCodeFailfunction", null, sourceCodeProps);
        failureCode.addCodeLine("// code for failed transactions graph traversing/substring searching", null, 0, null);
        failureCode.changeColor("color", Color.BLACK, null, null);
        failureCode.addCodeLine("if (next letter is not found in current branch", null, 0, null);
        failureCode.addCodeLine("&& child is not direct child of root)", null, 2, null);
        failureCode.addCodeLine("move along failed transaction", null, 1, null);

        lang.nextStep("comment");
        failureCode.addCodeLine("/*", null, 1, null);
        failureCode.addCodeLine("  Instead of going back to root every time", null, 1, null);
        failureCode.addCodeLine("  and restarting the search, ", null, 1, null);
        failureCode.addCodeLine("  this way is more efficient by searching", null, 1, null);
        failureCode.addCodeLine("  for a substring of the current string.", null, 1, null);
        failureCode.addCodeLine("  This is done by moving to the root-nearest node", null, 1, null);
        failureCode.addCodeLine("  with the same letter.", null, 1, null);
        failureCode.addCodeLine("*/", null, 1, null);
        failureCode.changeColor("color", Color.GRAY, null, null);

        lang.nextStep("Find current letter in higher branch");
        graph.setNodeHighlightFillColor(2, Color.BLUE, null, null);
        graph.highlightNode(2, null, null);
        failureCode.addCodeLine("if there is none", null, 2, null);
        failureCode.addCodeLine("move back to root", null, 3, null);
        failureCode.changeColor("color", Color.BLACK, null, null);
        failureCode.addCodeLine("if there is one", null, 2, null);
        failureCode.addCodeLine("check if a substring is found", null, 3, null);

        lang.nextStep();
        he.changeColor("color", Color.BLUE, null, null);
        failureCode.changeColor("color", Color.BLACK, null, null);
        failureCode.addCodeLine("if there is a substring of previous string", null, 4, null);
        failureCode.addCodeLine("//{he} is a substring of {she}", null, 4, null);

        lang.nextStep();
        failureCode.changeColor("color", Color.BLACK, null, null);
        failureCode.addCodeLine("add that substring to the output", "addingHe", 5, null);

        lang.nextStep("Found keyword 'he'");
        failureCode.highlight("addingHe");
        graph.setNodeHighlightFillColor(1, Color.GREEN, null, null); //Farbe grün
        graph.setNodeHighlightFillColor(2, Color.GREEN, null, null); //Farbe grün
        graph.highlightNode(1, null, null); //Letter H grün
        he.changeColor("color", Color.GREEN, null, null); //{he} grün
        dictionary.unhighlightCell(1, null, null); //Letter S schwarz
        textProps.set("color", Color.BLACK);
        textProps.set("font", new Font("SansSerif", 0, 14));
        keywordHe.changeColor("color", Color.GREEN, null, null); //keyword He grün
        Text keywordHeFound = this.lang.newText(new Offset(0, 30, dictionary, "SW"), "Word: " + keywordHe.getText() + " appears in dictionary from 2 to 3, as a substring from 'she'.", "keywordHeFound", null, textProps);

        lang.nextStep("Füge He zu She hinzu: {he, she}");
        she.setText("{he, she}", null, null); //{He} zu {She} hinzugefügt
        she.changeColor("color", Color.GREEN, null, null); //{he, she} grün

        lang.nextStep("Add 'he' to output Strings");
        textProps.set("color", Color.BLACK);
        textProps.set("font", new Font("SansSerif", 1, 14));
        output.setText("Patterns found in dictionary: {he, she} ", null, null);

        lang.nextStep("Back to traversing graph for the next letter: 'R'");
        graph.unhighlightNode(1, null, null); //Letter H schwarz
        graph.setNodeHighlightFillColor(2, Color.BLUE, null, null); //Node E zurück zu blau
        keywordHeFound.hide();
        he.changeColor("color", Color.GRAY, null, null); //{he} schwarz
        she.changeColor("color", Color.GRAY, null, null); //{He, She} schwarz
        keywordHe.changeColor("color", Color.BLACK, null, null); //keywordHe schwarz
        failureCode.unhighlight("addingHe");
        failureCode.changeColor("color", Color.BLACK, null, null);
        failureCode.addCodeLine("Traverse further in graph", "traverseFurther", 2, null);

        lang.nextStep();
        graph.setNodeHighlightFillColor(2, Color.RED, null, null); //Node E zurück zu rot
        failureCode.hide();
        code.show();
        code.addCodeLine("if there is one", null, 1, null);
        code.changeColor("color", Color.BLACK, null, null);
        code.addCodeLine("look for next letter in current branch", null, 2, null);

        lang.nextStep("Traverse further in graph after failfunction");
        graph.unhighlightNode(2, null, null); //Node E from 'He' schwarz (von rot)
        graph.unhighlightNode(5, null, null); //Node E from 'She' schwarz (von grün)
        graph.hideEdge(5, 2, null, null); //hide failed transaction edge
        graph.highlightEdge(2, 8, null, null); //Edge E-R rot
        graph.highlightNode(8, null, null); //Node R rot
        code.addCodeLine("if there is none", "failure", 2, null);
        code.addCodeLine("use failed transaction line back to root", "failure1", 3, null);
        code.changeColor("color", Color.BLACK, null, null);
        code.addCodeLine("if there is one", null, 2, null);

        lang.nextStep("Found letter: 'R'");
        graph.unhighlightEdge(2, 8, null, null); //Edge E-R schwarz
        graph.unhighlightNode(2, null, null); //Node E schwarz
        graph.setNodeHighlightFillColor(8, Color.GREEN, null, null); //Farbe grün
        dictionary.setHighlightFillColor(4, Color.GREEN, null, null); //Farbe grün
        graph.highlightNode(8, null, null); //Node R grün
        dictionary.highlightCell(4, null, null); //Letter R grün
        code.changeColor("color", Color.BLACK, null, null);
        code.addCodeLine("move to that child", "move1", 3, null);
        code.highlight("move1");

        lang.nextStep("Find next letter 'S'");
        graph.unhighlightNode(8, null, null);
        dictionary.highlightCell(5, null, null);

        //Back to first code section
        code.changeColor("color", Color.BLACK, null, null);
        sourceCodeProps.set("color", Color.RED);
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 1, 12));
        code1 = this.lang.newSourceCode(new Offset (-480, 0, graph.getNode(0), "SW"), "sourceCode", null, sourceCodeProps);
        code1.addCodeLine("// code for regular graph traversal/pattern searching", null, 0, null);
        code1.addCodeLine("look for current letter at children of root", null, 0, null);
        code1.addCodeLine("if there are none", null, 1, null);
        code1.addCodeLine("use failed transaction line and restart at root", null, 2, null);
        code1.addCodeLine("if there is one", null, 1, null);
        code1.changeColor("color", Color.BLACK, null, null);
        code1.addCodeLine("look for next letter in current branch", null, 2, null);


        lang.nextStep("Traverse further in graph");
        graph.highlightNode(9, null, null);
        graph.highlightEdge(8, 9, null, null);
        code1.addCodeLine("if there is none", "failure", 2, null);
        code1.addCodeLine("use failed transaction line back to root", "failure1", 3, null);
        code1.changeColor("color", Color.BLACK, null, null);
        code1.addCodeLine("if there is one", null, 2, null);

        lang.nextStep("Found letter: 'S'");
        graph.unhighlightEdge(8, 9, null, null);
        graph.setNodeHighlightFillColor(9, Color.GREEN, null, null);
        dictionary.setHighlightFillColor(5, Color.GREEN, null, null);
        graph.highlightNode(9, null, null);
        dictionary.highlightCell(9, null, null);
        code1.changeColor("color", Color.BLACK, null, null);
        code1.addCodeLine("move to that child", "move1", 3, null);
        code1.highlight("move1");
        code.hide();


        lang.nextStep("Keyword 'hers' was found");
        keywordHers.changeColor("color", Color.GREEN, null, null);
        hers.changeColor("color", Color.GREEN, null, null);
        textProps.set("font", new Font("SansSerif", 0, 14));
        Text keywordHersFound = this.lang.newText(new Offset(0, 30, dictionary, "SW"), "Word: " + keywordHers.getText() + " appears in dictionary from 2 to 5.", "keywordHersFound", null, textProps);
        code1.addCodeLine("if it is the last letter of a pattern", "patternFound", 3, null);
        code1.highlight("patternFound");


        lang.nextStep("Add 'hers' to output Strings");
        textProps.set("color", Color.BLACK);
        textProps.set("font", new Font("SansSerif", 1, 14));
        output.setText("Patterns found in dictionary: {he, she, hers} ", null, null);
        code1.addCodeLine("add the pattern to the output", "addPattern", 4, null);
        code1.highlight("addPattern");

        lang.nextStep("last letter in patterns");
        keywordHersFound.hide();
        keywordHers.changeColor("color", Color.BLACK, null, null);
        hers.changeColor("color", Color.GRAY, null, null);
        code1.changeColor("color", Color.BLACK, null, null);
        code2 = this.lang.newSourceCode(new Offset (-480, 0, graph.getNode(0), "SW"), "sourceCode", null, sourceCodeProps);
        code2.addCodeLine("// code for regular graph traversal/pattern searching", null, 0, null);
        code2.addCodeLine("look for current letter at children of root", null, 0, null);
        code2.addCodeLine("if there are none", null, 1, null);
        code2.addCodeLine("use failed transaction line and restart at root", null, 2, null);
        code2.addCodeLine("if there is one", null, 1, null);
        code2.changeColor("color", Color.BLACK, null, null);
        code2.addCodeLine("look for next letter in current branch", null, 2, null);

        lang.nextStep();
        graph.setNodeHighlightFillColor(9, Color.RED, null, null);
        code2.addCodeLine("if there is none", "failure", 2, null);

        lang.nextStep("Failure function back to O(root)");
        graph.showEdge(9, 0, null, null);
        graph.setEdgeHighlightPolyColor(9, 0, Color.BLUE, null, null); //switch failfunction Edge highlightColor to blue
        graph.hideEdgeWeight(9, 0, null, null); //hide failfunction EdgeWeight
        graph.highlightEdge(9, 0, null, null); //show failfunction Edge in blue
        code2.addCodeLine("use failed transaction line back to root", "failure1", 3, null);

        lang.nextStep("back at root, no more dictionary letters left");
        graph.unhighlightNode(9, null, null);
        graph.setNodeHighlightFillColor(0, Color.BLUE, null, null);
        graph.highlightNode(0, null, null);
        code.hide();
        code1.hide();
        code2.hide();

        //code shown while in failed transaction
        sourceCodeProps.set("color", Color.BLUE);
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 1, 12));
        failureCode = this.lang.newSourceCode(new Offset (-480, 0, graph.getNode(0), "SW"), "sourceCodeFailfunction", null, sourceCodeProps);
        failureCode.addCodeLine("// code for failed transactions graph traversing/substring searching", null, 0, null);
        failureCode.addCodeLine("if (next letter is not found in current branch", null, 0, null);
        failureCode.addCodeLine("&& child is not direct child of root)", null, 2, null);
        failureCode.addCodeLine("move along failed transaction", null, 1, null);
        failureCode.addCodeLine("/*", null, 1, null);
        failureCode.addCodeLine("  Instead of going back to root every time", null, 1, null);
        failureCode.addCodeLine("  and restarting the search, ", null, 1, null);
        failureCode.addCodeLine("  this way is more efficient by searching", null, 1, null);
        failureCode.addCodeLine("  for a substring of the current string.", null, 1, null);
        failureCode.addCodeLine("  This is done by moving to the root-nearest node", null, 1, null);
        failureCode.addCodeLine("  with the same letter.", null, 1, null);
        failureCode.addCodeLine("*/", null, 1, null);
        failureCode.addCodeLine("if there is none", null, 2, null);
        failureCode.changeColor("color", Color.BLACK, null, null);
        failureCode.addCodeLine("move back to root", null, 3, null);

        lang.nextStep();
        graph.hideEdge(9, 0, null, null);
        failureCode.addCodeLine("if it is the end of dictionary", null, 3, null);
        for (int i = 0; i < dictionary.getLength(); i++) {
            dictionary.setHighlightFillColor(i, Color.BLUE, null, null);
            dictionary.highlightCell(i, null, null);
        }

        lang.nextStep();
        for (int i = 0; i < dictionary.getLength(); i++) {
            dictionary.setHighlightFillColor(i, Color.RED, null, null);
            dictionary.unhighlightCell(i, null, null);
        }
        graph.unhighlightNode(0, null, null);
        failureCode.changeColor("color", Color.BLACK, null, null);
        failureCode.addCodeLine("finish algorithm and show output", null, 4, null);
        output.changeColor("color", Color.BLUE, null, null);
        textProps.set("color", Color.BLACK);
        textProps.set("font", new Font("SansSerif", 0, 14));
        Text keywordHeFound1 = this.lang.newText(new Offset(0, 15, output, "SW"), "Word '" + keywordHe.getText() + "' appears in dictionary from 2 to 3", "keywordHeFound", null, textProps);
        Text keywordHersFound1 = this.lang.newText(new Offset(0, 10, keywordHeFound1, "SW"), "Word '" + keywordHers.getText() + "' appears in dictionary from 2 to 5.", "keywordHersFound", null, textProps);
        Text keywordSheFound1 = this.lang.newText(new Offset(0, 10, keywordHersFound1, "SW"), "Word '" + keywordShe.getText() + "' appears in dictionary from 1 to 3.", "keywordSheFound", null, textProps);



        lang.nextStep("first MultipleChoice");
        failureCode.hide();
        MultipleChoiceQuestionModel firstmulti = new MultipleChoiceQuestionModel("Unix-command");
        firstmulti.setPrompt((String)this.text.get("firstmulti"));
        firstmulti.addAnswer((String)this.text.get("answer0_0"), 0, (String)this.text.get("feedback0_0"));
        firstmulti.addAnswer((String)this.text.get("answer0_1"), 0, (String)this.text.get("feedback0_1"));
        firstmulti.addAnswer((String)this.text.get("answer0_2"), 0, (String)this.text.get("feedback0_2"));
        firstmulti.addAnswer((String)this.text.get("answer0_3"), 1, (String)this.text.get("feedback0_3"));
        this.lang.addMCQuestion(firstmulti);

        lang.nextStep("third MultipleChoice");
        MultipleChoiceQuestionModel lastmulti = new MultipleChoiceQuestionModel("Runtime");
        lastmulti.setPrompt((String)this.text.get("lastmulti"));
        lastmulti.addAnswer((String)this.text.get("answer2_0"), 0, (String)this.text.get("feedback2_0"));
        lastmulti.addAnswer((String)this.text.get("answer2_1"), 0, (String)this.text.get("feedback2_1"));
        lastmulti.addAnswer((String)this.text.get("answer2_2"), 0, (String)this.text.get("feedback2_2"));
        this.lang.addMCQuestion(lastmulti);

        lang.nextStep();





    }


    public String getName() {
        return "Aho-Corasick algorithm";
    }

    public String getAlgorithmName() {
        return "Aho-Corasick string matching algorithm";
    }

    public String getAnimationAuthor() {
        return "Hannah Drews, Yves Geib";
    }

    public String getDescription() {
        return "Aho-Corasick is a string-matching algorithm that locates elements of a finite set of strings within an input text."
                + "\n";
    }

    public String getCodeExample() {
        return "CodeExample";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return this.locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }


    //R- alphabet size
    //M - Max number of states [= Sum of the length of all keywords]

    //out[] - output function - Bit i in this mask is 1 iff the word
    //                          with index i appears when machine enters
    //                          this state

    //f[] - failure function

    //g[][] - goto function(Trie)

    private int R = 26, M = 500;
    private int[] out, f;
    private int[][] g;


    //Initialize all values in
    //failure function to -1
    //goto function to -1
    //out function to 0 [default when created]
    public void initialize() {
        out = new int[M];
        f = new int[M];
        g = new int[M][R];
        Arrays.fill(f, -1);
        for (int[] row : g) {
            Arrays.fill(row, -1);
        }
    }

    public int buildMatchingMachine(String[] arr, int k) {

        //Initialize step is called
        initialize();
        //Initially we just have 0 states
        int states = 1;

        //Construct values for goto function same as
        //building a Trie for arr[]
        for (int i = 0; i < k; i++) {

            int currentState = 0;
            char[] word = arr[i].toCharArray();

            for (int j = 0; j < word.length; j++) {

                int ch = word[j] - 'a';

                //Allocate a new node (create a new state) if a
                // node for ch doesn't exist
                if (g[currentState][ch] == -1) {
                    g[currentState][ch] = states++;
                }

                currentState = g[currentState][ch];
            }

            //Add current word in output function
            out[currentState] |= (1 << i);

        }

        //For all characters which don't have an edge from root
        //(or state 0) in Trie, add a goto edge to state 0 itself

        for (int ch = 0; ch < R; ch++) {
            if (g[0][ch] == -1) {
                g[0][ch] = 0;
            }
        }

        //let's build the failure function now
        //Failure function is computed in breadth first order
        //using a queue
        Queue<Integer> q = new LinkedList<Integer>();

        //Iterate over every possible input
        for (int ch = 0; ch < R; ch++) {
            //All nodes of depth 1 have failure function value as 0
            //For example, in above diagram we move to 0 from states 1 and 3
            if (g[0][ch] != 0) {
                f[g[0][ch]] = 0;
                q.add(g[0][ch]);
            }
        }

        //Now queue has states 1 and 3
        while (!q.isEmpty()) {
            //Remove the front state from queue
            int state = q.poll();

            //For the removed state, find failure function for all
            //those characters for which goto function is not defined
            for (int ch = 0; ch < R; ch++) {

                //If goto function is defined
                if (g[state][ch] != -1) {

                    //Find failure state of removed state
                    int failure = f[state];

                    //Find the deepest node labelled by proper suffix
                    //of string from root to current state
                    while (g[failure][ch] == -1) {
                        failure = f[failure];
                    }

                    failure = g[failure][ch];
                    f[g[state][ch]] = failure;

                    //Merge output values
                    out[g[state][ch]] |= out[failure];

                    //Insert the next level node (of Trie) in queue
                    q.add(g[state][ch]);
                }
            }
        }

        return states;
    }

    //Returns the next state machine will transform to using goto
    //and failure functions
    //currentState - The current state of the machine.
    //               [Must be between 0 and no. of states -1, inclusive]
    //nextInput - The next character that enters the machine

    public int findNextState(int currentState, char nextInput) {

        int ch = nextInput - 'a';

        //If goto function is not defined use failure function
        while (g[currentState][ch] == -1) {
            currentState = f[currentState];
        }
        return g[currentState][ch];
    }

    //Finds all occurrences of all array words in text
    public void searchWords(String[] arr, int k, String text) {

        //Preprocess patterns
        //Build machine with goto, failure and output functions
        buildMatchingMachine(arr, k);

        //Initialize the current state
        int currentState = 0;

        //Traverse the text through the built machine to find all
        //occurrences of words in arr[]
        for (int i = 0; i < text.length(); i++) {

            currentState = findNextState(currentState, text.charAt(i));

            //If match not found, move to next state
            if (out[currentState] == 0) {
                continue;
            }

            //Match found, print all matching words of arr[] using
            //output function
            for (int j = 0; j < k; j++) {

                if ((out[currentState] & (1 << j)) == (1 << j)) {

                    System.out.println("Word " + arr[j] + " appears from " + (i - arr[j].length() + 1) + " to " + i);
                }

            }

        }

    }


    public static void main(String[] args) {

        AhoCorasick aho = new AhoCorasick();
        Animal.startGeneratorWindow(aho);

    }


}