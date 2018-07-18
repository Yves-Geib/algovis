import algoanim.primitives.IntArray;
import algoanim.primitives.generators.Language;

public class UebungSelectionSort {

    /**
     * The concrete language object used for creating output
     */
    private Language lang;

    /**
     * Default constructor
     *
     * @param l
     *          the conrete language object used for creating output
     */
    public UebungSelectionSort(Language l) {
        // Store the language object
        lang = l;
        // This initializes the step mode. Each pair of subsequent steps has to
        // be divdided by a call of lang.nextStep();
        lang.setStepMode(true);
    }

    private void SelectionSort(IntArray array) {

        int maxPos = 0;
        int max = 0;

        for (int i = array.getLength()-1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (array.getData(j) > array.getData(j+1)) {
                    max = array.getData(j+1);
                    maxPos = j+1;
                }
            }
            if (max > array.getData(i)) {
                int tmp = array.getData(i);
                array.put(i, max, null, null);
                array.put(maxPos, tmp, null, null);
            }
        }
    }
}
