/*
 * AhoCorasick.java
 * Hannah Drews, Yves Geib, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package AhoCorasick;

import algoanim.primitives.Graph;
import algoanim.properties.*;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.util.*;

import algoanim.primitives.generators.Language;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class AhoCorasick implements Generator {
    private Language lang;
    private TextProperties textProps;
    private ArrayProperties arrayProps;
    private ArrayMarkerProperties arrayMarkerProps;
    private String[] inputText;
    private SourceCodeProperties sourceCodeProps;
    private Graph graph;

    public AhoCorasick() {}

    public void init() {
        this.lang = new AnimalScript("Aho-Corasick algorithm", "Hannah Drews, Yves Geib", 800, 600);
        this.lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

        this.lang.setInteractionType(1024);

        textProps = (TextProperties) props.getPropertiesByName("textProps");
        arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
        arrayMarkerProps = (ArrayMarkerProperties) props.getPropertiesByName("arrayMarkerProps");
        inputText = (String[]) primitives.get("inputText");
        sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps");
        graph = (Graph) primitives.get("trie");


        GraphProperties graphProps = this.graph.getProperties();
        Node[] var1 = new Node[this.graph.getSize()];
        String[] var2 = new String[this.graph.getSize()];

        for(int var3 = 0; var3 < this.graph.getSize(); ++var3) {
            System.out.println(Arrays.toString(this.graph.getAdjacencyMatrix()[var3]));
            var1[var3] = this.graph.getNode(var3);
            var2[var3] = this.graph.getNodeLabel(var3);
        }

        this.graph = this.lang.newGraph("graph", this.graph.getAdjacencyMatrix(), var1, var2, (DisplayOptions)null, graphProps);

        //init and start
        String[] arr = {"he", "she", "hers", "his"};
        String text = "ahishers";
        int k = arr.length;

        searchWords(arr, k, text);

        this.lang.finalizeGeneration();
        return lang.toString();
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
            if (out[currentState] == 0) continue;

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