/* Zachary Yore
   Dr. Steinberg
   COP3503 Fall 2022
   Programming Assignment 3
 */
import java.io.File;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class GreedyChildren {

    private Integer[] greedyArr;
    private Integer[] sweetnessArr;
    private int numChild;
    private int numSweet;
    private int angryNum;
    private String greedy;
    private String sweet;
    private int happy;
    private int happyChilds;

    public GreedyChildren(int candy, int numChild, String greedyFile, String sweetFile) { // constructor method
        this.angryNum = angryNum;
        this.greedyArr = greedyArr;
        this.numSweet = candy;
        this.numChild = numChild;
        this.sweetnessArr = sweetnessArr;
        this.sweet = sweetFile;
        this.greedy = greedyFile;
        this.happyChilds = 0;
    }

    public Integer[] read(String fileName, int size) { // reads charcters from txt files
        try {
            File f = new File(fileName);
            Integer arr[] = new Integer[size];
            Scanner s1 = new Scanner(f);
            for (int i = 0; i < arr.length; i++) {
                arr[i] = s1.nextInt();
            }
            Arrays.sort(arr, Collections.reverseOrder()); // sorts array from greatest to least
            return arr;
        } catch (Exception e) {
            System.out.println("File not found!");
        }
        return null;
    }

    public final int greedyCandy() { // method that calculates number of happy and angry childs using greedy
        greedyArr = read(greedy, numChild); // calls read method
        sweetnessArr = read(sweet, numSweet);
        List<Integer> greedyList = new LinkedList<>();
        List<Integer> sweetList = new LinkedList<>();
        greedyList.addAll(Arrays.asList(greedyArr)); // turning array to linked list
        sweetList.addAll(Arrays.asList(sweetnessArr));
        int f = numChild - 1;
        int i = 0;
        int j = 0;
        while (i <= f && j <= f) {
            if (sweetList.get(j) >= greedyList.get(i)) {
                happyChilds += 1;
            }
            if (j + 1 < f) {
                if (sweetList.get(j) < greedyList.get(i)) {
                    if (sweetList.get(0) < greedyList.get(0)) {
                        j = 1;
                    }
                    if (sweetList.get(j - 1) >= greedyList.get(i)) {
                        j -= 1;
                    }
                }
            }
            i += 1;
            j += 1;
        }
        if (sweetList.get(0) < greedyList.get(0)) {
            happyChilds += 1;
        }

        return happyChilds;
    }

    public void display() { // display function that prints the number of happy and angry childs
        int angry = numChild - happyChilds;
        System.out.println("There are " + happyChilds + " happy children.");
        System.out.println("There are " + angry + " angry children.");
    }
}
