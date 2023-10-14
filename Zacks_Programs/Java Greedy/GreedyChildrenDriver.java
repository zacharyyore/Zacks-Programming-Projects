/* PLACE YOUR NAME HERE
   Dr. Steinberg
   COP3503 Fall 2022
   Programming Assignment 3
*/
public class GreedyChildrenDriver
{
public static void main(String [] args) 
{
GreedyChildren test = new GreedyChildren(105, 100, "children1.txt", 
"candy1.txt");
GreedyChildren test2 = new GreedyChildren(800, 750, "children2.txt", 
"candy2.txt");
GreedyChildren test3 = new GreedyChildren(2000, 1000, "children3.txt", 
"candy3.txt");
GreedyChildren test4 = new GreedyChildren(9000, 5000, "children4.txt", 
"candy4.txt");
GreedyChildren test5 = new GreedyChildren(20000, 10000, 
"children5.txt", "candy5.txt");
System.out.println("Testing First Scenario...");
test.greedyCandy();
test.display();
System.out.println("Testing Second Scenario...");
test2.greedyCandy();
test2.display();
System.out.println("Testing Third Scenario...");
test3.greedyCandy();
test3.display();
System.out.println("Testing Fourth Scenario...");
test4.greedyCandy();
test4.display();
System.out.println("Testing Fifth Scenario...");
test5.greedyCandy();
test5.display();
}
}

