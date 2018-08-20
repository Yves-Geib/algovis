/*
 * AhoCorasick.java
 * Hannah Drews, Yves Geib, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package AhoCorasick;

import algoanim.primitives.Graph;
import algoanim.primitives.Point;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
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

public class AhoCorasick implements Generator {
    private Language lang;
    private TextProperties textProps;
    private ArrayProperties arrayProps;
    private ArrayMarkerProperties arrayMarkerProps;
    private GraphProperties graphProps;
    private Text patterns, dictionaryText, keywordHe, keywordShe, keywordHis, keywordHers, output;
    private Text he, she, his, hers;
    //private String[] dictionary;
    private SourceCodeProperties sourceCodeProps;
    private Graph graph;
    private StringArray dictionary;
    private Point pointer;

    public AhoCorasick() {}

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

        ahoAlgo(this.graph);
        //init and start
        String[] arr = {"he", "she", "hers", "his"};
        String text = "ushers";
        int k = arr.length;

        searchWords(arr, k, text);

        //this.lang.finalizeGeneration();
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
        graph.hideEdge(5, 2, null, null); //failure function edge
        graph.hideEdge(9, 0, null, null); //failure function edge
        int position = graph.getPositionForNode(graph.getNode(0)); //Position of first node of graph

        // Search words in array, fixed/not changeable
        textProps.set("color", Color.GRAY);
        textProps.set("font", new Font("SansSerif", 1, 14));
        patterns = this.lang.newText(new Offset(175, 20, graph, "N"), "Patterns: ", "patterns", null, textProps);

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

        lang.nextStep("Find letter 'U'");
        dictionary.highlightCell(0, null, null); //Letter U rot

        lang.nextStep("Highlight edge O-O");
        graph.highlightEdge(0, 0, null, null); //Edge O-O rot

        lang.nextStep("Find next letter: 'S'");
        graph.unhighlightEdge(0, 0, null, null); //Edge O-O schwarz
        dictionary.unhighlightCell(0, null, null); //Letter U schwarz
        dictionary.highlightCell(1, null, null); //Letter S rot

        lang.nextStep("Traverse in graph");
        graph.highlightEdge(0, 3, null, null); //Edge O - S rot
        graph.highlightNode(3, null, null); //Node S rot

        lang.nextStep();
        graph.unhighlightEdge(0, 3, null, null); //Edge O-S schwarz
        graph.setNodeHighlightFillColor(3, Color.GREEN, null, null); //Grüne Farbe
        dictionary.setHighlightFillColor(1, Color.GREEN, null, null); //Grüne Farbe
        graph.highlightNode(3, null, null); //Node S grün
        dictionary.highlightCell(1, null, null); //Letter S grün

        lang.nextStep("Find next letter: 'H'");
        graph.unhighlightNode(3, null, null); //Node S schwarz
        dictionary.highlightCell(2, null, null); //Letter H rot

        lang.nextStep("Traverse in graph");
        graph.highlightEdge(3, 4, null, null); //Edge S-H rot
        graph.highlightNode(4, null, null); //Node H rot
        lang.nextStep();
        graph.unhighlightEdge(3, 4, null, null); //Edge S-H schwwarz
        graph.setNodeHighlightFillColor(4, Color.GREEN, null, null); //Grüne Farbe
        dictionary.setHighlightFillColor(2, Color.GREEN, null, null); //Grüne Farbe
        graph.highlightNode(4, null, null); //Node H grün
        dictionary.highlightCell(2, null, null); //Letter H grün

        lang.nextStep("Find next letter: 'E'");
        graph.unhighlightNode(4, null, null); //Node H schwarz
        dictionary.highlightCell(3, null, null); //Letter E rot

        lang.nextStep("Traverse in graph");
        graph.highlightEdge(4, 5, null, null); //Edge H-E rot
        graph.highlightNode(5, null, null); //Node E rot
        lang.nextStep();
        graph.unhighlightEdge(4, 5, null, null); //Edge H-E schwarz
        graph.setNodeHighlightFillColor(5, Color.GREEN, null, null); //Grüne Farbe
        dictionary.setHighlightFillColor(3, Color.GREEN, null, null); //Grüne Farbe
        graph.highlightNode(5, null, null); //Node E grün
        dictionary.highlightCell(3, null, null); //Letter E grün

        lang.nextStep("Keyword 'she' was found");
        keywordShe.changeColor("color", Color.GREEN, null, null);
        she.changeColor("color", Color.GREEN, null, null);
        textProps.set("color", Color.BLACK);
        textProps.set("font", new Font("SansSerif", 0, 14));
        Text keywordSheFound = this.lang.newText(new Offset(0, 30, dictionary, "SW"), "Word: " + keywordShe.getText() + " appears in dictionary from 1 to 3.", "keywordSheFound", null, textProps);

        lang.nextStep("Add 'she' to output Strings");
        textProps.set("color", Color.BLACK);
        textProps.set("font", new Font("SansSerif", 1, 14));
        output.setText("Patterns found in dictionary: {she} ", null, null);

        lang.nextStep("Continue with algorithm");
        keywordSheFound.hide();
        keywordShe.changeColor("color", Color.BLACK, null, null);
        she.changeColor("color", Color.GRAY, null, null);
        //Node E bleibt so lange grün, solange er mit der failfunction zu tun hat.
        //graph.unhighlightNode(5, null, null); //Node E schwarz

        lang.nextStep();
        dictionary.highlightCell(4, null, null); //Letter R rot

        //FEHLERFUNKTION
        lang.nextStep("Fail function");
        graph.showEdge(5, 2, null, null); //show failfunction Edge
        graph.setEdgeHighlightPolyColor(5, 2, Color.BLUE, null, null); //switch failfunction Edge highlightColor to blue
        graph.hideEdgeWeight(5, 2, null, null); //hide failfunction EdgeWeight
        graph.highlightEdge(5, 2, null, null); //show failfunction Edge in blue
        lang.nextStep();
        graph.highlightNode(2, null, null); //Letter E rot

        lang.nextStep("Found keyword 'he'");
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
        graph.setNodeHighlightFillColor(2, Color.RED, null, null); //Node E zurück zu rot
        keywordHeFound.hide();
        he.changeColor("color", Color.GRAY, null, null); //{he} schwarz
        she.changeColor("color", Color.GRAY, null, null); //{He, She} schwarz
        keywordHe.changeColor("color", Color.BLACK, null, null); //keywordHe schwarz

        lang.nextStep("Traverse further in graph after failfunction");
        graph.unhighlightNode(2, null, null); //Node E from 'He' schwarz (von rot)
        graph.unhighlightNode(5, null, null); //Node E from 'She' schwarz (von grün)
        graph.hideEdge(5, 2, null, null); //hide failure function edge
        graph.highlightEdge(2, 8, null, null); //Edge E-R rot
        graph.highlightNode(8, null, null); //Node R rot

        lang.nextStep("Found letter: 'R'");
        graph.unhighlightEdge(2, 8, null, null); //Edge E-R schwarz
        graph.unhighlightNode(2, null, null); //Node E schwarz
        graph.setNodeHighlightFillColor(8, Color.GREEN, null, null); //Farbe grün
        dictionary.setHighlightFillColor(4, Color.GREEN, null, null); //Farbe grün
        graph.highlightNode(8, null, null); //Node R grün
        dictionary.highlightCell(4, null, null); //Letter R grün

        lang.nextStep("Find next letter 'S'");

        graph.unhighlightNode(8, null, null);
        dictionary.highlightCell(5, null, null);

        lang.nextStep("Traverse further in graph");
        graph.highlightNode(9, null, null);
        graph.highlightEdge(8, 9, null, null);

        lang.nextStep("Found letter: 'S'");
        graph.unhighlightEdge(8, 9, null, null);
        graph.setNodeHighlightFillColor(9, Color.GREEN, null, null);
        dictionary.setHighlightFillColor(5, Color.GREEN, null, null);
        graph.highlightNode(9, null, null);
        dictionary.highlightCell(9, null, null);

        lang.nextStep("Keyword 'hers' was found");
        keywordHers.changeColor("color", Color.GREEN, null, null);
        hers.changeColor("color", Color.GREEN, null, null);
        textProps.set("font", new Font("SansSerif", 0, 14));
        Text keywordHersFound = this.lang.newText(new Offset(0, 30, dictionary, "SW"), "Word: " + keywordHers.getText() + " appears in dictionary from 2 to 5.", "keywordHersFound", null, textProps);

        lang.nextStep("Add 'hers' to output Strings");
        textProps.set("color", Color.BLACK);
        textProps.set("font", new Font("SansSerif", 1, 14));
        output.setText("Patterns found in dictionary: {he, she, hers} ", null, null);

        lang.nextStep("Failure function back to O(root)");
        keywordHersFound.hide();
        keywordHers.changeColor("color", Color.BLACK, null, null);
        hers.changeColor("color", Color.GRAY, null, null);
        graph.showEdge(9, 0, null, null);
        graph.setEdgeHighlightPolyColor(9, 0, Color.BLUE, null, null); //switch failfunction Edge highlightColor to blue
        graph.hideEdgeWeight(9, 0, null, null); //hide failfunction EdgeWeight
        graph.highlightEdge(9, 0, null, null); //show failfunction Edge in blue

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
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    /*

    Bevor wir das vergessen:

    if (dictionary[i] == searchedWords[irgendein char]
     */



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