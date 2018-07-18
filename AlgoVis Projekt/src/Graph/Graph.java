package Graph;

import Graph.Edge;
import generators.misc.arithconvert.sugar.Sub;

import java.util.Random;

public class Graph {

    Subset testSet = new Subset();

    int v; // number of vertices (nodes)
    int e; // number of edges
    Edge[] edgeArray; // array to store all edges

    public Graph(int v, int e) {
        this.v = v;
        this.e = e;
        edgeArray = new Edge[e];
        for (int i = 0; i < e; ++i) {
            edgeArray[i] = new Edge(); // for number of edges e create a new edge and store it in edgeArray
        }
    }

    // Kargers Min Cut Algorithm
    int kargersMinCut(Graph graph) {
        // get given Graph
        int nrOfVertices = graph.v;
        int nrOfEdges = graph.e;
        // here should be a single edge...
        Random r = new Random();

        // allocate memory for creating v subsets
        Subset[] subset = new Subset[nrOfVertices];

        // create v subsets of single elements
        for (int v = 0; v < nrOfVertices; ++v) {
            subset[v] = new Subset(0,0);
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
            int subset1 = testSet.find(subset, edgeArray[i].src);
            int subset2 = testSet.find(subset, edgeArray[i].dest);

            // if the vertices belong to the same subset, this edge is not considered
            if (subset1 == subset2) {
                continue;
            }

            // else contract the edges (combine the subsets and combine
            // the node of the edge into one)
            else {
                System.out.println("Contracting edge" + edgeArray[i].src + edgeArray[i].dest);
                // number of Vertices is one less
                vertices--;
                testSet.union(subset, subset1, subset2);
            }
        }

        // there are now two subsets left in the contracted graph
        // so the results are the edges between the components
        int cutEdges = 0;

        for (int i = 0; i < nrOfEdges; i++) {
            int subset1 = testSet.find(subset, edgeArray[i].src);
            int subset2 = testSet.find(subset, edgeArray[i].dest);
            if (subset1 != subset2) {
                cutEdges++;
            }
        }
        return cutEdges;
    }

    public static void main(String[] args) {

        /*
        0------1
        | \    |
        |   \  |
        |     \|
        2------3
         */

        Graph testGraph = new Graph(4,5);

        // add edge 0-1
        testGraph.edgeArray[0].src = 0;
        testGraph.edgeArray[0].dest = 1;

        // add edge 0-2
        testGraph.edgeArray[1].src = 0;
        testGraph.edgeArray[1].dest = 2;

        // add edge 0-3
        testGraph.edgeArray[2].src = 0;
        testGraph.edgeArray[2].dest = 3;

        // add edge 1-3
        testGraph.edgeArray[3].src = 1;
        testGraph.edgeArray[3].dest = 3;

        // add edge 2-3
        testGraph.edgeArray[4].src = 2;
        testGraph.edgeArray[4].dest = 3;

        System.out.println("Kargers minimum cut for given graph is:" + testGraph.kargersMinCut(testGraph));
    }
}
