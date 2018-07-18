package Graph;

import java.util.LinkedList;

public class GraphAdjacencyList {

    // number of nodes
    int n;
    // number of edges
    int e;
    // LinkedList to represent Adjacency List of Edges
    LinkedList<Integer> adjListArray[];

    public GraphAdjacencyList(int n) {
        this.n = n;

        // define the size of array as number of nodes
        adjListArray = new LinkedList[n];

        // create new list for each node such that adjacent nodes can be stored
        for (int i = 0; i < n; i++) {
            adjListArray[i] = new LinkedList<>();
        }
    }

    // Method to add an edge to an undirected GraphAdjacencyList
    public static void addEdge(GraphAdjacencyList graphAdjacencyList, int src, int dest) {

        // add edge from source to dest
        graphAdjacencyList.adjListArray[src].addFirst(dest);

        // for undirected GraphAdjacencyList, add edge from dest to source too
        graphAdjacencyList.adjListArray[dest].addFirst(src);
    }

    // Function to print the list
    static void printGraph(GraphAdjacencyList graphAdjacencyList) {
        for (int i = 0; i < graphAdjacencyList.n; i++) {
            System.out.println("Adjacency list of vertex " + i);
            System.out.print("head");
            for (Integer pCrawl : graphAdjacencyList.adjListArray[i]) {
                System.out.print(" -> " + pCrawl);
            }
            System.out.println("\n");
        }
    }

    public static void main(String args[]) {
        // create the graphAdjacencyList given in above figure
        int n = 4;
        GraphAdjacencyList graphAdjacencyList = new GraphAdjacencyList(n);
        addEdge(graphAdjacencyList, 0, 1);
        addEdge(graphAdjacencyList, 0, 2);
        addEdge(graphAdjacencyList, 0, 3);
        addEdge(graphAdjacencyList, 1, 3);
        addEdge(graphAdjacencyList, 2, 3);

        // print the adjacency list representation of
        // the above graphAdjacencyList
        printGraph(graphAdjacencyList);
    }
}
