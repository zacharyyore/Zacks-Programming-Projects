package banksim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankSim {

    public Lock accessLock = new ReentrantLock();
    public Condition canWithdraw = accessLock.newCondition();
    public int balence = 0;
    public int trasactionNum = 0;
    public int auditCount = 0;

    public BankSim() {
        try {
            File myObj = new File("transactions.txt");
            if (myObj.createNewFile()) {
                //System.out.println("File created: " + myObj.getName());
            } else {
                // System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public class withdrawls implements Runnable {

        private int threadNum;
        private static Random generator = new Random();
        private static Random random = new Random();
        private static Random random2 = new Random();

        public withdrawls(int threadNum) {
            this.threadNum = threadNum;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    accessLock.lock();
                    int sleepRan = random2.nextInt(50 - 20) + 20;
                    int withdrawlNum = random.nextInt(99 - 1) + 1;
                    if (balence - withdrawlNum < 0) {
                        System.out.println("\t\t\t\tAgent WT" + threadNum + " $" + withdrawlNum + "\t\t\t(******) WITHDRAWL BLOCKED - INSUFFICIENT FUNDS!!!");
                        canWithdraw.await();
                    } else {
                        trasactionNum++;
                        auditCount++;
                        balence -= withdrawlNum;
                        System.out.println("\t\t\t\tAgent WT" + threadNum + " $" + withdrawlNum + "\t\t\t" + "(-)  Balence is $" + balence + "\t\t\t\t\t\t" + trasactionNum);

                        if (withdrawlNum > 75) {

                            try {
                                Date today = new Date();
                                DateFormat frenchDateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, Locale.FRANCE);
                                String timestampID = frenchDateTime.format(today);
                                FileWriter fstream = new FileWriter("transactions.txt", true);
                                BufferedWriter out = new BufferedWriter(fstream);
                                out.write("\n\t Agent WT" + threadNum + " issued withdrawal of" + " $" + withdrawlNum + " at: " + timestampID + "\tTransaction Number : " + trasactionNum + "\n");
                                out.close();
                            } catch (Exception e) {
                                System.err.println("Error while writing to file: " + e.getMessage());
                            }

                            System.out.println("\n* * * Flagged Transaction - Withdrawl Agent WT" + threadNum + " Made A Withdrawal In Excess Of $75.00 USD - See Flagged Transaction Log.\n");
                        }
                    }
                    accessLock.unlock();
                    Thread.yield();
                    Thread.sleep(sleepRan);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public class deposits implements Runnable {

        private int threadNum;
        private static Random random = new Random();
        private static Random random2 = new Random();

        public deposits(int threadNum) {
            this.threadNum = threadNum;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    accessLock.lock();
                    int sleepRan = random2.nextInt(300 - 1) + 1;
                    int depositNum = random.nextInt(500 - 1) + 1;
                    balence += depositNum;
                    trasactionNum++;
                    auditCount++;
                    System.out.println("Agent DT" + threadNum + " $" + depositNum + "\t\t\t\t\t\t\t" + "(+)  Balence is $" + balence + "\t\t\t\t\t\t" + trasactionNum);
                    if (depositNum > 350) {
                        try {
                            Date today = new Date();
                            DateFormat frenchDateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, Locale.FRANCE);
                            String timestampID = frenchDateTime.format(today);
                            FileWriter fstream = new FileWriter("transactions.txt", true);
                            BufferedWriter out = new BufferedWriter(fstream);
                            out.write("\n Agent DT" + threadNum + " issued deposit of" + " $" + depositNum + " at: " + timestampID + "\tTransaction Number : " + trasactionNum + "\n");
                            out.close();
                        } catch (Exception e) {
                            System.err.println("Error while writing to file: " + e.getMessage());
                        }
                        System.out.println("\n* * * Flagged Transaction - Depositer Agent DT" + threadNum + " Made A Deposit In Excess Of $350.00 USD - See Flagged Transaction Log.\n");
                    }
                    canWithdraw.signalAll();
                    accessLock.unlock();;
                    Thread.sleep(sleepRan);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }

    }

    public class auditor implements Runnable {

        private static Random random = new Random();

        @Override
        public void run() {

            while (true) {
                try {
                    int sleepRan = random.nextInt(800 - 1) + 1;
                    accessLock.lock();
                    System.out.println("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ** * * * * * * * * * * * * * * * * * * * * *\n");
                    System.out.println("\t\tAUDITOR FINDS CURRENT ACCOUNT BALENCE TO BE: $" + balence + "\t Number of transactions since last audit is: " + auditCount);
                    System.out.println("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ** * * * * * * * * * * * * * * * * * * * * *\n");
                    auditCount = 0;
                    accessLock.unlock();
                    Thread.sleep(sleepRan);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BankSim.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BankSim sim = new BankSim();
        ExecutorService application = Executors.newFixedThreadPool(16);

        System.out.println("Deposit Agents\t\t\t Withdrawal Agents\t\t       Balence                                          Transaction Number");
        System.out.println("--------------\t\t\t -----------------\t\t---------------------\t\t\t\t\t------------------");

        application.execute(sim.new withdrawls(4)); 
        application.execute(sim.new withdrawls(3)); 
        application.execute(sim.new withdrawls(5)); 
        application.execute(sim.new deposits(3));
        application.execute(sim.new withdrawls(2)); 
        application.execute(sim.new deposits(4));
        application.execute(sim.new withdrawls(1)); 
        application.execute(sim.new withdrawls(7)); 
        application.execute(sim.new withdrawls(8)); 
        application.execute(sim.new deposits(0));
        application.execute(sim.new withdrawls(9)); 
        application.execute(sim.new withdrawls(0)); 
        application.execute(sim.new deposits(2));
        application.execute(sim.new withdrawls(6));
        application.execute(sim.new auditor());
        application.execute(sim.new deposits(5));
    }
}
