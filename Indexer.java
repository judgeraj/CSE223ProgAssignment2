/*
Rajpreet Judge
Programming Assignment
CSE 223
Nick Macias

This is the indexer class, you will be opening a file and this class will allow you to
process that file (if file is corrupt or has a typo in name, program will exit and return null)
the class will keep track of each word and store the position at which the word is said in the file
you will be able to access the location of the word, number of times it appears. better more indepth 
descriptions will be near methods
*/

//importing all needed packages
import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class Indexer { //start of the class called Indexer

 
  private HashMap<String, LinkedList<Integer>> index; //declaring my hashmap which takes a key (string) and stores a linkedlist of integers
  private LinkedList<Integer> myList; //declaration of my linkedlist stores integers which is the location of the word in the file
  private boolean fileFailed = true; //fileFailed is true unless a file is open and read properly then it comes false. this allows methods to not function with incorrect inputs

  /*
  cleanupWord is a really important method, this method will clean up words.
  meaning if a word has extra non characters attached to it or has random 
  noncharacters inbetween characters this will remove them and return the
  cleaned word.
  */

  private String cleanupWord(String word) { //this is a private method because we dont want the user to access this method in anyway to break our program

    String output = "";//this empty string will hold our cleaned characters

    for(int i = 0; i < word.length(); i++) { //start of the loop to clean up chars
      if(Character.isLetter(word.charAt(i)))  //if the char at i'th position is a letter then we jump inside the if
        output += word.charAt(i); //we concatenate the letter to the output string
    }

    return (output.toUpperCase().trim()); //we return the output string converted to uppercase and trimmed of the addition whitespace

  }

  /*
  addReference is also one of the more important methods in this class hence why its a private method. This method will check the
  hashmap to see if the passed word is in the hasmap alread and then if it is then another location will be added to the list
  if not then a new linkedlist object will be created and then that object will be added to the hashmap along with the key.
  */

  private void addReference(String word, int location) { //here we have addReference it takes String and an int
     							//we definitely do not want the user to see or access this code because it can lead to direct crashes/errors
    myList = index.get(word); //here we check out hashmap for a key if null is returned that key is not in our hashmap
 
    if(myList == null) { //checking to see if the return from our key search is null
      myList = new LinkedList<Integer>(); //create a new linkedlist object
      index.put(word, myList);  //put the key and linkedlist object in the hashmap called index
    }

    myList.add((location + 1)); //then add the position of the word in the linkedlist

  }
    
  /*
  This is the processFile method, here we perform all our file opening, file closing and then we
  call all of our cleanupWord methods and add methods. as you can tell this is really important
  to the functionaility to our class. This is also why its public without the user having access to
  this method the whole class is useless.
  */

  public boolean processFile(String filename) { //this method just takes a String which is also the filename
     
    Scanner scan; //declaring a scanner named scan
    index = new HashMap<String, LinkedList<Integer>>(); //constructing a new hashmap;

    try { //here is my try catch block
      scan = new Scanner(new File(filename)); //i try to open this file which may throw an exception
    } 

    catch(Exception e) { //if the exception is thrown I catch it here.
      System.out.println("Error in opening the file! Exiting Program"); //I print out an error message that will appear before exiting
      return false; //I return false because we failed to open the file
    }

    int count = 0; //I declare and initialize the location counter called count set it to 0;
    fileFailed = false; //since we made to this point we must have succesfully opened the file so we make fileFailed false

    while(scan.hasNext()) { //we will keep looping here until there is no more lines (eof)
      String inputWord = scan.next(); //store the word into inputword
      String trimedWord = cleanupWord(inputWord); //call cleanupWord pass it inputWord and store the return into trimedWord

      if(trimedWord.isEmpty()) //checking to see if the word returned happens to be an empty string
        continue; //just continue and dont store into hash
      else {
        addReference(trimedWord,count); //call addReference to add the key (which is the word) and the count variable (which is the location of that word)
        count++; //increment my count to the location of the next word
      }
    }

    scan.close(); //closing the file's output stream
    return true; //return true after we exit the loop

  }

  /*
  locationOf method does kind of exactly what the methods name suggests lol
  this will return the location of the given word provided an instance number example below

  HELLO HELLO HELLO (THESE ARE IN THE FILE)
  locationOf("HELLO", 2) returns 3
  */

  public int locationOf(String word, int instance) { //returns position of where the word appears given the instance

    if(fileFailed)  //checking to see if the file was even successfully opened
      return -1;  //returning -1 if it was unsuccessful
    if(numberOfInstances(word) == -1) //checking to see if the word is even in the hashmap
      return -1; //returning -1
    String trimedWord = cleanupWord(word); //we clean the word passed by the user for error handling
    
    int numberOfTimes = index.get(trimedWord).size(); //we create an int called numberOfTimes that is = to the size of the linked list
 
    if((numberOfTimes <= instance) || (instance < 0)) //we use that size to determine if the users instance number passed is a valid location
      return -1; //return -1 if they arent
 
    return(index.get(trimedWord).get(instance)); //we ill return the location of that letters location besided on the instance it was said

  }
  
  /*
  This is an extremely simple method that just returned the numberofwords in the hash
  */

  public int numberOfWords() { //takes no arguements just returns an integer

    if(fileFailed) //checking to see if the file was opened
      return -1; //returning -1 if it wasnt opened

    return index.size(); //returned the global variable that we had been incrementing in addReference
  }
  
  /*
  numberOfInstances method is an extremely short method but has a lot of stuff happening
  we will take a String as an argument that we will use to get the linkedlist associated
  to that word and then return that size of that list
  */

  public int numberOfInstances(String word) { //String argument that is passed by the user
    
    if(fileFailed) //checking the if the filed was opened or failed at opening
      return -1; //return -1 if not opened successfully

    String trimedWord = cleanupWord(word); //calling cleanupWord that is going to be used inside

    if(index.get(trimedWord) == null) //we check to see if that word is even a key for our hashmap
      return -1; //return -1 in the case its not in the hashmap
   
    return(index.get(trimedWord).size()); //return the size of the linkedlist associated to that key

  }
  
  /*
  toString method will be a super simple method that like others hopes the file was opened properly
  */
  
  public String toString() { //this method takes no arguments and returns a String

    if(fileFailed) //checking to see if the file passed was opened properly
      return ("null");  //return null if that file wasnt opened

    return(index.toString()); //return a string verison of that hashmap

  } 
    
} //end of the class called indexer


