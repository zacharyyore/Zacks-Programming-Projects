package cachesimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class CacheSimulator {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        //main method
        CacheSimulator simulator = new CacheSimulator();
        //creates a new chache Simulator
        simulator.displayResults(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), args[4]);
        //calls the display method that goes through and prints results
    }
    static private double TR; // Total Request
    static private double wNum; // Number of writes
    static private double rNum;// Number of reads
    static private double mNum;//Number of misses
    static private int WBP;//Write Back Policy
    static private int RP;//Replacement Policy
    public static final int BS = 64;//Block size
    public static final int LRU = 0;
    public static final int WT = 0;//Write through
    public static final int WB = 1;//Write back

    public CacheSimulator() {//Cache simulator constructor
        this.TR = 0;
        this.wNum = 0;
        this.rNum = 0;
        this.mNum = 0;
        this.WBP = WBP;
        this.RP = RP;
    }

    public void displayResults(int cacheSize, int assoc, int RP, int WBP, String file) throws FileNotFoundException {
        //method that is called by main that goes through .t file and calculates results
        Scanner scanner = new Scanner(new File(file));
        Cache cache = new Cache(assoc, cacheSize);
        char OP; // signifies the operation R/W
        int sNum; // set Number
        String st; // String 
        BigInteger address; // represents the full address
        BigInteger tagBits; // represents the nums in the tag
        while (scanner.hasNextLine()) { //while there is still lines in the .t file
            st = scanner.nextLine();
            OP = st.charAt(0);
            address = new BigInteger(st.substring(4), 16); // formats the address
            tagBits = address.divide(BigInteger.valueOf(BS)); // calculates the tag bits
            sNum = (tagBits.mod(BigInteger.valueOf(cache.numSets))).intValue(); // calculates the set number
            TR += 1;
            if (OP == 'R') {
                cache.readCache(tagBits, sNum); // if operation is read call readCache method
            } else if (OP == 'W') { // if operation is write call writeCache Method
                cache.writeCache(tagBits, sNum);
            } else {
                System.out.println("Error!");
            }
        }
        // prints the number of writes, reads and miss rate
        System.out.println("Number of Write: " + wNum);
        System.out.println("Number of Reads: " + rNum);
        System.out.printf("Miss Rate: " + "%.6f\n", mNum / TR);

    }
// class that defines variables needed for block configuration
    static class cacheBlock {// constructor
        private BigInteger tagBits;
        // represents tag
        private boolean bad; 
        // represents if the bit is dirty / bad
        private boolean empty;
        // if is empty

        cacheBlock() {
            this.tagBits = BigInteger.valueOf(0);
            this.bad = false;
            this.empty = true;
        }
    }

    static class Cache {
        //class that represents the variables needed to represent the cache
        private int assoc;
        //represents the assoctivity
        private int numSets;
        //represents the number of sets
        private int size;
        cacheBlock[][] blocks;
        //calls block class and makes 2d array
        ArrayList<LinkedList<Integer>> data;
        // linked list to hold data

        Cache(int assoc, int cacheSize) { //cache constructor
            this.size = cacheSize;
            this.assoc = assoc;
            this.numSets = cacheSize / (assoc * BS);
            data = new ArrayList<>();
            blocks = new cacheBlock[numSets][assoc];
            for (int i = 0; i < blocks.length; i++) {
                for (int j = 0; j < blocks[i].length; j++) {
                    blocks[i][j] = new cacheBlock();
                }
            }
            int i = 0;
            while (i < numSets) {
                data.add(new LinkedList<>());
                i += 1;
            }
        }

        int free(int sNum) { 
            //method that finds a free block to be used for the cache
            LinkedList<Integer> set = data.get(sNum);
            int i = 0;
            while (i < assoc) {
                if (blocks[sNum][i].empty) { 
                    //runs through cache ro find a empty space
                    i += 1;// counter
                    return i;
                }
            }
            // remove the set from cache
            return set.remove();
        }

        int locationOf(BigInteger tagBits, int sNum) {
            //method that returns the spot of the tag and returns -1 if there is no tag
            for (int i = 0; i < assoc; i++) {
                if (blocks[sNum][i].tagBits != null) {
                    //checks if tag bit is null
                    if (blocks[sNum][i].tagBits.compareTo(tagBits) == 0) {
                        //compares tag bits
                        return i;
                    }
                }
            }
            return -1; // returns -1 if location is not found
        }

        void readCache(BigInteger tagBits, int sNum) {
            // method for read cache
            int location = locationOf(tagBits, sNum);
            // represents the location calls locationOf method
            if (location != -1) {
                updateData(sNum, location);
                // calls update data method
            } else {
                mNum+=1;
                //adds 1 to the miss rate
                location = free(sNum);
                // calls free method
                cacheBlock block = blocks[sNum][location];
                block.tagBits = tagBits;
                //sets tag bits
                block.empty = false;
                // sets block to not empty
                rNum+=1;
                //add 1 to the read 
                if (WBP == WB) {
                    if (block.bad) {
                        //write num adds 1
                        wNum += 1;
                    }
                    block.bad = false;
                    // sets dirty bit to false
                }
                updateData(sNum, location);
                // update the data 
            }
        }

        void writeCache(BigInteger tagBits, int sNum) {
            // method for writing to cache
            cacheBlock block;
            int location = locationOf(tagBits, sNum);
            //calls the location method
            if (location != -1) {
                // if its not a good location
                block = blocks[sNum][location];
                block.empty = false;
                // sets block to not empty
                block.tagBits = tagBits;
                // sets the tag bits
                if (WBP == 0) {
                    // add 1 to write num
                    wNum += 1;
                } else if (WBP == 1) {
                    block.bad = true;
                }
                //update the data in the cache set
                updateData(sNum, location);
            } else {
                mNum += 1;
                location = free(sNum);
                block = blocks[sNum][location];
                block.tagBits = tagBits;
                block.empty = false;
                rNum++;
                if (WBP == 0) {
                    // add 1 to write cache
                    wNum += 1;
                } else if (WBP == 1) {
                    if (block.bad) {
                        wNum += 1;
                    }
                    blocks[sNum][location].bad = true;
                }
                //update the data in the cache set
                updateData(sNum, location);
            }
        }

        void updateData(int sNum, int location) {
            LinkedList<Integer> set = data.get(sNum);
            if (set.isEmpty()) {
                //theres nothing in the set
                set.add(location);
                //add set to location
            } else {
                if (RP == LRU) {
                    //replacement policy = LRU
                    int TI = set.indexOf(location);
                    // target index set to location
                    if (TI != -1) {
                        // target index is empty
                        set.remove(TI);
                        //remove from set
                    }
                }
                set.add(location);
                // add set to location
            }
        }
    }
}
