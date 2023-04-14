# Assignment3
Processes of Parallel and Distributed Programming Assignment 3

Running: Download Gifts.java and concurrentLinkedList.java. Also download TemperatureReading.java. 
Open terminal and navigate to the folder where the above files are saved.

To run Gifts.java, Type in the terminal: javac Gifts.java followed by java Gifts. Make sure that concurrentLinkedList is saved in the same folder.

To run TemperatureReading.java, Type in the terminal: javac TemperatureReading.java followed by java TemperatureReading

Statements: Gifts.java simulates the process of the minotaur's servants going through his gifts are writing thank you notes to his guests. The 4 servants are represented by 4 threads, represented by the ServantThread class. The randomList function populates a list with random values to represent the minotaurs gifts that need to be processed. The process gifts function processes the minotaur's gifts, alternating between adding gifts to the concurrent linked list and writing thank you cards to the guests. 

Temperature reading.java simulates the 8 temperature sensors, which measure the temperates and then print out the top 5 highest and lowest temperatures, as well as the biggest difference between a 10 minute interval for that hour. It does this for 100 seperate hours although this can be adjusted by modifying the numHours variable. 

Efficiency and Experimental evaluation: 
The Gifts program takes a little while to run at 1318692 ms. It's possible to optimize it better either by remove print statements or finding a more efficient way to multithread. 

The Temperature Reading program runs very efficiently with it's 8 threads, taking 222 ms to run. 

Proof of correctness:
The Gifts program uses 4 servant threads and makes use of a mutex to allow the threads to complete work concurrently. The Temperature program uses 8 temperature sensor threads and makes use of a semapore mutex to allow the threads to complete work concurrently. So far there do not appear to be cases of deadlock or thread starvation in either program.
