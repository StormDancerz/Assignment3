My original Gifts.java program would work fine on low numbers but then return out of bounds errors once the threads reached large numbers. 
I was unable to fix this within the deadline, so I submitted it as it is. I was then able to fix the program under an hour after the due-date, 
with minor changes to the original code (replacing the arrayList causing out of bounds error with a hashSet)
I decided to push the fixed code to the github, under GiftsNew.java that way the grader may run it and see how the program would have ran to completetion
without the out of bounds errors. The fixed code takes 79594 ms to run and returns the following:
<img width="546" alt="Screen Shot 2023-04-14 at 12 55 22 AM" src="https://user-images.githubusercontent.com/114779107/231945205-071f53f1-57e4-4a0a-abe7-3ae1f6838428.png">

The following is commented at the top of GiftsNew.java

// IMPORTANT NOTE: GiftsNew.java was pushed at 12:50 am, after the due time. 
// Please grade the original Gifts.java, which was pushed before the due time. 
// GiftsNew.java fixes a critical issue of Gifts.java, which while processing the threads eventually gave an out of bounds error 
// for the larger numbers that I was not able to solve in time regarding the ArrayList. I would get several errors that would look like, 
// "Exception in thread "Thread-0" java.lang.IndexOutOfBoundsException: Index 261346 out of bounds for length 261346" 
//  I fixed this by replacing the ArrayList for gifts and cards with a HashSet, which solved the out of bounds errors
// and if you compare the 2 codes they are the same but with fixing the code to work with a HashSet instead of an arrayList to store the cards and gifts 
// I pushed GiftsNew.java that way you can run it and see how the program should run to completion without the error 
