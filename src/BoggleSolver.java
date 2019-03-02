
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author anishmahto
 * Date: Aug 21, 2018
 * Purpose: this class contains all the algorithms the boggle game page requires
 */
public class BoggleSolver {
    
    //create an instance of the Trie class, to use a trie data structure
    static Trie engWords = new Trie();
    //declare and initalize a 2d array of chars, to store the Boggle board
    static char[][] charBoard = new char[4][4];
    //create a string that stores each letter in the English alphabet, as a capital letter
    static String strLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //create a string that stores all the vowels of the English alphabet
    static String strVowels = "AEIOUY";
    //declare and instantiate a string arraylist to store all the valid words my algorithms find
    static ArrayList <String> strAnswers = new ArrayList<String>();
    //declare a boolean array to check whether a specific letter is being used in the current Boggle board or not
    static boolean[] usingChar = new boolean[201]; //note that only english letter indicies will be accessed, but we declare an array of size 201 just to be safe
    //declare a variable to store the total score achieved by the computer
    static int intScore = 0;
    
    /**
     * Pre-condition:N/A
     * Post-condition:
     * @return strBoard as the newly generated Boggle board, in the form of a string
     * Purpose: to randomly generate a new Boggle board
     */
    static String generateBoard () {
        for (int x = 0; x < 201; x++) {
            //for every index in the usingChar array, set it to false as we are generating a new board
            usingChar[x] = false;
        }
        //declare a variable that will only be set to true if the generated Boggle board contains a vowel
        boolean boolContainsVowel = false;
        
        //create an instance of the random class
        Random rand = new Random();
        //declare a variable to store the string version of the generated Boggle board; we will return this
        String strBoard = "";
        for (int x = 0; x < 4; x++) {
            for (int i = 0; i < 4; i++) {
                //for every cell, randomly choose an index from the strLetters string
                int intRandIdx = rand.nextInt(strLetters.length());
                //set the current cell to the index generated
                charBoard[x][i] = strLetters.charAt(intRandIdx);
                //set the index of the character being used to true
                usingChar[(int)strLetters.charAt(intRandIdx)] = true;
                //add the character being used to the string representation of the Boggle board
                strBoard += charBoard[x][i];
                //using Mr. Jeg's favourite character check method :) we can check if the current letter is a vowel
                if (strVowels.contains("" + charBoard[x][i])) {
                    //if the strVowels string does indeed contain the letter at charBoard, then set boolContainsVowel to true
                    boolContainsVowel = true;
                }
            }
        }
        if (boolContainsVowel) {
            //if the current board configuration contains at least one voewl, return the string representation of the generated Boggle board
            return strBoard;
        } else {
            //otherwise, we need to try generating the board again (as no words can be made if the board contains no vowels)
            //note that the probability of this method having to do n recursive calls is ((20/26)^16)^n. This implies the odds that this method gets called more than even 5 times in a row is exteremly small
            return generateBoard();
        }
    }
    
    /**
     * Pre-conditions: N/A
     * Post-conditions:
     * @return strBoard as the current Boggle board rotated, represented as a string
     * Purpose: to rotate the current Boggle board by 90 degrees counter clockwise
     */
    static String rotateBoard() {
        //declare a string variable to store the string representation of the rotated board
        String strBoard = "";
        //declare a 2d array to store the rotated board
        char [][] charRotatedBoard = new char[4][4];
        for (int x = 0; x < 4; x++) {
            for (int i = 0; i < 4; i++) {
                //for every cell in the rotated board, find the corresponding cell in the current board
                //If you work this out by hand, you will notice a pattern in rotating a cell: (x,y) -> (3-y, x)
                charRotatedBoard[x][i] = charBoard[i][3-x];
            }
        }
        //set the current board equal to the rotated board we just calculated
        charBoard = charRotatedBoard;
        for (int x = 0; x < 4; x++) {
            for (int i = 0; i < 4; i++) {
                //for each cell in our Boggle board (which is now rotated) add its letter to the string representation
                strBoard += charBoard[x][i];
            }
        }
        //return the string representation of the rotated board
        return strBoard;
    }
    
    /**
     * Pre-conditions:
     * @param intPos as the index of the given string currently being assessed when searched for in the trie
     * @param strWord as the given string to be searched for in the trie
     * @param intIdx as the current index of the trie to begin the search
     * Post-conditions:
     * @return an integer representing the index where the string terminates in the trie, or -(index where mismatch is found)
     */
    static int intCheckFullWord (int intPos, String strWord, int intIdx) {
        //because we are checking for a full word, we need to add a termination character that our trie recognizes; the character with ASCII value 64 (the character itself is irrelevant)
        strWord += (char)64;
        //call the intFind method to check the index where the queried string is found, and return the value
        return engWords.intFind (intPos, strWord, intIdx);
    }
    /**
     * Pre-conditions:
     * @param intPos
     * @param strPrefix
     * @param intIdx
     * Post-conditions:
     * @return an integer representing the index where the string terminates in the trie, or -(index where mismatch is found)
     */
    static int intCheckPrefix (int intPos, String strPrefix, int intIdx) {
        //because the current string we are checking is not a full string but rather a prefix, do not add the special termination character
        //simply return the index returned by the intFind() method
        return engWords.intFind (intPos, strPrefix, intIdx);
    }
    
    /**
     * Pre-conditions:
     * @param intPosX as the x position of a given cell in the Boggle board
     * @param intPosY as the y position of a given cell in the Boggle board
     * @return the corresponding string index of a given cell's index
     * Purpose: to return the corresponding string index of a given cell's index 
     */
    static int intConvPos (int intPosX, int intPosY) {
        //using simple math, the corresponding string position is simply the current y position times the number of cells per row plus the x position
        return intPosY*4 + intPosX;
    }
    
    /**
     * Pre-conditions:
     * @param intPosX as the x coordinate/column number the depth first search call is currently on
     * @param intPosY as the y coordinate/row number the depth first search call is currently on
     * @param check as a bitset (basically a boolean array) that stores 1 for indicies that have already been visited, and 0 for indicies not visited so far
     * @param strCurWord as the string created by the accumulation of visiting unvisited cells in the Boggle voard
     * @param intPrevIdx as the index in the trie the previous string was found at
     * Post-conditions: N/A
     * Purpose: serves as the core algorithm; using depth first search on the Boggle board, find all solutions
     */
    static void findWords (int intPosX, int intPosY, BitSet check, String strCurWord, int intPrevIdx) {
        if (strCurWord.length() >= 2) {
            //if the current string has more than 1 letter, that means a letter has been added by the dfs (the first letter was simply the start letter)
            //so, we need to check if the added letter still forms a valid prefix of some english word
            //get the index in the trie where the current string is found
            int intCheckString = intCheckPrefix (strCurWord.length()-2, strCurWord, intPrevIdx);
            if (intCheckString < 0) {
                //if the index returned is less than 0 that means this current string is not a prefix of any english word, so we can simply stop here
                return;
            } else {
                //otherwise if it is the prefix of some english word (hence index > 0), we need to check if it is an english word itself
                intPrevIdx = intCheckString; //the new previous trie index becomes the trie index found in this call
                //using the intCheckFullWord() method, check to see if the current string is found in the trie as a full word
                intCheckString = intCheckFullWord (strCurWord.length()-1, strCurWord, intPrevIdx);
                if (intCheckString > 0 && strCurWord.length() >= 3) {
                    //if the index is greater than 0 that means a full matching word in the english dictionary has been found
                    //however, according to Boggle the length of the word must be greater than or equal to 3 letters, so we need to verify that as well in the if condition
                    //if both conditionals are true this string is a valid word in both the Boggle board and english dictionary, so add it to the strAnswers arraylist
                    strAnswers.add (strCurWord);
                }
            }
        }
        //even if the current string is a valid word, we need to continue trying to add characters as we may discover more english words
        for (int y = Math.max(0, intPosY-1); y <= Math.min(3, intPosY+1); y++) {
            for (int x = Math.max(0, intPosX-1); x <= Math.min (3, intPosX+1); x++) {
                //loop through every cell in a 3x3 grid where the current position is the center
                if ((x!=intPosX || y != intPosY) && !check.get(intConvPos (x, y))) {
                    //if cell being considered is not equal to the current position and the cell being considered has not been visited (hence set in our bitset) yet, we will try continuing our dfs from here
                    //as we will try continuing our dfs at this cell, flip it to true in the check bitset
                    check.flip(intConvPos (x, y));
                    if (charBoard[y][x] != 'Q') {
                        //if the current cell's letter is not Q we can simply append the letter to our current string and continue the dfs at this cell
                        findWords (x, y, check, strCurWord + charBoard[y][x], intPrevIdx);
                    } else {
                        //if the current cell's letter is Q we can append the letter 'Q' and 'U' to our current string and continue the dfs at this cell
                        //note that this is per Boggle rules, and it makes sense as you can never use the letter Q without a 'U' directly after it in the English language
                        findWords (x, y, check, strCurWord + charBoard[y][x] + 'U', intPrevIdx);
                    }
                    //after the recursive call is complete, flip this current cell back to false (unvisited)
                    check.flip(intConvPos (x, y));
                }
            }
        }
    }
    
    /**
     * Pre-condition: N/A
     * Post-condition: N/A
     * Purpose: to sort the strAnswers arraylist using bubble sort
     */
    static void bubbleSortAnswers () {
        //Because the computer rarely ever finds more than 100 words in the given Boggle board, O(n^2) bubble sort is more than fast enough to sort the arraylist
        //note that although bubble sort is n^2, the comparison of strings can take up to O(n), therefore the total time complexity is O(n^3)
        for (int x = 0; x < strAnswers.size()-1; x++) {
            //we will bubble up elements as many times as the number of strings minus one
            for (int i = 1; i < strAnswers.size(); i++) {
                //for every element other than the first, check if the element before it in the arraylist is lexographically greater than it
                if (strAnswers.get(i).compareTo(strAnswers.get(i-1)) < 0) {
                    //if it is, swap the two elements in the arraylist
                    Collections.swap(strAnswers, i, i-1);
                }
            }
        }
    }
    
    /**
     * Pre-condition:N/A
     * Post-condition:N/A
     * Purpose: to begin the process of find all the correct Boggle words in the current board
     */
    static void initFindWords () {
        //set the algorithms score to 0
        intScore = 0;
        //clear any answers found in the last Boggle board configuration
        strAnswers.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                //for every cell in the Boggle board, we will try running a depth first search (dfs) to generate words
                //instantiate a new bitset that will be used to keep track of letters already visited in the dfs
                BitSet temp = new BitSet();
                //set the starting position to visited, as that is where we will start the dfs
                temp.set(intConvPos(x, y));
                //begin the dfs process by passing the current cell x and y index, a string the accumulates the word discoverd so far (since we start at the current cell, add the current cell's letter), and the index we are currently at on the trie (starts at 0)
                findWords (x, y, temp, "" + charBoard[y][x], 0);
            }
        }
        //once we are done discovering all solutions, sort the arraylist that stores them (so that it can be binary searched)
        bubbleSortAnswers();
    }
    
    /**
     * Pre-conditions: 
     * @param intLeft as the left bound of the current binary search recursive call
     * @param intRight as the right bound of the current binary search recursive call
     * @param strUserWord as the word being binary searched for
     * Post-conditions:
     * @return true if the word has been found, false if it is not in the current range, otherwise return the recursive call of the sub problem
     * Purpose: to binary search the strAnswers arraylist to check if the word the user has guessed is a valid answer in the current Boggle board
     */
    static boolean checkUserWord (int intLeft, int intRight, String strUserWord) {
        if (intLeft > intRight) {
            //if the current range is invalid (left bound is greater than right bound) return false
            return false;
        }
        //get the index of the middle term in the current range
        int intMid = (intLeft + intRight)/2;
        if (strAnswers.get(intMid).equals(strUserWord)) {
            //if the word found at the middle of the range equals the user word, return true as the word has been found
            return true;
        } else if (strAnswers.get(intMid).compareTo(strUserWord) < 0) {
            //otherwise if the middle word is less than the user's word, binary search the right half of the range and return the result
            return checkUserWord (intMid+1, intRight, strUserWord);
        } else {
            //else binary search the left half of the range and return the result
            return checkUserWord (intLeft, intMid-1, strUserWord);
        }
    }
    
    /**
     * Pre-conditions:
     * @param strUserWord as the word that must binary searched for (to check if the user word is a correct word according to the algorithm)
     * Post-conditions:
     * @return the result of the binary search for strUserWord; true if the word is found, false otherwise
     * Purpose: to serve as an auxiliary method to binary search for a specific word
     */
    static boolean checkUserWordIntermediary (String strUserWord) {
        //the main reason why I need this auxiliary method is to be able to use the strAnswers.size() method, as I cannot reference it from the BoggleGame class
        //simply return the result of the binary search on the strAnswers arraylist, using the word the user inputted
        return checkUserWord (0, strAnswers.size()-1, strUserWord);
    }
    
    /**
     * Pre-condition:
     * @param strWord as the word we want check validity for
     * Post-condition:
     * @return true if the current word is a valid Boggle word, false otherwise
     * Purpsoe: given an english word, determine if it is a valid Boggle word or not
     */
    static boolean boolValidWord (String strWord) {
        if (strWord.length() < 3 || strWord.length() > 16) {
            //by Boggle rules, if the word has length less than 3, it is not a valid word
            //furthermore, the Boggle board I coded is 4x4, meaning that if the length is greater than 16, it is an invalid word (letters cannot be reused in making the same word)
            return false;
        }
        if (!usingChar[(int)strWord.charAt(0)]) {
            //this is just a constant optimization
            //if the first letter used in this word does not exist in the current Boggle board, this word cannot be valid
            return false;
        }
        for (int x = 0; x < strWord.length(); x++) {
            //loop through every character in the word
            if ((int)strWord.charAt(x) > (int)'Z' || (int)strWord.charAt(x) < (int)'A') {
                //if the character does not fall in the range of capital english letters (ex. dashes), it is not a valid Boggle word 
                return false;
            }
        }
        //if the method reaches this point, it is a valid word, so return true
        return true;
    }
    
    /**
     * Pre-conditions: N/A
     * Post-conditions: N/A
     * Purpose: read a file with approximately 200,000 english words, and insert each word into the trie data structure
     */
    void fillTrie () {
        //declare a BufferedReader object, but dont initalize it yet
        BufferedReader br = null;
        //declare a string to store the input line when reading the file
        String strWord;
        try {
            //https://raw.githubusercontent.com/dwyl/english-words/master/words_alpha.txt
            //initalize the BufferedReader object while passing a new InputStreamReader object (that refers to the file location of the file)
            InputStream in = getClass().getResourceAsStream("/EnglishWords.txt");
            br = new BufferedReader (new InputStreamReader(in));
            //br = new BufferedReader (new FileReader("src/EnglishWords.txt"));
            while ((strWord = br.readLine()) != null) {
                //while strWord reads the current line in the EnglishWords.txt file and it is not an empty (null) line, do the following
                if (boolValidWord (strWord)) {
                    //if the current english word is a valid Boggle word, then we need to add it to the trie
                    //note that this gets rid of words with dashes, etc.
                    //the special string termination character that the trie uses is the character with ASCII value 64 (actual character itself is irrelevant), so add this character to the end of the current word
                    strWord += (char)64;
                    //insert the word into the trie, starting at index 0 of the string and index 0 of the trie
                    engWords.insert (0, strWord, 0);
                }
            }
        } catch (IOException e) {
            //catch IOException errors from trying to read input from the file
            e.printStackTrace();
        } finally {
            try {
                //if we are done, try closing the BufferedReader
                br.close();
            } catch (IOException ex) {
                //catch any IOException errors when trying to close the reader
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Pre-conditions:
     * @param strWord as the word to grade/determine how many points its worth
     * Post-conditions:
     * @return the point associated with strWord
     * Purpose: given a word, determine the point value of that word
     */
    static int intWordPts (String strWord) {
        //the points associated with a word dependent on the length of the word which is a single integer
        //so, we can use a switch statement here, on the length of strWord
        //notice that if the length of a word is less than 3 it will never be passed through this method, as that word is invalid
        switch (strWord.length()) {
            case 3:
            case 4:
                //if the length of the word if 3 or 4, the score is 1
                return 1;
            case 5:
                //if the length of the word is 5, the score is 2
                return 2;
            case 6: 
                //if the length of the word is 6, the score is 3
                return 3;
            case 7:
                //if the length of the word is 7, the score is 5
                return 5;
            default:
                //otherwise if the length of the word is greater than 7, the score is 11
                return 11;
        }
    }
    
    /**
     * Pre-conditions: N/A
     * Post-conditions:
     * @return strOutput as the string that conatins the algorithm's answers to the current Boggle board
     * Purpose: to return all the valid words the algorithm found in a single string 
     */
    static String strCompAnalysis () {
        //declare a string variable to store the algorithm's answers, and set it equal to some default text
        String strOutput = "Computer Analysis: \n";
        //declare integer variables to store the number of unique words the algo found, and the number of points each word the algo found is worth
        int intCnt = 0, intPts;
        for (int x = 0; x < strAnswers.size(); x++) {
            //loop through every word in the strAnswers arraylist
            if (x==0 || !strAnswers.get(x).equals(strAnswers.get(x-1))) {
                //if the current word is the first word, or the current word does not equal the previous word, this word is a unique word
                //notice that we can use this method because the strAnswers arraylist is sorted
                //store the points associated with the current word into intPts, using the intWordPoints() method
                intPts = intWordPts(strAnswers.get(x));
                //append the current word and its corresponding points to the output string
                strOutput += strAnswers.get(x) + " (" + intPts + ")\n";
                //add the points corresponding to the current string to the aglgorithms total score
                intScore += intPts;
                //increment the counter for the number of unique words the algo has found
                intCnt++;
            }
        }
        //append the unique word count and total score stats to the output string
        strOutput += "Computer Score: " + intScore + "\n";
        strOutput += "Total Words Computer Found: " + intCnt + "\n\n";
        //return the output string
        return strOutput;
    }
    
}