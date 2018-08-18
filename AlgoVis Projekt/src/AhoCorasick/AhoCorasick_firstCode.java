package ahoCorasick;

import java.util.*;

public class AhoCorasick {


    //R- alphabet size
    //M - Max number of states [= Sum of the length of all keywords]

    //out[] - output function - Bit i in this mask is 1 iff the word
    //                          with index i appears when machine enters
    //                          this state

    //f[] - failure function

    //g[][] - goto function(Trie)

    public static int R = 26,M = 500;
    public static int[] out,f;
    public static int[][] g;

    //Initialize all values in
    //failure function to -1
    //goto function to -1
    //out function to 0 [default when created]
    public static void init(){
        out = new int[M];f = new int[M];g = new int[M][R];
        Arrays.fill(f, -1);
        for(int[] row:g){
            Arrays.fill(row, -1);
        }
    }

    public static int buildMatchingMachine(String[] arr,int k){

        //Initialize step is called
        init();

        //Initially we just have 0 states
        int states=1;

        //Construct values for goto function same as
        //building a Trie for arr[]
        for(int i=0;i<k; i++){

            int currentState=0;
            char[] word = arr[i].toCharArray();

            for(int j=0; j<word.length;j++){

                int ch = word[j]-'a';

                //Allocate a new node (create a new state) if a
                // node for ch doesn't exist
                if(g[currentState][ch]==-1){
                    g[currentState][ch] = states++;
                }

                currentState = g[currentState][ch];
            }

            //Add current word in output function
            out[currentState] |= (1<<i);

        }

        //For all characters which don't have an edge from root
        //(or state 0) in Trie, add a goto edge to state 0 itself

        for(int ch=0; ch<R; ch++){
            if(g[0][ch]==-1){
                g[0][ch]=0;
            }
        }

        //let's build the failure function now
        //Failure function is computed in breadth first order
        //using a queue
        Queue<Integer> q = new LinkedList<Integer>();

        //Iterate over every possible input
        for(int ch=0;ch<R;ch++){
            //All nodes of depth 1 have failure function value as 0
            //For example, in above diagram we move to 0 from states 1 and 3
            if(g[0][ch]!=0){
                f[g[0][ch]]=0;
                q.add(g[0][ch]);
            }
        }

        //Now queue has states 1 and 3
        while(!q.isEmpty()){
            //Remove the front state from queue
            int state = q.poll();

            //For the removed state, find failure function for all
            //those characters for which goto function is not defined
            for(int ch=0; ch<R; ch++){

                //If goto function is defined
                if(g[state][ch]!=-1){

                    //Find failure state of removed state
                    int failure = f[state];

                    //Find the deepest node labelled by proper suffix
                    //of string from root to current state
                    while(g[failure][ch]==-1){
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

    public static int findNextState(int currentState, char nextInput){

        int ch= nextInput-'a';

        //If goto function is not defined use failure function
        while(g[currentState][ch]==-1){
            currentState = f[currentState];
        }

        return g[currentState][ch];
    }

    //Finds all occurrences of all array words in text
    public static void searchWords(String[] arr, int k, String text){

        //Preprocess patterns
        //Build machine with goto, failure and output functions
        buildMatchingMachine(arr,k);

        //Initialize the current state
        int currentState=0;

        //Traverse the text through the built machine to find all
        //occurrences of words in arr[]
        for(int i=0; i<text.length(); i++){

            currentState = findNextState(currentState,text.charAt(i));

            //If match not found, move to next state
            if(out[currentState]==0) continue;

            //Match found, print all matching words of arr[] using
            //output function
            for(int j=0; j<k; j++){

                if((out[currentState] & (1<<j))==(1<<j)){
                    System.out.println("Word "+arr[j]+" appears from "+(i-arr[j].length()+1)+" to "+ i);
                }

            }

        }

    }


    public static void main(String[] args){
        String[] arr = {"he","she","hers","his"};
        String text = "ahishers";
        int k = arr.length;

        searchWords(arr,k,text);
    }

}