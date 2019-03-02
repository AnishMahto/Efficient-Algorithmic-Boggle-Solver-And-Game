/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author anishmahto
 * Date: Aug 21, 2018
 * Purpose: this class serves as my implementation of the trie data structure, which is heavily used in the BoggleSolver class
 */
public class Trie {
    //as I am not comfortable with pointers and such yet, I like to implement tries simply using a 2d array
    //an index in the trie can be understood as such: trie[node][index of next node using letter]
    static int[][] intTrie = new int[1500001][30];
    //declare a ctr variable to store the index of the greatest/last createad node so far
    static int intCtr = 0;
    
    /**
     * Pre-conditions:
     * @param intPos as the index of the letter in the string we are currently processing
     * @param strWord as the entire string we need to find
     * @param intIdx as the current index of the trie we are currently on
     * Post-conditions:
     * @return return the index in the trie where the string is found if it is found, otherwise return -(the index where the first mismatch is found)
     * Purpose: to check if a given string exists in the trie
     */
    public int intFind (int intPos, String strWord, int intIdx) {
        //get the character at the index intPos in strWord
        char charCurLetter = strWord.charAt(intPos);
        //next we want to convert the integer ASCII value of the current character into a valid index in the trie by subtracting 64
        //that means (int)'A'-64 = 1, (int)'B'-64 = 2, .... (int)'Z'-64 = 26
        //also note that the special string termination character I used is '@', which has an ASCII value of 64, which becomes 0 in the trie: (int)'@' - 64 = 0
        int intLetterIdx = (int)charCurLetter-64;
        if (intTrie[intIdx][intLetterIdx] != 0) {
            //if the conditional evaluates to true, that means there exists a path extending from the current trie index using the letter at intPos 
            if (intPos == strWord.length()-1) {
                //if we are currently at the last letter of strWord, simply return the current index; we have found the word
                return intIdx;
            } else {
                //otherwise while so far we have matched the string, we need to keep going
                //continue the search by moving along the path, and incrementing the current string index pos by 1
                return intFind (intPos+1, strWord, intTrie[intIdx][intLetterIdx]);
            }
        } else {
            //otherwise if no path exists to travel, strWord does not exist in the trie (there is a mismatch at this position)
            //so, return the negative value of this index to indicate this
            return -intIdx;
        }
    }
    
    /**
     * Pre-conditions:
     * @param intPos as the index of the letter in the string we are currently processing
     * @param strWord as the entire string we need to insert
     * @param intIdx as the current index of the trie we are currently on
     * Post-conditions: N/A
     * Purpose: insert the string given in strWord into the trie
     */
    public void insert (int intPos, String strWord, int intIdx) {
        if (intPos==strWord.length()) {
            //if we have finished processing all the letters in the word, we are done inserting it and can end method execution
            return;
        }
        //get the character at the index intPos in strWord
        char charCurLetter = strWord.charAt(intPos);
        //next we want to convert the integer ASCII value of the current character into a valid index in the trie by subtracting 64
        //that means (int)'A'-64 = 1, (int)'B'-64 = 2, .... (int)'Z'-64 = 26
        //also note that the special string termination character I used is '@', which has an ASCII value of 64, which becomes 0 in the trie: (int)'@' - 64 = 0
        int intLetterIdx = (int)charCurLetter-64;
        if (intTrie[intIdx][intLetterIdx] == 0) {
            //if no path currently exists following the current character, then we need to create a path/node
            //increment the node counter by 1
            intCtr++;
            //set the next node from this path equal to the node counter index
            intTrie[intIdx][intLetterIdx] = intCtr;
        }
        //recursively insert the string by moving to the next character in the  string, and moving to the next index in the trie (stored at intTrie[intIdx][intLetterIdx])
        insert (intPos+1, strWord, intTrie[intIdx][intLetterIdx]);
    }
    
}
