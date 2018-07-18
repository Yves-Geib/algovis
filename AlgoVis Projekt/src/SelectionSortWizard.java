/*
 * SelectionSortWizard.java
 * Hannah Drews, Yves Geib, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import java.awt.Color;
import java.awt.Font;
import algoanim.properties.ArrayProperties;
import algoanim.properties.TextProperties;

public class SelectionSortWizard implements Generator {
    private Language lang;
    private Color color;
    private ArrayProperties array;
    private TextProperties text;
    private int[] intArray;
    private Font font;

    public void init(){
        lang = new AnimalScript("Selection Sort", "Hannah Drews, Yves Geib", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        color = (Color)primitives.get("color");
        array = (ArrayProperties)props.getPropertiesByName("array");
        text = (TextProperties)props.getPropertiesByName("text");
        intArray = (int[])primitives.get("intArray");
        font = (Font)primitives.get("font");
        
        return lang.toString();
    }

    public String getName() {
        return "Selection Sort";
    }

    public String getAlgorithmName() {
        return "Selection Sort ";
    }

    public String getAnimationAuthor() {
        return "Hannah Drews, Yves Geib";
    }

    public String getDescription(){
        return "Selection Sort ist ein Sortieralgorithmus mit quadratischer Laufzeit."
 +"\n"
 +"Er sortiert ein Eingabearry der Länge n nach aufsteigender Reihenfolge."
 +"\n"
 +"Im Eingabearray wird hierzu zunächst das Maximum bestimmt und dieses"
 +"\n"
 +"mit dem Element an Position n-1 getauscht. Ist das Element an Position n-1 das Maximum"
 +"\n"
 +"findet keine Vertauschung statt. Dieser Vorgang wiederholt sich mit den restlichen Position.";
    }

    public String getCodeExample(){
        return "public void selectionSort(int[] array)"
 +"\n"
 +"{"
 +"\n"
 +"  int i, j, minIndex;"
 +"\n"
 +"  for (i=0; i<array.length - 1; i++)"
 +"\n"
 +"  {"
 +"\n"
 +"    minIndex = i;"
 +"\n"
 +"    for (j=i+1; j<array.length; j++)"
 +"\n"
 +"      if (array[j] < array[minIndex])"
 +"\n"
 +"        minIndex = j;"
 +"\n"
 +"    swap(array, i, minIndex);"
 +"\n"
 +"  }"
 +"\n"
 +"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    public static void main(String[] args) {
        Language lang = new AnimalScript("Selection Sort", "Hannah Drews, Yves Geib", 800, 600);
        Text text = lang.newText(new Coordinates(300, 200), "test", "testText", null);

        Animal.startAnimationFromAnimalScriptCode(lang.toString());
    }
}