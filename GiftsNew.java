// IMPORTANT NOTE: GiftsNew.java was pushed at 12:50 am, after the due time. 
// Please grade the original Gifts.java, which was pushed before the deadline. 
// GiftsNew.java fixes a critical issue of Gifts.java, which while processing the threads eventually gave an out of bounds error 
// for the larger numbers that I was not able to solve in time regarding the ArrayList. I would get several errors that would look like, 
// "Exception in thread "Thread-0" java.lang.IndexOutOfBoundsException: Index 261346 out of bounds for length 261346" 
//  I fixed this by replacing the ArrayList for gifts and cards with a HashSet, which solved the out of bounds errors
// and if you compare the 2 codes they are the same but with fixing the code to work with a HashSet instead of an arrayList to store the cards and gifts 
// I pushed GiftsNew.java that way you can run it and see how the program should run to completion without the error 

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadLocalRandom;

public class GiftsNew {
    // threads represent each of the servants
    private static final int numThreads = 4;
    private static final int numGifts = 500000;
    static final AtomicInteger cardNum = new AtomicInteger(0);
    static final Object mutex = new Object();

    // servant thread to process the minotaur's gifts and write thank you cards
    private static class ServantThread implements Runnable {
        private final concurrentLinkedList list;
        private final HashSet<Integer> gifts;
        private final HashSet<Integer> cards;

        public ServantThread(concurrentLinkedList list, HashSet<Integer> gifts, HashSet<Integer> cards) {
            this.list = list;
            this.gifts = gifts;
            this.cards = cards;
        }

        @Override
        public void run() {
            processGifts(list, gifts, cards);
        }
    }

    // function to generate a random set
    private static HashSet<Integer> randomSet(int size) {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < size; i++) {
            set.add(i);
        }
        return set;
    }

    // function that alternates between adding gifts to the ordered chain and
    // writing thank you cards until all cards have been written
    private static void processGifts(concurrentLinkedList list, HashSet<Integer> gifts, HashSet<Integer> cards) {
        while (cardNum.get() < numGifts) {
            int task = ThreadLocalRandom.current().nextInt(3);
            switch (task) {
            // Add a gift to the linkedlist
            case 0: {
                synchronized (mutex) {
                    if (gifts.isEmpty()) {
                        continue;
                    }
                    if (!gifts.isEmpty()) {
                        int num = gifts.iterator().next();
                        gifts.remove(num);
                        list.insert(num);
                        break;
                    }

                }
            }
            case 1: {
                // Write thank you cards
                // make sure the list is not empty and the number of cards is less than number
                // of gifts
                // write the card then increment cardNum
                synchronized (mutex) {
                    if (!list.isEmpty() && cardNum.get() < numGifts) {
                        int guest = list.removeHead();
                        cards.add(guest);
                        System.out.println("Thank you card for guest " + guest);
                        // System.out.println("Guests remaining : %d\n", guest, list.size());
                        cardNum.incrementAndGet();
                    }

                    break;
                }
            }

            case 2: {
                int randomGuest = (int) (Math.random() * numGifts);
                boolean found = list.contains(randomGuest);
                if(found) {
                    System.out.println("Guest " + randomGuest + " found");
                } else {
                    System.out.println("Guest " + randomGuest + " not found");
                }

                System.out.println(cardNum.get() + " / " + numGifts + " cards");

                break;

            }

            }
        }
    }

    public static void main(String[] args) {
        // create the concurrentLinkedList and a list for the cards
        // call randomList to populate the list with random values
        // create an array for the threads representing the servants
        // generate servant threads
        long startTime = System.nanoTime();
        concurrentLinkedList list = new concurrentLinkedList();
        HashSet<Integer> cards = new HashSet<>();
        Thread[] threads = new Thread[numThreads];

        HashSet<Integer> gifts = randomSet(numGifts);
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new ServantThread(list, gifts, cards));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Time taken : " + (endTime - startTime) / 1000000 + " milliseconds");
    }
}
