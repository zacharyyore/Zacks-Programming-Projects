/* Zachary Yore 
   Dr. Steinberg
   COP3503 Fall 2022
   Programming Assignment 5
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import java.util.Comparator;

public class Railroad { // rail road class

    private final String FileName;
    private final int NumTracks;

    public Railroad(int numTracks, String fileName) { // rail road constructor
        this.FileName = fileName;
        this.NumTracks = numTracks;
    }

    public class pairs implements Comparable { // class for edges 

        public int cost; // cost of edge
        public String a; // point a 
        public String b; // point b

        public pairs(String a, String b, int cost) {
            this.a = a;
            this.b = b;
            this.cost = cost;
        }

        @Override // used for sorting
        public String toString() {
            return "[ a=" + a + ", b=" + b + ", cost=" + cost + "]";
        }

        @Override // used for sorting
        public int compareTo(Object o) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost///SystemFile//System/Templates/Classes/Code/GeneratedMethodBody
        }
    }

    class subset { // class for subset to represent "parent node" 

        int parent;
        int rank; // rank used for union by rank
    };

    int find(subset subsets[], int i) { // find technique using path compression
        if (subsets[i].parent != i) {
            subsets[i].parent = find(subsets, subsets[i].parent);
        }
        return subsets[i].parent;
    }

    void Union(subset subsets[], int x, int y) { // union by rank method
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);
        if (subsets[xroot].rank < subsets[yroot].rank) {
            subsets[xroot].parent = yroot;
        } else if (subsets[xroot].rank > subsets[yroot].rank) {
            subsets[yroot].parent = xroot;
        } else {
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }

    public int StringToInt(String[] arr, String x) { // converts a array of strings to the index a specific string is at in array
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(x)) {
                return i;
            }
        }
        return 0;
    }

    public String printString(String a, String b, int c) { // prints strings based lexographically
        if (a.compareTo(b) < 1) {
            return a + "---" + b + "\t" + "$" + c + "\n";
        }
        return b + "---" + a + "\t" + "$" + c + "\n";
    }

    public String[][] readFile() { // reads txt file and puts data into a 2d array
        try {
            int count = 0;
            String strArray[][] = new String[NumTracks][3];
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(FileName));
            String line = reader.readLine();
            while (line != null) {
                strArray[count] = line.split(" ");
                count += 1;
                line = reader.readLine();
            }
            return strArray;
        } catch (IOException e) {
        }
        return null;
    }

    public String buildRailroad() {
        String starr[][] = readFile(); // 2d array from txt file
        String vertArr[][] = new String[NumTracks][2];
        HashSet<String> set = new HashSet<>();
        String a = " ";
        String b = " ";
        int cost = 0;
        for (int i = 0; i < NumTracks; i++) {
            for (int j = 0; j < 2; j++) {
                vertArr[i][j] = starr[i][j]; //creates vert arr for just edges
                set.add(starr[i][j]); // adds to set
            }
        }
        String arr[] = new String[set.size()]; // 
        int num[] = new int[set.size()];
        int x = 0;
        for (String ele : set) {
            num[x] = x;
            arr[x++] = ele;
        }
        int numVert = (int) Arrays.stream(vertArr).flatMap(Arrays::stream).distinct().count(); // creates value for num of vert
        ArrayList<pairs> edges = new ArrayList<>(); // creates arraylist of class
        for (int i = 0; i < NumTracks; i++) {
            a = starr[i][0];
            b = starr[i][1];
            cost = Integer.parseInt(starr[i][2]);
            edges.add(new pairs(a, b, cost));
        }
        edges.sort(Comparator.comparing(pairs -> pairs.cost)); // sorts edge class on cost
        ArrayList<pairs> results = new ArrayList<>();

        int i = 0;
        subset subsets[] = new subset[numVert]; // creates subset values
        for (i = 0; i < numVert; ++i) {
            subsets[i] = new subset();
        }

        for (int v = 0; v < numVert; ++v) { // sets values for parents for subset
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }
        i = 0;
        int e = 0;
        int minimumCost = 0;
        String strPrint = "";
        String StrPrint = "";
        while (e < numVert - 1) { // loop that last verticies - 1
            int y = find(subsets, StringToInt(arr, edges.get(i).a)); // calls formating method
            int z = find(subsets, StringToInt(arr, edges.get(i).b));
            if (y != z) {
                results.add(edges.get(i));
                Union(subsets, y, z);
                e += 1;

            }
            i += 1;
        }
        for (i = 0; i < e; ++i) {
            strPrint = printString(results.get(i).a, results.get(i).b, results.get(i).cost);
            StrPrint = StrPrint + strPrint;
            minimumCost += results.get(i).cost;
        }
        return StrPrint + "The cost of the railroad is $" + minimumCost + "."; // returns string 
    }
}
