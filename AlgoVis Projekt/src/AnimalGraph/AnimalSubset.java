package AnimalGraph;

import animal.main.Animal;

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
