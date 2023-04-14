import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadLocalRandom;

public class Gifts {
    // threads represent each of the servants
    private static final int numThreads = 4;
    private static final int numGifts = 500000;
    static final AtomicInteger cardNum = new AtomicInteger(0);
    static final Object mutex = new Object();

    // servant thread to process the minotaur's gifts and write thank you cards
    private static class ServantThread implements Runnable {
        private final concurrentLinkedList list;
        private final List<Integer> gifts;
        private final List<Integer> cards;

        public ServantThread(concurrentLinkedList list, List<Integer> gifts, List<Integer> cards) {
            this.list = list;
            this.gifts = gifts;
            this.cards = cards;
        }

        @Override
        public void run() {
            processGifts(list, gifts, cards);
        }
    }

    // function to generate a random list
    private static List<Integer> randomList(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }

    // function that alternates between adding gifts to the ordered chain and
    // writing thank you cards until all cards have been written
    private static void processGifts(concurrentLinkedList list, List<Integer> gifts, List<Integer> cards) {
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
                        int num = gifts.get(0);
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
                int randomGuest = ThreadLocalRandom.current().nextInt(gifts.size());
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
        List<Integer> cards = new LinkedList<>();
        Thread[] threads = new Thread[numThreads];

        List<Integer> gifts = randomList(numGifts);
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
