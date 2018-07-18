package Graph;

public class Subset {
    int parent;
    int rank;

    public Subset() {
    }

    // constructor of subset
    public Subset(int parent, int rank) {
        this.parent = parent;
        this.rank = rank;
    }

    // find the subset of an element i
    // return only the root node of the subset in which the node is in
    int find(Subset subsetArray[], int i) {
        if (subsetArray[i].parent != i) {
            subsetArray[i].parent = find(subsetArray, subsetArray[i].parent);
        }
        return subsetArray[i].parent;
    }

    // union of two subsets (union by rank means, that the
    // "heavier" one becomes the parent of the other)
    void union(Subset subsetArray[], int x, int y) {
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
